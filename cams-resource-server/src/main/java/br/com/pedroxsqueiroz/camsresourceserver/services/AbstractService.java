package br.com.pedroxsqueiroz.camsresourceserver.services;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import lombok.extern.java.Log;

@Log
public class AbstractService<T> {
	
//	@PersistenceContext
//	EntityManager entityManager;
	
	@Autowired
	protected JpaRepository<T, Integer> dao;
	
	protected JpaRepository<T, Integer> getDao()
	{	
		
//		if(this.dao == null) 
//		{
//			Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass()
//					.getGenericSuperclass()).getActualTypeArguments()[0];
//			
//			SimpleJpaRepository<T, Integer> dao = new SimpleJpaRepository<T, Integer>(entityClass, entityManager);
//			
//			this.dao = dao;
//		}
		
		return this.dao;
	}
	
	public List<T> list()
	{
		return this.getDao().findAll();
	}
	
	public T get(Integer id) 
	{
		return this.getDao().findById(id).get();
	}
	
	@Transactional
	public void delete(Integer id) 
	{
		this.getDao().deleteById(id);
	}
	
	@Transactional
	public T save(T node) 
	{
		this.getDao().save(node);
		return node;
	}
	
}
