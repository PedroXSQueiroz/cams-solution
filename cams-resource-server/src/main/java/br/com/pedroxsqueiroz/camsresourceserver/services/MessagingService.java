package br.com.pedroxsqueiroz.camsresourceserver.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;

import br.com.pedroxsqueiroz.camsresourceserver.config.NodeConfig;
import br.com.pedroxsqueiroz.camsresourceserver.exceptions.ServerNotRegisteredException;
import br.com.pedroxsqueiroz.camsresourceserver.models.ClientModel;
import br.com.pedroxsqueiroz.camsresourceserver.models.NodeModel;
import br.com.pedroxsqueiroz.camsresourceserver.models.ServerModel;
import lombok.extern.java.Log;

@Service
@Log
public class MessagingService {

	@Autowired
	private ServerService serverService;
	
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private NodeConfig thisClientConfig;
	
	@Autowired
	private MessagingChannelManagerService channelManager;
	
	private Map<String, MessageCallback> queues = new HashMap<String, MessageCallback>();
	
	public static interface MessageCallback
	{
		byte[] execute(String messageTag, Delivery message);
	}
	
	public void on(final String action, MessageCallback callback) throws IOException, ServerNotRegisteredException, TimeoutException 
	{
		this.registerQueue(action, callback);
		this.queues.put(action, callback);
	}
	
	public void connectServers() 
	{
		this.serverService.list().forEach(this::connectServer);
	}
	
	public void connectServer(ServerModel server) 
	{
		
		
		this.queues.entrySet().forEach( callbackEntry -> {
			
			try {
				
				String action = callbackEntry.getKey();
				MessageCallback callback = callbackEntry.getValue();
				
				this.registerQueue(action, callback, server);
			
			} catch (IOException | ServerNotRegisteredException | TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	
	private void registerQueue( String action, MessageCallback callback )
			throws IOException, ServerNotRegisteredException, TimeoutException {
		
		List<NodeModel> allRelatedNodes = new ArrayList<NodeModel>();
		
		allRelatedNodes.add( this.thisClientConfig );
		
		List<ServerModel> servers = this.serverService.list();
		allRelatedNodes.addAll(servers);
		
		List<ClientModel> clients = this.clientService.list();
		allRelatedNodes.addAll(clients);
		
		allRelatedNodes.forEach( server -> {
			
			try {
				
				this.registerQueue(action, callback, server);
			
			} catch (IOException | ServerNotRegisteredException | TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		});
		
	}

	private void registerQueue(String action, MessageCallback callback, NodeModel server)
			throws IOException, ServerNotRegisteredException, TimeoutException {
		
		this.registerQueue(action, callback, server, new ClientModel(this.thisClientConfig));
	}
	
	private void registerQueue(String action, MessageCallback callback, NodeModel server, ClientModel client) throws IOException, ServerNotRegisteredException, TimeoutException 
	{
		Channel channel = this.channelManager.getChannel(server);
		
		String nodeKey = client.getKey();
		final String queueName = String.format("%s.%s", nodeKey, action);
		final String queueResponseName = String.format("%s.%s.response", nodeKey, action);
		
		channel.queueDeclare( queueName, false, false, false, null );
		channel.queueDeclare( queueResponseName, false, false, false, null );
		
		channel.basicConsume( 
				queueName , 
				true, 
				(tag, message) -> {
					
					byte[] result = callback.execute(tag, message);
					
					channel.basicPublish("", queueResponseName, null, result);
					
				}, 
				consumerTag -> {});
	}
	
	//FIXME: IMPLEMENTAR CORRELATION ID PARA VERIFICAÇÃO DA RESPOSTA CORRETA
	public <T> T send(NodeModel destinyNode, String action, Object data, final Class<T> responseType) throws IOException, InterruptedException, ServerNotRegisteredException, TimeoutException 
	{
		String serverKeyDestiny = destinyNode.getKey();
		
		String queueName = String.format("%s.%s", serverKeyDestiny, action);
		String queueResponseName = String.format("%s.response", queueName);
		
		Channel channel = this.channelManager.getChannel(this.thisClientConfig);
		
		ObjectMapper requestBodySerializer = new ObjectMapper();
		byte[] requestBody = requestBodySerializer.writeValueAsBytes(data);
		
		if(!queues.containsKey(action)) 
		{
			this.registerQueue(action, ( tag, message ) -> message.getBody(), new ServerModel(this.thisClientConfig), new ClientModel(destinyNode) );
		}

		ArrayBlockingQueue<T> awaiter = new ArrayBlockingQueue<T>(1);

		String consumer = channel.basicConsume(queueResponseName, true, "", (tag, delivery) -> {

			byte[] body = delivery.getBody();
			ObjectMapper responseBodySerializer = new ObjectMapper();
			T response = responseBodySerializer.readValue(body, responseType);

			awaiter.offer(response);

		}, consumerTag -> {});

		channel.basicPublish("", queueName, null, requestBody);

		T response = awaiter.take();
		
		channel.basicCancel(consumer);
		
		return response;
		
		
	}
	
}
