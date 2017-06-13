package edu.fateczl.dao;

import javax.persistence.EntityManager;

public class ParticipanteDAO<T> extends GenericDAOImpl<T> {
	
	public ParticipanteDAO(EntityManager em) {
		super(em);
	}
	
}
