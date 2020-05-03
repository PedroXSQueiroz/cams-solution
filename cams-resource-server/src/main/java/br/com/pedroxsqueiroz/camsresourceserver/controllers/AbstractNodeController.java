package br.com.pedroxsqueiroz.camsresourceserver.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.pedroxsqueiroz.camsresourceserver.models.NodeModel;
import br.com.pedroxsqueiroz.camsresourceserver.services.AbstractNodeService;
import br.com.pedroxsqueiroz.camsresourceserver.services.AbstractService;

public class AbstractNodeController <T extends NodeModel> {

	@Autowired
	public AbstractNodeService<T> service;
	
	@GetMapping(value = "/")
	@ResponseBody()
	public List<T> list()
	{
		return this.service.list();
	}
	
	@GetMapping(value = "/{key}")
	@ResponseBody()
	public T getByKey(@PathVariable() String key) 
	{
		return this.service.getByKey(key);
	} 
	
	@PostMapping(value = "/")
	@ResponseBody()
	public T create(@RequestBody T node) 
	{
		return this.service.save(node);
	}
	
	
	@PutMapping(value = "/{id}")
	@ResponseBody()
	public T update(@RequestBody T node, @PathVariable Integer id) 
	{
		this.service.save(node);
		return node;
	}
	
	@DeleteMapping(value = "/{id}")
	@ResponseBody()
	public ResponseEntity delete(@PathVariable Integer id) 
	{
		this.service.delete(id);
		
		return new ResponseEntity(HttpStatus.OK);
	}
	
}
