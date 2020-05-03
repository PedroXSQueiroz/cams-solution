package br.com.pedroxsqueiroz.camsresourceserver.services;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import br.com.pedroxsqueiroz.camsresourceserver.config.NodeConfig;
import br.com.pedroxsqueiroz.camsresourceserver.models.NodeModel;

public class AbstractNodeService<T extends NodeModel> extends AbstractService<T>{

	public NodeConfig getNodeConfig(String address) 
	{
		RestTemplate restTemplate = new RestTemplate();
		
		String nodeConfigurationUrl = String.format("%s/servers/self", address);
		
		ResponseEntity<NodeConfig> nodeConfigResponse = restTemplate.getForEntity(nodeConfigurationUrl, NodeConfig.class);
		
		NodeConfig nodeConfig = nodeConfigResponse.getBody();
		
		return nodeConfig;
	}

	public T getByKey(String key) 
	{
		
		JpaSpecificationExecutor<T> currentDao =  (JpaSpecificationExecutor<T>) this.dao ;
		
		Optional<T> findNodeQuery = currentDao.findOne( (root, query, cb) -> 
			cb.equal( root.get("key"), key )
		 );
		
		return findNodeQuery.get();
		
	}
	
}
