package br.com.pedroxsqueiroz.camsresourceserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.pedroxsqueiroz.camsresourceserver.config.NodeConfig;
import br.com.pedroxsqueiroz.camsresourceserver.models.ServerModel;

@Controller
@RequestMapping(value = "servers")
@CrossOrigin("*")
public class ServerController extends AbstractNodeController<ServerModel>{
	
	@Autowired
	private NodeConfig nodeConfig;
	
	@GetMapping(value = "self")
	@ResponseBody
	public NodeConfig getSelfConfig() 
	{
		return this.nodeConfig;
	}
	
}
