package br.com.pedroxsqueiroz.camsresourceserver.models;

import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.pedroxsqueiroz.camsresourceserver.config.NodeConfig;
import lombok.Builder;

@Table(name = "server")
@Entity
public class ServerModel extends NodeModel{
	
	public ServerModel() {
		
	}
	
	public ServerModel(NodeConfig config) {
		super(config);
	}
	
	@Builder
	public ServerModel(
			Integer id, 
			String name, 
			String address, 
			String messagingBrokerAddress,
			Integer messagingBrokerPort,
			String messagingBrokerUsername,
			String messagingBrokerPassword,
			String key,
			Integer hubInputPort,
			Integer hubOutputPort) {
		
		super(
				id, 
				key, 
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
	
}
