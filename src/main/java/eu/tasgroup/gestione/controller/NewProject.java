package eu.tasgroup.gestione.controller;

import java.io.IOException;
import java.util.Date;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.facade.ProjectManagerFacade;
import eu.tasgroup.gestione.businesscomponent.model.Project;
import eu.tasgroup.gestione.businesscomponent.security.EscapeHTML;

/**
 * Servlet implementation class NewProject
 */
@WebServlet("/projectManager/insertProject")
public class NewProject extends HttpServlet {
	private static final long serialVersionUID = 6506827356648211374L;

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
			String nome = EscapeHTML.escapeHtml(request.getParameter("nome"));
			String descrizione = EscapeHTML.escapeHtml(request.getParameter("descrizione"));
			
			//TODO: LE DATE
			Date dataInizio = new Date();
			Date dataFine = new Date();
			
			Double budget = Double.parseDouble(EscapeHTML.escapeHtml(request.getParameter("budget")));
			
			Long idCliente = Long.parseLong(EscapeHTML.escapeHtml(request.getParameter("cliente")));
			Long idResponsabile = Long.parseLong(EscapeHTML.escapeHtml(request.getParameter("responsabile")));
			Double costo = Double.parseDouble(EscapeHTML.escapeHtml(request.getParameter("costo")));
			
			Project project = new Project();
			project.setNomeProgetto(nome);
			project.setDescrizione(descrizione);
			project.setDataInizio(dataInizio);
			project.setDataFine(dataFine);
			project.setBudget(budget);
			project.setIdCliente(idCliente);
			project.setIdResponsabile(idResponsabile);
			project.setCostoProgetto(costo);
			
			pmf.createOrUpdateProject(project);
			
			response.sendRedirect("pm-projects.jsp");
		}catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
	}

}
