package edu.fateczl.dao;

import javax.persistence.EntityManager;

public class GenericDAOImpl<T>  implements GenericDAO<T> {
	
	protected EntityManager em;
	
	public GenericDAOImpl(EntityManager em) {
		this.em = em;
	}

	public void persist(T object) {
		em.persist(object);
		em.flush();
	}

	public void refresh(T object) {
		em.refresh(object);
		em.flush();
	}
	
	public void merge(T object) {
		em.refresh(object);
		em.flush();
	}

	public void remove(T object) {
		em.remove(object);
		em.flush();
	}

	public T find(Class<T> clazz, long id) {
		return em.find(clazz, id);
	}

}
