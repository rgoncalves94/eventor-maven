package edu.fateczl.mb;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import edu.fateczl.dao.EventoDAO;
import edu.fateczl.dao.IngressoDAO;
import edu.fateczl.dao.VendaDAO;
import edu.fateczl.model.Evento;
import edu.fateczl.model.Ingresso;
import edu.fateczl.model.Venda;
import edu.fateczl.util.JPAUtil;

@ManagedBean
@SessionScoped
public class EventoMB {
	
	private Evento evento;
	private EventoDAO<Evento> dao;
	private IngressoDAO<Ingresso> ingressoDAO;
	private EntityManager em = JPAUtil.criaEntityManager();
	private Ingresso ingresso;
	
	
	public EventoMB() {
		evento = new Evento();
		dao = new EventoDAO<Evento>(em);
		ingresso = new Ingresso();
		ingressoDAO = new IngressoDAO<Ingresso>(em);
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}
	
	public Ingresso getIngresso() {
		return ingresso;
	}

	public void setIngresso(Ingresso ingresso) {
		this.ingresso = ingresso;
	}

	public void novo() throws IOException
	{
		evento = new Evento();
		FacesContext.getCurrentInstance().getExternalContext().redirect("novo-evento.xhtml");
	}
	
	public String totalEventos() {
		Map<String, Object> session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		
		UsuarioMB umb = (UsuarioMB) session.get("usuarioMB");
		System.out.println(umb.getUsuarioLogado().getUsername());
		long contaEventosPorUsuario = dao.contaEventosPorUsuario(umb.getUsuarioLogado());
		
		return String.valueOf(contaEventosPorUsuario);
	}
	
	public List<Evento> listaEventos() {
		Map<String, Object> session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		
		UsuarioMB umb = (UsuarioMB) session.get("usuarioMB");
		Evento eventoSelect = new Evento();
		eventoSelect.setDono(umb.getUsuarioLogado());
		List<Evento> contaEventosPorUsuario = dao.selecionaEventosByUsuario(eventoSelect);
		
		return contaEventosPorUsuario;
	}
	
	public List<Evento> todosEventos() {
		List<Evento> contaEventosPorUsuario = dao.selecionaTodos();
		
		return contaEventosPorUsuario;
	}
	
	public void edita(long id) throws IOException
	{
		evento = dao.find(Evento.class, id);
		System.out.println(evento.toString());
		FacesContext.getCurrentInstance().getExternalContext().redirect("novo-evento.xhtml");
	}
	
	public void relatorio(long id)
	{
	}
			
	
	public void salva() throws IOException
	{
		Map<String, Object> session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		UsuarioMB umb = (UsuarioMB) session.get("usuarioMB");
		evento.setDono(umb.getUsuarioLogado());
		evento.setDtCadastro(new Date());
		
		//Tratamento
		evento.getLocalizacao().setCep(evento.getLocalizacao().getCep().replace("-", ""));
		//Fim - Tratamento
		if(evento.getId() > 0) {
			em.getTransaction().begin();
			Evento efinded = dao.find(Evento.class, evento.getId());
			
			efinded.setDtAlteracao(new Date());
			efinded.setTitulo(evento.getTitulo());
			efinded.setDescricao(evento.getDescricao());
			efinded.setNomeOrganizador(evento.getNomeOrganizador());
			efinded.setInicio(evento.getInicio());
			efinded.setTermino(evento.getTermino());
			efinded.setTermino(evento.getTermino());
			efinded.setLocalizacao(evento.getLocalizacao());
			
			em.getTransaction().commit();
			
			if((ingresso.getDescricao() != null || ingresso.getDescricao() != "")
				&& ingresso.getValor() > 0.0) {
				ingresso.setEvento(efinded);
				ingressoDAO.persist(ingresso);
				
				evento.getIngressos().add(ingresso);
			}
			ingresso = new Ingresso();
			
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage msg = 
					new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Evento alterado", 
					"Evento alterado com sucesso");
			fc.addMessage(null, msg);
			
			return ;
		}
		
		dao.persist(evento);
		
		if((ingresso.getDescricao() != null || ingresso.getDescricao() != "")
			&& ingresso.getValor() > 0.0) {
			ingresso.setEvento(evento);
			ingressoDAO.persist(ingresso);
			evento.getIngressos().add(ingresso);
		}
		ingresso = new Ingresso();
		
		FacesContext fc = FacesContext.getCurrentInstance();
		FacesMessage msg = 
				new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Evento adicionado", 
				"Evento adicionado com sucesso");
		fc.addMessage(null, msg);
		
		return ;
	}
			
	public void removeTicket(long idTicket)
	{
		em.getTransaction().begin();
		Ingresso finded = ingressoDAO.find(Ingresso.class, idTicket);
		System.out.println(finded.getId());
		em.remove(finded);
		em.flush();
		em.getTransaction().commit();
		System.out.println("removido");
	}
	
	public void visualiza(long id) throws IOException
	{
		evento = dao.find(Evento.class, id);
		FacesContext.getCurrentInstance().getExternalContext().redirect("event.xhtml");
	}
	
	//Calendario
	
	public int getDiaInicio() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(evento.getInicio());
		int day = cal.get(Calendar.DAY_OF_MONTH);
		return day;
	}
	
	public String getMesInicio() {
        String month = "wrong";
        Calendar cal = Calendar.getInstance();
		cal.setTime(evento.getInicio());
		int num = cal.get(Calendar.MONTH);
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month.substring(0, 3);
    }
	
	public void registraVenda(long ticket) throws IOException
	{
		Ingresso ingresso = ingressoDAO.find(Ingresso.class, ticket);
		
		Venda venda = new Venda();
		venda.setDtCadastro(new Date());
		venda.setIngresso(ingresso);
		VendaDAO<Venda> vendaDAO = new VendaDAO<Venda>(em);
		vendaDAO.persist(venda);
		
		FacesContext fc = FacesContext.getCurrentInstance();
		FacesMessage msg = 
				new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Compra efetuada", 
				"O ingresso foi vendido com sucesso.");
		fc.addMessage(null, msg);
		
		FacesContext.getCurrentInstance().getExternalContext().redirect("event.xhtml");
	}
}
