package edu.fateczl.dao;

import javax.persistence.EntityManager;

public class VendaDAO<T> extends GenericDAOImpl<T>{

	public VendaDAO(EntityManager em) {
		super(em);
	}

	
}
