package edu.fateczl.mb;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import edu.fateczl.dao.IngressoDAO;
import edu.fateczl.model.Ingresso;
import edu.fateczl.util.JPAUtil;

@RequestScoped
@ManagedBean
public class IngressoMB {

	private Ingresso ingresso;
	private EntityManager em;
	private IngressoDAO<Ingresso> dao;
	
	public IngressoMB() {
		em = JPAUtil.criaEntityManager();
		dao = new IngressoDAO<Ingresso>(em);
		ingresso = new Ingresso();
	}
}
