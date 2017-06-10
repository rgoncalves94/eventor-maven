package edu.fateczl.mb;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import edu.fateczl.dao.UsuarioDAO;
import edu.fateczl.model.Usuario;
import edu.fateczl.util.JPAUtil;

@SessionScoped
@ManagedBean
public class UsuarioMB {
	
	private Usuario usuario = new Usuario();
	private Usuario usuarioLogado = null;
	private UsuarioDAO<Usuario> dao;
	private String tituloEvento;
	
	public UsuarioMB() {
		dao = new UsuarioDAO<Usuario>(JPAUtil.criaEntityManager());
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public String getTituloEvento() {
		return tituloEvento;
	}

	public void setTituloEvento(String tituloEvento) {
		this.tituloEvento = tituloEvento;
	}
	
	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public String criaConta() throws IOException {
		FacesContext fc = FacesContext.getCurrentInstance();
		if((!usuario.getUsername().equals("") && usuario.getUsername() != null)
			&& (!usuario.getSenha().equals("") && usuario.getSenha() != null)
			&& !dao.verificaNomeDeUsuarioExistente(usuario.getUsername()) 
				) {
			
			usuario.setAtivo(true);
			
			dao.persist(usuario);
			
			usuarioLogado = usuario.clone();
			FacesContext.getCurrentInstance().getExternalContext().redirect("home.xhtml");
			return "";
			//return "home.xhtml";
		} else {
			FacesMessage msg = 
					new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Login Invalido", 
					"Usuário ou senha inválido");
			fc.addMessage(null, msg);
		}
		usuarioLogado = null;
		return "index.html";
	}
	
	public String login() throws IOException {
		
		Usuario user = dao.selecionaPorUsuarioESenha(usuario.getUsername(), usuario.getSenha());

		if(user == null) {
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage msg = 
					new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Login Invalido", 
					"Usuário ou senha inválido");
			fc.addMessage(null, msg);
			
			usuarioLogado = null;
			return "index.xhtml";
		}
		
		usuarioLogado = user.clone();
		return "home.xhtml";
	}
	
	public String logout() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		usuarioLogado = null;
		FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
		return "index.xhtml";
	}
	
}
