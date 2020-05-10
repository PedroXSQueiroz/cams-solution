package br.com.pedroxsqueiroz.camsresourceserver.dtos;

import br.com.pedroxsqueiroz.camsresourceserver.models.CamModel;
import lombok.Data;

@Data
public class StreamCam {

	private String id ;
	private CamModel cam;
	
}
