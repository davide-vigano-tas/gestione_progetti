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
import eu.tasgroup.gestione.businesscomponent.facade.DipendenteFacade;
import eu.tasgroup.gestione.businesscomponent.model.Timesheet;
import eu.tasgroup.gestione.businesscomponent.security.EscapeHTML;

/**
 * Servlet implementation class NewProject
 */
@WebServlet("/dipendente/insertTimesheet")
public class InsertTimesheets extends HttpServlet {
	private static final long serialVersionUID = 6506827356648211374L;

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
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			
			Long idDipendente = Long.parseLong(EscapeHTML.escapeHtml(request.getParameter("dipendente")));
			Long idTask = Long.parseLong(EscapeHTML.escapeHtml(request.getParameter("task")));
			Long idProgetto = df.getProjectTaskById(idTask).getIdProgetto();
			Double ore = Double.parseDouble(request.getParameter("ore"));
			Date data = formato.parse(request.getParameter("data"));
			
			Timesheet timesheets = new Timesheet();
			timesheets.setIdDipendente(idDipendente);
			timesheets.setIdProgetto(idProgetto);
			timesheets.setIdTask(idTask);
			timesheets.setOreLavorate(ore);
			timesheets.setData(data);
			
			df.createOrUpdateTimesheet(timesheets);
			
			response.sendRedirect("DipendenteTimesheets.jsp");
		}catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
	}

}
