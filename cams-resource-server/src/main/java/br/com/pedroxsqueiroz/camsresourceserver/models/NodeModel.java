package br.com.pedroxsqueiroz.camsresourceserver.models;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import br.com.pedroxsqueiroz.camsresourceserver.config.NodeConfig;
import br.com.pedroxsqueiroz.camsresourceserver.models.ServerModel.ServerModelBuilder;
import lombok.Builder;
import lombok.Data;

@Data	
@MappedSuperclass
public class NodeModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "messaging_broker_address")
	private String messagingBrokerAddress;
	
	@Column(name = "messaging_broker_port")
	private Integer messagingBrokerPort;
	
	@Column(name = "messaging_broker_username")
	private String messagingBrokerUsername;
	
	@Column(name = "messaging_broker_password")
	private String messagingBrokerPassword;
	
	@Column(name = "hub_input_port")
	private Integer hubInputPort;
	
	@Column(name = "hub_output_port")
	private Integer hubOutputPort;
	
	@Column(name = "hub_host")
	private String hubHost; 

	@Column(name = "node_key")
	private String key;
	
	
	public NodeModel() 
	{
		super();
	}
	
	public NodeModel(NodeModel config) 
	{
		this.setName( config.getName() );
		this.setMessagingBrokerAddress( config.getMessagingBrokerAddress() );
		this.setMessagingBrokerPort( config.getMessagingBrokerPort() );
		this.setAddress( config.getAddress() );
		this.setKey( config.getKey() );
		this.setHubInputPort(config.getHubInputPort());
		this.setHubOutputPort(config.getHubOutputPort());
		this.setMessagingBrokerUsername( config.getMessagingBrokerUsername() );
		this.setMessagingBrokerPassword( config.getMessagingBrokerPassword() );
	}

	public NodeModel(
			String name, 
			String address, 
			String messagingBrokerAddress,
			Integer messagingBrokerPort,
			String messagingBrokerUsername,
			String messagingBrokerPassword,
			Integer hubInputPort, 
			Integer hubOutputPort) 
	{
		super();
		this.name = name;
		this.address = address;
		this.messagingBrokerAddress = messagingBrokerAddress;
		this.messagingBrokerPort = messagingBrokerPort;
		this.hubInputPort = hubInputPort;
		this.hubOutputPort = hubOutputPort;
	}
	
	public NodeModel(
			Integer id, 
			String name, 
			String address, 
			String messagingBrokerAddress,
			Integer messagingBrokerPort, 
			String messagingBrokerUsername, 
			String messagingBrokerPassword, 
			Integer hubInputPort, 
			Integer hubOutputPort) {
		
		this(
				name, 
				address, 
				messagingBrokerAddress, 
				messagingBrokerPort, 
				messagingBrokerUsername, 
				messagingBrokerPassword, 
				hubInputPort, 
				hubOutputPort
			);
		
		this.id = id;
	}
	
	public NodeModel(
			Integer id, 
			String key, 
			String name, 
			String address, 
			String messagingBrokerAddress,
			Integer messagingBrokerPort, 
			String messagingBrokerUsername, 
			String messagingBrokerPassword, 
			Integer hubInputPort, 
			Integer hubOutputPort) {
		
		this(
				id, 
				name, 
				address, 
				messagingBrokerAddress, 
				messagingBrokerPort, 
				messagingBrokerUsername, 
				messagingBrokerPassword,
				hubInputPort, 
				hubOutputPort
			);
		
		this.key = key;
	}
	
}
