package edu.fateczl.servlet;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.fateczl.dao.EventoDAO;
import edu.fateczl.model.Evento;
import edu.fateczl.model.Usuario;
import edu.fateczl.util.JPAUtil;

@WebServlet("/evento/edit")
public class EventoServlet extends HttpServlet {
	
	private EntityManager em;
	
	public EventoServlet() {
		em = JPAUtil.criaEntityManager();
	}
	
	@Override
	public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		EventoDAO<Evento> dao = new EventoDAO<Evento>(em);
		
		req.getRequestDispatcher("/WEB-INF/novo-evento.xhtml").forward(req, resp);
	}
}
