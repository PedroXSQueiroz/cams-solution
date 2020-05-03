package br.com.pedroxsqueiroz.camsresourceserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pedroxsqueiroz.camsresourceserver.models.NodeModel;
import lombok.Getter;
import lombok.Setter;

@Component
public class NodeConfig extends NodeModel {
	
	@Override
	@Value("${node.name}") 
	public void setName(String name) 
	{
		super.setName(name);
	}
	
	@Override
	@Value("${node.address}") 
	public void setAddress(String address) 
	{
		super.setAddress(address);
	}
	
	@Override
	@Value("${messaging_broker.address}") 
	public void setMessagingBrokerAddress(String messagingBrokerAddress) 
	{
		super.setMessagingBrokerAddress(messagingBrokerAddress);
	}
	
	@Override
	@Value("${messaging_broker.port}") 
	public void setMessagingBrokerPort(Integer messagingBrokerPort) 
	{
		super.setMessagingBrokerPort(messagingBrokerPort);
	}
	
	@Override
	@Value("${hub.input_port}") 
	public void setHubInputPort(Integer hubInputPort) 
	{
		super.setHubInputPort(hubInputPort);
	}
	
	@Override
	@Value("${hub.output_port}") 
	public void setHubOutputPort(Integer hubOutputPort) 
	{
		super.setHubOutputPort(hubOutputPort);
	}
	
	@Override
	@Value("${hub.host}")
	public void setHubHost(String hubHost) 
	{
		super.setHubHost(hubHost);
	}
	
	@Override
	@Value("${messaging_broker.username}") 
	public void setMessagingBrokerUsername(String messagingBrokerUsername) 
	{
		super.setMessagingBrokerUsername(messagingBrokerUsername);
	}
	
	@Override
	@Value("${messaging_broker.password}") 
	public void setMessagingBrokerPassword(String messagingBrokerPassword) 
	{
		super.setMessagingBrokerPassword(messagingBrokerPassword);
	}
	
	public NodeConfig() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NodeConfig(
			String name, 
			String address, 
			String messagingBrokerAddress,
			Integer messagingBrokerPort, 
			Integer hubInputPort, 
			Integer hubOutputPort,
			String messagingBrokerUsername,
			String messagingBrokerPassword ) {
		
		super(
				name, 
				address, 
				messagingBrokerAddress, 
				messagingBrokerPort, 
				messagingBrokerUsername, 
				messagingBrokerPassword, 
				hubInputPort, 
				hubOutputPort
			);
		
	}

	public NodeConfig(NodeModel config) {
		super(config);
	}
	
	@Override
	public String toString() {
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		try 
		{
			
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
		
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return super.toString();
		}
		
	}

}
