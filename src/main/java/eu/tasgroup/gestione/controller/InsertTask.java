package eu.tasgroup.gestione.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.enumerated.Fase;
import eu.tasgroup.gestione.businesscomponent.facade.ProjectManagerFacade;
import eu.tasgroup.gestione.businesscomponent.model.ProjectTask;
import eu.tasgroup.gestione.businesscomponent.security.EscapeHTML;

/**
 * Servlet implementation class NewProject
 */
@WebServlet("/projectManager/insertTask")
public class InsertTask extends HttpServlet {
	private static final long serialVersionUID = -8051496007511743068L;
	private ProjectManagerFacade pmf;
	
	@Override
	public void init() throws ServletException {
		try {
			pmf = ProjectManagerFacade.getInstance();
		} catch (DAOException | NamingException e) {
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			
			Long idProgetto = Long.parseLong(EscapeHTML.escapeHtml(request.getParameter("progetto")));
			String nome = EscapeHTML.escapeHtml(request.getParameter("nome"));
			String descrizione = EscapeHTML.escapeHtml(request.getParameter("descrizione"));
			
			Long idDipendente = Long.parseLong(EscapeHTML.escapeHtml(request.getParameter("dipendente")));
			
			Date scadenza = formato.parse(request.getParameter("scadenza"));
			
			Fase fase = Fase.valueOf(request.getParameter("fase").toUpperCase());
			
			ProjectTask task = new ProjectTask();
			task.setIdProgetto(idProgetto);
			task.setNomeTask(nome);
			task.setDescrizione(descrizione);
			task.setIdDipendente(idDipendente);
			task.setScadenza(scadenza);
			task.setFase(fase);
			
			pmf.createOrUpdateProjectTask(task);
			
			response.sendRedirect("pm-tasks.jsp");
		}catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
	}

}
