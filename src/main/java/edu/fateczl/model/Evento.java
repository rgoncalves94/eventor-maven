package edu.fateczl.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name="evento")
@Entity
public class Evento {
	
	private long id;
	private Date dtCadastro;
	private Date dtAlteracao;
	private String titulo;
	private Endereco localizacao;
	private Date inicio;
	private Date termino;
	private String descricao;
	private List<Ingresso> ingressos;
	private String nomeOrganizador;
	private Usuario dono;
	
	public Evento() {
		localizacao = new Endereco();
		ingressos = new ArrayList<Ingresso>();
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@Column(columnDefinition="timestamp")
	public Date getDtCadastro() {
		return dtCadastro;
	}
	public void setDtCadastro(Date dtCadastro) {
		this.dtCadastro = dtCadastro;
	}
	
	@Column(columnDefinition="timestamp")
	public Date getDtAlteracao() {
		return dtAlteracao;
	}
	public void setDtAlteracao(Date dtAlteracao) {
		this.dtAlteracao = dtAlteracao;
	}
	
	@Column(length=100)
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	@ManyToOne(targetEntity=Endereco.class, cascade=CascadeType.ALL)
	public Endereco getLocalizacao() {
		return localizacao;
	}
	public void setLocalizacao(Endereco localizacao) {
		this.localizacao = localizacao;
	}
	
	@Column(columnDefinition="timestamp")
	public Date getInicio() {
		return inicio;
	}
	
	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}
	
	@Column(columnDefinition="timestamp")
	public Date getTermino() {
		return termino;
	}
	public void setTermino(Date termino) {
		this.termino = termino;
	}
	
	@Column(columnDefinition="text")
	public String getDescricao() {
		return descricao;
	}
	
	@Column(columnDefinition="text")
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@OneToMany(targetEntity=Ingresso.class, fetch=FetchType.EAGER)
	@JoinColumn(name="evento_id")
	public List<Ingresso> getIngressos() {
		return ingressos;
	}
	public void setIngressos(List<Ingresso> ingressos) {
		this.ingressos = ingressos;
	}
	
	@ManyToOne(targetEntity=Usuario.class)
	public Usuario getDono() {
		return dono;
	}
	public void setDono(Usuario dono) {
		this.dono = dono;
	}
	
	public String getNomeOrganizador() {
		return nomeOrganizador;
	}

	public void setNomeOrganizador(String nomeOrganizador) {
		this.nomeOrganizador = nomeOrganizador;
	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("id:" + String.valueOf(id) + "\n");
		sb.append("dtCadastro:" + String.valueOf(dtCadastro) + "\n");
		sb.append("dtAlteracao:" + String.valueOf(dtAlteracao) + "\n");
		sb.append("titulo:" + titulo + "\n");
		sb.append("endereco.id:" + String.valueOf(localizacao.getId()) + "\n");
		sb.append("endereco.cep:" + localizacao.getCep() + "\n");
		sb.append("endereco.logradouro:" + localizacao.getLogradouro() + "\n");
		sb.append("endereco.bairro:" + localizacao.getBairro() + "\n");
		sb.append("endereco.cidade:" + localizacao.getCidade() + "\n");
		sb.append("endereco.uf:" + localizacao.getUf() + "\n");
		sb.append("endereco.numero:" + localizacao.getNumero() + "\n");
		sb.append("endereco.complemento:" + localizacao.getComplemento() + "\n");
		sb.append("inicio:" + inicio + "\n");
		sb.append("termino:" + termino + "\n");
		sb.append("descricao:" + descricao + "\n");
		sb.append("usuario.id:" + String.valueOf(dono.getId()) + "\n");
		sb.append("usuario.nome:" + dono.getUsername() + "\n");
		
		return sb.toString();
	}
	
}
