package br.com.pedroxsqueiroz.camsresourceserver.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.com.pedroxsqueiroz.camsresourceserver.exceptions.ServerNotRegisteredException;
import br.com.pedroxsqueiroz.camsresourceserver.models.CamModel;
import br.com.pedroxsqueiroz.camsresourceserver.models.ClientModel;

@Service
public class ClientService extends AbstractNodeService<ClientModel>{

	@Autowired
	private MessagingService messagingService;
	
	@Autowired
	private CamService camsService;
	
	public List<CamModel> allClientCams() {
		// TODO Auto-generated method stub
		
		List<CamModel> cams = this.list().stream().flatMap(client -> {
		
			return  this.camsService.getCamsFromClient(client).stream() ;
		
		}).collect(Collectors.toList());
		
		return cams;
		
	}
	
	

	
}
