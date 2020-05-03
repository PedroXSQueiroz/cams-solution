package br.com.pedroxsqueiroz.camsresourceserver.models;

import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "client")
@Entity
public class ClientModel extends NodeModel{

	public ClientModel()
	{
		super();
	}
	
	public ClientModel(NodeModel config )
	{
		super(config);
	}
	
}
