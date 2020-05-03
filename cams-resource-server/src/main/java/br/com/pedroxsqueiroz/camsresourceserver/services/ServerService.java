package br.com.pedroxsqueiroz.camsresourceserver.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.pedroxsqueiroz.camsresourceserver.config.NodeConfig;
import br.com.pedroxsqueiroz.camsresourceserver.exceptions.ServerNotRegisteredException;
import br.com.pedroxsqueiroz.camsresourceserver.models.ClientModel;
import br.com.pedroxsqueiroz.camsresourceserver.models.NodeModel;
import br.com.pedroxsqueiroz.camsresourceserver.models.ServerModel;
import lombok.extern.java.Log;

@Service
@Log
public class ServerService extends AbstractNodeService<ServerModel> {

	@Autowired
	private MessagingService messagingService;
	
	@Autowired
	private NodeConfig thisClientConfig;

	public void registerClient(String serverAddress, ClientModel client) {

		RestTemplate restTemplate = new RestTemplate();

		String saveClientUrl = String.format("%s/clients/", serverAddress);

		ResponseEntity<ClientModel> saveClientResponse = restTemplate.postForEntity(saveClientUrl, client,
				ClientModel.class);

		// FIXME: VERIFICAR RESPOSTA E TRATAR ERROS
	}

	@Override
	public ServerModel save(ServerModel serverData) {
		String serverAddress = serverData.getAddress();

		// SELF REGISTER AS CLIENT ON SERVER
		ClientModel thisAsClient = new ClientModel(this.thisClientConfig);
		this.registerClient(serverAddress, thisAsClient);
		
		NodeConfig serverConfig = this.getNodeConfig(serverAddress);
		ServerModel server = new ServerModel(serverConfig);
		
		ServerModel save = super.save(server);
		
		this.messagingService.connectServer(server);

		return save;
	}

	public NodeModel findByKey(String nodeKey) throws ServerNotRegisteredException {
		
		ServerModel entryWithKey = ServerModel.builder().key(nodeKey).build();
		
		Optional<ServerModel> serverEntry = this.getDao().findOne( Example.of( entryWithKey ) );
		
		if(!serverEntry.isPresent()) 
		{
			throw new ServerNotRegisteredException();
		}
		
		return serverEntry.get();
	}

}
