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

@WebServlet("/excel")
public class ExcelServlet extends HttpServlet {
	
	private EntityManager em;
	
	public ExcelServlet() {
		em = JPAUtil.criaEntityManager();
	}
	
	@Override
	public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setHeader("Content-Disposition",      
                "attachment; filename=lista.xls");
		
		resp.getWriter().append(req.getParameter("excelExport")).flush();
		return ;
	}
}
