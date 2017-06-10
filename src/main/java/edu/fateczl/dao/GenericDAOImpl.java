package edu.fateczl.dao;

import javax.persistence.EntityManager;

public class GenericDAOImpl<T>  implements GenericDAO<T> {
	
	protected EntityManager em;
	
	public GenericDAOImpl(EntityManager em) {
		this.em = em;
	}

	public void persist(T object) {
		try {
			em.getTransaction().begin();
			em.persist(object);
			em.flush();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
		}
	}

	public void refresh(T object) {
		try {
			em.getTransaction().begin();
			em.refresh(object);
			em.flush();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
		}
	}
	
	public void merge(T object) {
		try {
			em.getTransaction().begin();
			em.refresh(object);
			em.flush();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
		}
	}

	public void remove(T object) {
		try {
			em.getTransaction().begin();
			em.remove(object);
			em.flush();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
		}
	}

	public T find(Class<T> clazz, long id) {
		return em.find(clazz, id);
	}

}
