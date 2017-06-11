package edu.fateczl.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="endereco")
public class Endereco {
	private long id;
	private Date dtCadastro;
	private Date dtAlteracao;
	private String cep;
	private String logradouro;
	private String bairro;
	private String cidade;
	private String uf;
	private String numero;
	private String complemento;
		
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
	@Column(length=8)
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	
	@Column(length=150)
	public String getLogradouro() {
		return logradouro;
	}
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}
	
	@Column(length=75)
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	
	@Column(length=75)
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	
	@Column(length=2)
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	
	@Column(length=5)
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	@Column(length=150)
	public String getComplemento() {
		return complemento;
	}
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getLogradouro() + ", ");
		sb.append(this.getNumero() + ", ");
		sb.append(this.getBairro() + ", ");
		sb.append(this.getCidade() + "-");
		sb.append(this.getUf() + ", CEP:");
		sb.append(this.getCep());
		
		return sb.toString();
	}
	
}
