package edu.fateczl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import edu.fateczl.model.Evento;
import edu.fateczl.model.Usuario;

public class EventoDAO<T> extends GenericDAOImpl<T>{

	public EventoDAO(EntityManager em) {
		super(em);
	}
	
	@SuppressWarnings("unchecked")
	public List<Evento> selecionaEventosByUsuario(Evento e) {
		
		Query q = em.createQuery("SELECT e FROM Evento e WHERE e.dono = :usuario");
		
		q.setParameter("usuario", e.getDono());
		
		return (List<Evento>) q.getResultList();
	}
	
	public long contaEventosPorUsuario(Usuario e) {
		Query q = em.createQuery("SELECT count(e.id) FROM Evento e WHERE e.dono = :usuario");
		q.setParameter("usuario", e);
		
		return (long) q.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Evento> selecionaTodos() {
		
		Query q = em.createQuery("SELECT e FROM Evento e");
		
		return (List<Evento>) q.getResultList();
	}
	
	public List<Map<String, String>> selecionaToRelatorio(Evento e) {
		try {
			Connection c = JDBCUtil.getConnection();
			
			PreparedStatement stmt = c.prepareStatement("SELECT count(i.id) total, sum(i.valor) arrecadacao, i.descricao, i.valor, v.dtCadastro FROM ingresso i"+
					" INNER JOIN venda v ON v.ingresso_id = i.id" +
					" WHERE i.evento_id = ?" +
					" GROUP BY i.id, date(v.dtCadastro), minute(v.dtCadastro)");
			
			stmt.setLong(1, e.getId());
			
			stmt.executeQuery();
			
			ResultSet resultSet = stmt.getResultSet();
			
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			while(resultSet.next()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("total", resultSet.getString("total"));
				map.put("arrecadacao", resultSet.getString("arrecadacao"));
				map.put("descricao", resultSet.getString("descricao"));
				map.put("valor", resultSet.getString("valor"));
				map.put("dtCadastro", resultSet.getString("dtCadastro"));
				
				list.add(map);
			}
			
			return list;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return null;
	}

}
