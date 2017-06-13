package edu.fateczl.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="venda")
public class Venda {
	private long id;
	private Date dtCadastro;
	private Ingresso ingresso;
	private Participante participante;
	
	public Venda() {
		ingresso = new Ingresso();
		participante = new Participante();
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
	
	@ManyToOne(targetEntity=Ingresso.class, cascade={CascadeType.ALL})
	public Ingresso getIngresso() {
		return ingresso;
	}
	
	public void setIngresso(Ingresso ingresso) {
		this.ingresso = ingresso;
	}
	
	@ManyToOne(targetEntity=Participante.class)
	public Participante getParticipante() {
		return participante;
	}
	public void setParticipante(Participante participante) {
		this.participante = participante;
	}
	
}
