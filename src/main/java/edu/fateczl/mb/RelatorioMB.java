
package edu.fateczl.mb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

import edu.fateczl.dao.EventoDAO;
import edu.fateczl.model.Evento;
import edu.fateczl.util.JPAUtil;

@ManagedBean
@SessionScoped
public class RelatorioMB {
	private Evento evento;
	private EntityManager em = JPAUtil.criaEntityManager();
	private Map<String, List<Map<String, String>>> lista;
	private List<String> chaves;
	
	private Map<String, String> bars;
	private Map<String, String> totalArrecadado;
	
	public Evento getEvento() {
		return evento;
	}
	public void setEvento(Evento evento) {
		this.evento = evento;
	}
	
	public void carregaRelatorio(long idEvento) throws IOException
	{
		EventoDAO<Evento> dao = new EventoDAO<Evento>(em);
		
		em.getTransaction().begin();
		Evento evento = dao.find(Evento.class, idEvento);
		
		List<Map<String, String>> provList = dao.selecionaToRelatorio(evento);
		
		List<Map<String, String>> arrecadacaoTotal = dao.selecionaArrecadacaoTotal(evento);
		em.getTransaction().commit();
		
		chaves= new ArrayList<String>();
		
		lista = new HashMap<String, List<Map<String, String>>>();
		for(Map<String, String> dados : provList) {
			if(lista.get(dados.get("descricao")) != null) {
				lista.get(dados.get("descricao")).add(dados);
			} else {
				lista.put(dados.get("descricao"), new ArrayList<Map<String, String>>());
				lista.get(dados.get("descricao")).add(dados);
				chaves.add(dados.get("descricao"));
				
			}
		}
		
		bars = new HashMap<String, String>();
		totalArrecadado = new HashMap<String, String>();
		for(Map<String, String> arrecadacao : arrecadacaoTotal) {
			bars.put(arrecadacao.get("descricao"), arrecadacao.get("total"));
			totalArrecadado.put(arrecadacao.get("descricao"), arrecadacao.get("arrecadacao"));
		}
		
		FacesContext.getCurrentInstance().getExternalContext().redirect("relatorio.xhtml");
	}
	
	public LineChartModel getLineChart() 
	{
		LineChartModel model = new LineChartModel();
		
		if(lista == null)
			return model;
		
		double maiorValor = 0.0;
		Map<String, ChartSeries> mc = new HashMap<String, ChartSeries>();
		for(String s : chaves) {
			mc.put(s, new ChartSeries());
			mc.get(s).setLabel(s);
			List<Map<String, String>> data = lista.get(s);
			
			for(Map<String, String> std : data) {
				mc.get(s).set(std.get("dtCadastro").substring(0, 16), Double.parseDouble(std.get("arrecadacao")));
				
				if(Double.parseDouble(std.get("arrecadacao")) > maiorValor)
					maiorValor = Double.parseDouble(std.get("arrecadacao"));
			}
			
			model.addSeries(mc.get(s));
		}
 
        model.setTitle("Gráfico de Vendas");
        model.setLegendPosition("e");
        model.setShowPointLabels(true);
        model.getAxes().put(AxisType.X, new CategoryAxis("Data/Hora"));
        Axis yAxis = model.getAxis(AxisType.Y);
        yAxis.setLabel("Reais");
        yAxis.setMin(0);
        yAxis.setMax(maiorValor);
        
        return model;
	}
	
	public BarChartModel getBarChart() {
		BarChartModel model = new BarChartModel();
		
		int max = 0;
		for(String key : bars.keySet()) {
			ChartSeries ingressosVendidos = new ChartSeries();
			ingressosVendidos.setLabel(key); 
			
			String value = bars.get(key);
			ingressosVendidos.set("Ingressos", Integer.parseInt(value));
			max += Integer.parseInt(value);
			
			model.addSeries(ingressosVendidos);
		}
		
        
        
        model.setTitle("Ingressos Vendidos");
        model.setLegendPosition("ne");
         
        Axis xAxis = model.getAxis(AxisType.X);
        xAxis.setLabel("Ticket");
         
        Axis yAxis = model.getAxis(AxisType.Y);
        yAxis.setLabel("Quantidade");
        yAxis.setMin(0);
        yAxis.setMax(max);
        
        return model;
	}
	
	public BarChartModel getBarChartArrecadacao() {
		BarChartModel model = new BarChartModel();
		
		double max = 0;
		for(String key : totalArrecadado.keySet()) {
			ChartSeries ingressosVendidos = new ChartSeries();
			ingressosVendidos.setLabel(key);
			
			String value = totalArrecadado.get(key);
			ingressosVendidos.set("Ingressos", Double.parseDouble(value));
			
			max += Double.parseDouble(value);
			model.addSeries(ingressosVendidos);
		}
        
        model.setTitle("Total Arrecada por Ingresso");
        model.setLegendPosition("ne");
         
        Axis xAxis = model.getAxis(AxisType.X);
        xAxis.setLabel("Ticket");
         
        Axis yAxis = model.getAxis(AxisType.Y);
        yAxis.setLabel("Valor R$");
        yAxis.setMin(0);
        yAxis.setMax(max);
        
        return model;
	}
	
}
