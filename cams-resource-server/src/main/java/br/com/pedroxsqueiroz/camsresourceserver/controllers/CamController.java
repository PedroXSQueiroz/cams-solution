package br.com.pedroxsqueiroz.camsresourceserver.controllers;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
