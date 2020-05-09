package br.com.pedroxsqueiroz.camsresourceserver.controllers;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import br.com.pedroxsqueiroz.camsresourceserver.config.NodeConfig;
import br.com.pedroxsqueiroz.camsresourceserver.dtos.StreamCam;
import br.com.pedroxsqueiroz.camsresourceserver.exceptions.ServerNotRegisteredException;
import br.com.pedroxsqueiroz.camsresourceserver.models.CamModel;
import br.com.pedroxsqueiroz.camsresourceserver.models.ClientModel;
import br.com.pedroxsqueiroz.camsresourceserver.services.CamService;
import br.com.pedroxsqueiroz.camsresourceserver.services.ClientService;
import br.com.pedroxsqueiroz.camsresourceserver.services.MessagingService;

@Controller
@RequestMapping(value = "clients")
@CrossOrigin("*")
public class ClientController extends AbstractNodeController<ClientModel> {

	@Autowired
	public NodeConfig thisConfig;

	@Autowired
	public MessagingService messagingService;

	@Autowired
	private CamService camService;

	@GetMapping(value = "cams")
	@ResponseBody
	public List<CamModel> allClientCams() {

		ClientService clientService = (ClientService) this.service;

		return clientService.allClientCams();

	}

	@GetMapping(value = "{id}/cams")
	@ResponseBody
	public List<CamModel> listCamsFromClient(@PathVariable("id") Integer id) {
		ClientModel client = this.service.get(id);
		List<CamModel> cams = this.camService.getCamsFromClient(client);
		return cams;
	}

	@PostMapping(value = "{id}/stream/{camId}")
	@ResponseBody
	public StreamCam startStreamCam(@PathVariable("id") Integer clientId, @PathVariable("camId") Integer camId)
			throws IOException, InterruptedException, ServerNotRegisteredException, TimeoutException {

		ClientModel client = this.service.get(clientId);
		CamModel cam = this.camService.getFromClient(client, camId);

		return this.camService.startStream(client, cam);

	}

	@PostConstruct
	void setUpMessagesFunctions() throws IOException, ServerNotRegisteredException, TimeoutException {
		this.messagingService.on("delete/client", (messageTag, message) -> {

			try {
				
				ObjectMapper serializer = new ObjectMapper();
				ObjectNode simpleResponse = JsonNodeFactory.instance.objectNode();

				try {

					JsonNode messageBody = serializer.readTree(message.getBody());

					String clientKey = messageBody.get("key").asText();

					ClientModel byKey = this.getByKey(clientKey);
					this.service.delete(byKey);

					simpleResponse.put("success", true);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

					simpleResponse.put("success", false);
				}

				return serializer.writeValueAsBytes(simpleResponse);
			
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		});
	}

}
