package eu.tasgroup.gestione.controller;

import java.io.IOException;
import java.util.Arrays;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli;
import eu.tasgroup.gestione.businesscomponent.facade.AdminFacade;
import eu.tasgroup.gestione.businesscomponent.facade.ProjectManagerFacade;
import eu.tasgroup.gestione.businesscomponent.model.Role;
import eu.tasgroup.gestione.businesscomponent.model.Ticket;


@WebServlet("/updateTicket")
public class UpdateTicket extends HttpServlet {


	private static final long serialVersionUID = -840276823061410616L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String titolo = request.getParameter("titolo");
		String descrizione = request.getParameter("descrizione");
		String id = request.getParameter("id");
		String username =(String) request.getSession().getAttribute("username");
		if(username == null) {
			response.sendRedirect("login.jsp");
			return;
		}
		try {
			Role[] roles = AdminFacade.getInstance().getRolesByUsername(username);
			Ruoli ruolo = null;
			if(Arrays.asList(roles).stream().anyMatch(r->r.getRole().equals(Ruoli.DIPENDENTE)))
				ruolo = Ruoli.DIPENDENTE;
			else ruolo = Ruoli.PROJECT_MANAGER;
			if(titolo == null || descrizione == null || id == null) {
				if(ruolo.equals(Ruoli.DIPENDENTE)) 
						response.sendRedirect("../dipendente/dip-tickets.jsp?error=invalid_values");
				else response.sendRedirect("../projectManager/dip-tickets.jsp?error=invalid_values");
				return;
			}
			
			Ticket t = ProjectManagerFacade.getInstance().getTicketById(Long.parseLong(id));
			t.setTitle(titolo);
			t.setDescription(descrizione);
			ProjectManagerFacade.getInstance().createorUpdateTicket(t);
			if(ruolo.equals(Ruoli.DIPENDENTE)) 
				response.sendRedirect("dipendente/dip-tickets.jsp");
			else response.sendRedirect("projectManager/pm-tickets.jsp");
			
		} catch (DAOException | NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
		
		
		
		
		
	}

}
