package edu.fateczl.listeners;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import edu.fateczl.model.Usuario;

public class Access implements PhaseListener{

	private static final long serialVersionUID = -5944170037740462537L;

	public void afterPhase(PhaseEvent ev) {
		FacesContext fc = ev.getFacesContext();
		UIViewRoot view = fc.getViewRoot();
		String pagina = view.getViewId();
		System.out.println("test Access");
		System.out.println(pagina);
		if (!pagina.equals("/index.xhtml") && !pagina.equals("/event.xhtml") && !pagina.equals("/404.xhtml")) {
			System.out.println("entrou e tentou Access");
			Application app = fc.getApplication();
			Usuario usuario = app.evaluateExpressionGet(fc, "#{usuarioMB.usuarioLogado}", Usuario.class);
			
		    if (usuario == null) { 
		    	System.out.println("Usuário nulo Access");
		    	NavigationHandler nav = app.getNavigationHandler();
		    	nav.handleNavigation(fc, "", "index");
		    	try {
					fc.getExternalContext().redirect("index.xhtml");
					System.out.println("external context");
				} catch (IOException e) {
					System.out.println("falha no engano");
					fc.renderResponse();
				}
		    	
		    }
		}
		
	}

	public void beforePhase(PhaseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public PhaseId getPhaseId() {
		// TODO Auto-generated method stub
		return PhaseId.RESTORE_VIEW;
	}

}
