package br.com.pedroxsqueiroz.camsresourceserver.controllers;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import br.com.pedroxsqueiroz.camsresourceserver.config.NodeConfig;
import br.com.pedroxsqueiroz.camsresourceserver.dtos.StreamCam;
import br.com.pedroxsqueiroz.camsresourceserver.models.ClientModel;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pedroxsqueiroz.camsresourceserver.exceptions.ServerNotRegisteredException;
import br.com.pedroxsqueiroz.camsresourceserver.models.CamModel;
import br.com.pedroxsqueiroz.camsresourceserver.services.CamService;
import br.com.pedroxsqueiroz.camsresourceserver.services.MessagingService;

@Controller
@RequestMapping(value = "cams")
@CrossOrigin("*")
public class CamController{

	@Autowired
	private MessagingService messagingService;
	
	@Autowired
	private CamService service;

	@Autowired
	private NodeConfig thisNode;

	@GetMapping("/{id}")
	@ResponseBody
	public CamModel get(@PathVariable("id") Integer id) 
	{
		return this.service.get(id);
	}
	
	@GetMapping
	@ResponseBody
	public List<CamModel> list()
	{
		return this.service.list();
	}
	
	@PostMapping
	@ResponseBody
	public CamModel save(@RequestBody CamModel cam) 
	{
		return this.service.save(cam);
	}
	
	@PutMapping(value = "/{id}")
	@ResponseBody
	public CamModel update(@PathVariable("id") Integer id, @RequestBody CamModel cam) 
	{
		return this.service.update(id, cam);
	}

	@PostMapping(value = "/{id}/stream")
	@ResponseBody
	public StreamCam stream(@PathVariable("id") Integer camId) throws InterruptedException, TimeoutException, ServerNotRegisteredException, IOException {
		CamModel cam = this.service.get(camId);
		return this.service.startStream( new ClientModel( this.thisNode ), cam);
	}

	@PostMapping(value = "/{camId}/record/{start}")
	@ResponseBody
	public JsonNode recordStream(
			@PathVariable("camId") Integer camId,
			@PathVariable("start") Boolean start,
			@RequestParam("path") String path) throws InterruptedException, TimeoutException, ServerNotRegisteredException, IOException {

		Boolean success;

		CamModel cam = this.service.get(camId);

		if(start)
		{
			success = this.service.startRecordStream( new ClientModel(this.thisNode), cam, path );
		}else
		{
			success = this.service.stopRecordStream( new ClientModel(this.thisNode), cam );
		}

		ObjectNode response = JsonNodeFactory.instance.objectNode();
		response.put("success", success);

		return response;
	}
	
	@DeleteMapping(value="/{id}")
	@ResponseBody
	public void delete(@PathVariable("id") Integer id) 
	{
		this.service.delete(id);
	}
	
	@PostConstruct
	public void setUpMessagesFunctions() throws IOException, ServerNotRegisteredException, TimeoutException 
	{
		this.messagingService.on("list-cams", (messageTag, delivery) -> {			
			
			try 
			{
				List<CamModel> cams = this.service.list();
				
				ObjectMapper serializer = new ObjectMapper();
			
				return serializer.writeValueAsBytes(cams);
			
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		});
		
		this.messagingService.on("cam", (tag, delivery) -> {
			
			try 
			{
				
				ObjectMapper serializer = new ObjectMapper();
				
				JsonNode params = serializer.readTree( delivery.getBody() );
				int camId = params.get("id").asInt();
				CamModel cam = this.service.get(camId);
				
				Hibernate.initialize(cam);
				
				byte[] camBytes = serializer.writeValueAsBytes( cam );
				
				return camBytes;
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
			
		});
	}
	
}
