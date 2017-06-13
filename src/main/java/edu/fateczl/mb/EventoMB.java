package edu.fateczl.mb;

import java.io.ByteArrayInputStream;
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

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import edu.fateczl.dao.EventoDAO;
import edu.fateczl.dao.IngressoDAO;
import edu.fateczl.dao.ParticipanteDAO;
import edu.fateczl.dao.VendaDAO;
import edu.fateczl.model.Evento;
import edu.fateczl.model.Ingresso;
import edu.fateczl.model.Participante;
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
	private Venda venda;
	
	private List<Participante> participantes;

	private UploadedFile banner;

	public EventoMB() {
		evento = new Evento();
		dao = new EventoDAO<Evento>(em);
		ingresso = new Ingresso();
		ingressoDAO = new IngressoDAO<Ingresso>(em);
		venda = new Venda();
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

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}


	public UploadedFile getBanner() {
		return banner;
	}

	public void setBanner(UploadedFile banner) {
		this.banner = banner;
	}
	
	public List<Participante> getParticipantes() {
		return participantes;
	}

	public void setParticipantes(List<Participante> participantes) {
		this.participantes = participantes;
	}

	public StreamedContent bannerToImage() {
		if(evento.getBanner() == null)
			return null;
        return new DefaultStreamedContent(new ByteArrayInputStream(evento.getBanner()), "image/jpg");
	}
	
	public StreamedContent bannerToImage(long id) {
		Evento e = dao.find(Evento.class, id);
		if(e == null)
			return null;
		if(e.getBanner() == null)
			return null;
        return new DefaultStreamedContent(new ByteArrayInputStream(evento.getBanner()), "image/jpg");
	}
	
	public void novo() throws IOException {
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

	public void edita(long id) throws IOException {
		evento = dao.find(Evento.class, id);
		System.out.println(evento.toString());
		FacesContext.getCurrentInstance().getExternalContext().redirect("novo-evento.xhtml");
	}

	public void salva() throws IOException {
		Map<String, Object> session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		UsuarioMB umb = (UsuarioMB) session.get("usuarioMB");
		evento.setDono(umb.getUsuarioLogado());
		evento.setDtCadastro(new Date());
		
		if(banner.getSize() > 0)
			evento.setBanner(banner.getContents());

		// Tratamento
		evento.getLocalizacao().setCep(evento.getLocalizacao().getCep().replace("-", ""));
		// Fim - Tratamento
		if (evento.getId() > 0) {
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

			if ((ingresso.getDescricao() != null || ingresso.getDescricao() != "") && ingresso.getValor() > 0.0) {
				ingresso.setEvento(efinded);
				ingressoDAO.persist(ingresso);

				evento.getIngressos().add(ingresso);
			}

			em.getTransaction().commit();

			ingresso = new Ingresso();

			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Evento alterado",
					"Evento alterado com sucesso");
			fc.addMessage(null, msg);

			return;
		}

		em.getTransaction().begin();

		dao.persist(evento);

		if ((ingresso.getDescricao() != null || ingresso.getDescricao() != "") && ingresso.getValor() > 0.0) {
			ingresso.setEvento(evento);
			ingressoDAO.persist(ingresso);
			evento.getIngressos().add(ingresso);
		}

		em.getTransaction().commit();

		ingresso = new Ingresso();

		FacesContext fc = FacesContext.getCurrentInstance();
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Evento adicionado",
				"Evento adicionado com sucesso");
		fc.addMessage(null, msg);

		return;
	}

	public void removeIngresso(long idTicket) {
		try {
			em.getTransaction().begin();
			Ingresso finded = ingressoDAO.find(Ingresso.class, idTicket);
			System.out.println(finded.getId() + " Toppa");
			ingressoDAO.remove(finded);
			em.getTransaction().commit();
			System.out.println("removido");
		} catch (Exception e) {
			em.getTransaction().rollback();
			System.out.println("rolling back");
		}
	}

	public void visualiza(long id) throws IOException {
		evento = dao.find(Evento.class, id);
		System.out.println(evento.toString());
		FacesContext.getCurrentInstance().getExternalContext().redirect("event.xhtml");
	}

	public String registraVenda() throws IOException {
		try {
			venda.setDtCadastro(new Date());
			VendaDAO<Venda> vendaDAO = new VendaDAO<Venda>(em);
			ParticipanteDAO<Participante> participanteDAO = new ParticipanteDAO<Participante>(em);
			em.getTransaction().begin();
			venda.setIngresso(ingressoDAO.find(Ingresso.class, venda.getIngresso().getId()));
			participanteDAO.persist(venda.getParticipante());
			vendaDAO.persist(venda);
			em.getTransaction().commit();

			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Compra efetuada",
					"O ingresso foi vendido com sucesso.");
			fc.addMessage(null, msg);

			venda = new Venda();

		} catch (Exception e) {
			em.getTransaction().rollback();
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao registrar venda",
					"O ingresso não foi vendido, tente novamente.");
			fc.addMessage(null, msg);
		}
		return "event.xhtml";// FacesContext.getCurrentInstance().getExternalContext().redirect("event.xhtml");
	}

	// Calendario

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
		if (num >= 0 && num <= 11) {
			month = months[num];
		}
		return month.substring(0, 3);
	}
	
	public void carregaClientes(long idEvento) throws IOException {
		Evento ev = new Evento();
		ev.setId(idEvento);
		em.getTransaction().begin();
		
		participantes = dao.carregaClientesPorEvento(ev);
		em.getTransaction().commit();
		
		FacesContext.getCurrentInstance().getExternalContext().redirect("lista.xhtml");
	}
}
