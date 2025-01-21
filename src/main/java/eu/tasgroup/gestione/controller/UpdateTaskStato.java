package eu.tasgroup.gestione.controller;

import java.io.IOException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.enumerated.StatoTask;
import eu.tasgroup.gestione.businesscomponent.facade.DipendenteFacade;
import eu.tasgroup.gestione.businesscomponent.security.EscapeHTML;

/**
 * Servlet implementation class NewProject
 */
@WebServlet("/dipendente/updateStatoTask")
public class UpdateTaskStato extends HttpServlet {
	private static final long serialVersionUID = -951592250789737528L;
	private DipendenteFacade df;
	
	@Override
	public void init() throws ServletException {
		try {
			df = DipendenteFacade.getInstance();
		} catch (DAOException | NamingException e) {
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			System.out.println("WUT?");
			Long id = Long.parseLong(EscapeHTML.escapeHtml(request.getParameter("taskId")));
			StatoTask stato = StatoTask.valueOf(request.getParameter("statoTask").toUpperCase());
			if(stato.equals(StatoTask.DA_INIZIARE))
				stato=StatoTask.IN_PROGRESS;
			else if(stato.equals(StatoTask.IN_PROGRESS))
				stato=StatoTask.COMPLETATO;
			
			df.updateProjectTaskStato(stato, id);
			
			response.sendRedirect("dip-tasks.jsp");
		}catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
	}

}
