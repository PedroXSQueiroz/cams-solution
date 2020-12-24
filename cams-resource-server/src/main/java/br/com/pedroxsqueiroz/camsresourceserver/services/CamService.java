package br.com.pedroxsqueiroz.camsresourceserver.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import br.com.pedroxsqueiroz.camsresourceserver.config.NodeConfig;
import br.com.pedroxsqueiroz.camsresourceserver.controllers.ClientController;
import br.com.pedroxsqueiroz.camsresourceserver.dtos.StreamCam;
import br.com.pedroxsqueiroz.camsresourceserver.exceptions.ServerNotRegisteredException;
import br.com.pedroxsqueiroz.camsresourceserver.models.CamModel;
import br.com.pedroxsqueiroz.camsresourceserver.models.ClientModel;
import br.com.pedroxsqueiroz.camsresourceserver.models.NodeModel;

@Service
public class CamService extends AbstractService<CamModel>{
	
	@Autowired
	private NodeConfig thisConfig;
	
	@Autowired
	private MessagingService messagingService;

	public CamModel getFromClient(ClientModel client, Integer camId) throws IOException, InterruptedException, ServerNotRegisteredException, TimeoutException {
		
		ObjectNode camFindParams = JsonNodeFactory.instance.objectNode();
		camFindParams.put("id", camId);
		
		return this.messagingService.send(client, "cam", camFindParams, CamModel.class);
	}

	public StreamCam startStream(ClientModel client, CamModel cam)
			throws IOException, InterruptedException, ServerNotRegisteredException, TimeoutException {
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ObjectNode streamParams = JsonNodeFactory.instance.objectNode();
		streamParams.put("server",  objectMapper.valueToTree(this.thisConfig));
		streamParams.put("cam",  objectMapper.valueToTree(cam));
		
		return messagingService.send(client, "start-stream", streamParams, StreamCam.class);
	}

	public boolean startRecordStream(ClientModel client, CamModel cam, String path) throws InterruptedException, TimeoutException, ServerNotRegisteredException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();

		ObjectNode messageParams = JsonNodeFactory.instance.objectNode();
		messageParams.put("camId", cam.getId());
		messageParams.put("recordPath", path);
		messageParams.put("start", true);

		return messagingService.send(client, "set-record-stream", messageParams, JsonNode.class).get("success").asBoolean();
	}

	public boolean stopRecordStream(ClientModel client, CamModel cam) throws InterruptedException, TimeoutException, ServerNotRegisteredException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();

		ObjectNode messageParams = JsonNodeFactory.instance.objectNode();
		messageParams.put("camId", cam.getId());
		messageParams.put("start", false);

		return messagingService.send(client, "set-record-stream", messageParams, JsonNode.class).get("success").asBoolean();
	}
	
	public List<CamModel> getCamsFromClient(ClientModel client) {
		
		
		try 
		{
			CamModel[] clientCams = this.messagingService.send(client, "list-cams", new byte[0], CamModel[].class);
			
			return CollectionUtils.arrayToList(clientCams);
			
		} catch (IOException | InterruptedException | ServerNotRegisteredException | TimeoutException e) {

			e.printStackTrace();

			List<CamModel> emptyCameras = new ArrayList<CamModel>();
			
			return emptyCameras;
		}
		
	}

	public CamModel update(Integer id, CamModel cam) {
		
		if(this.dao.existsById(id)) 
		{
			//estourar exceção
		}
		
		return this.dao.save(cam);
	}
	
	
}
