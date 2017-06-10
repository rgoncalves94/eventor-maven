package edu.fateczl.dao;

import javax.persistence.EntityManager;

public class IngressoDAO<T> extends GenericDAOImpl<T> {

	public IngressoDAO(EntityManager em) {
		super(em);
	}
	
}
