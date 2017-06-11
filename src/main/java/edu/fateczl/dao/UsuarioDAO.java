package edu.fateczl.dao;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import edu.fateczl.model.Usuario;

public class UsuarioDAO<T> extends GenericDAOImpl<T>{

	public UsuarioDAO(EntityManager em) {
		super(em);
	}
	
	@SuppressWarnings("unchecked")
	public T selecionaPorUsuarioESenha(String username, String senha) {
		
		Query q = em.createQuery("FROM Usuario u WHERE u.username = :username AND u.senha = :senha");
		
		q.setParameter("username", username);
		q.setParameter("senha", senha);
		try {
			return (T) q.getSingleResult();
		} catch(Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public boolean verificaNomeDeUsuarioExistente(String username) {
		
		Query q = em.createQuery("FROM Usuario u WHERE u.username = :username");
		
		q.setParameter("username", username);
		try {
			return q.getSingleResult() == null;
		} catch(Exception e) {
			return false;
		}
	}

}
