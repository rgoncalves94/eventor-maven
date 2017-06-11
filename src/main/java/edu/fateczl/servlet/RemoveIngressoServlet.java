package edu.fateczl.servlet;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.fateczl.dao.IngressoDAO;
import edu.fateczl.mb.EventoMB;
import edu.fateczl.model.Ingresso;
import edu.fateczl.util.JPAUtil;

@WebServlet("/evento/remove")
public class RemoveIngressoServlet extends HttpServlet {
	
	private EntityManager em;
	
	public RemoveIngressoServlet() {
		em = JPAUtil.criaEntityManager();
	}
	
	@Override
	public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String ref = req.getParameter("ref");
		
		IngressoDAO<Ingresso> dao = new IngressoDAO<Ingresso>(em);
		
		em.getTransaction().begin();
		
		Ingresso ingresso = dao.find(Ingresso.class, Long.parseLong(ref));
		
		dao.remove(ingresso);
		
		em.getTransaction().commit();
		
		EventoMB mbean = (EventoMB) req.getSession().getAttribute("eventoMB");
		
		for(int i =0; i < mbean.getEvento().getIngressos().size(); i++) {
			Ingresso atual = mbean.getEvento().getIngressos().get(i);
			if(atual.getId() == ingresso.getId()) { 
				mbean.getEvento().getIngressos().remove(i);
			}
		}
		
		resp.getWriter().append("{\"success\":\"Ingresso removido com sucesso.\"}").flush();
		return ;
	}
}
