package eu.tasgroup.gestione.controller;

import java.io.IOException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.facade.ProjectManagerFacade;
import eu.tasgroup.gestione.businesscomponent.security.EscapeHTML;

/**
 * Servlet implementation class NewProject
 */
@WebServlet("/projectManager/rifiutaTimesheet")
public class RifiutaTimesheet extends HttpServlet {
	private static final long serialVersionUID = 8357089588223110045L;
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
			
			Long id = Long.parseLong(EscapeHTML.escapeHtml(request.getParameter("id")));
			pmf.approvaTimesheet(id, false);
			
			response.sendRedirect("pm-timesheets.jsp");
		}catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
	}

}
