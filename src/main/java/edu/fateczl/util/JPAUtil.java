package edu.fateczl.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {

	private static EntityManagerFactory factory = Persistence.createEntityManagerFactory("eventor");
	
	public static EntityManager criaEntityManager() {
		return factory.createEntityManager();
	}
}
