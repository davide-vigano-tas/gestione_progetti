package eu.tasgroup.gestione.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

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
import eu.tasgroup.gestione.businesscomponent.model.User;


@WebServlet("/insertTicket")
public class InserisciTicket extends HttpServlet {

	private static final long serialVersionUID = 1721184294886912432L;

	


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String titolo = request.getParameter("titolo");
		String descrizione = request.getParameter("descrizione");
		String username =(String) request.getSession().getAttribute("username");
		if(username == null) {
			response.sendRedirect("login.jsp");
			return;
		}
		try {
			User user = AdminFacade.getInstance().getByUsername(username);
			Role[] roles = AdminFacade.getInstance().getRolesByUsername(username);
			Ruoli ruolo = null;
			if(Arrays.asList(roles).stream().anyMatch(r->r.getRole().equals(Ruoli.DIPENDENTE)))
				ruolo = Ruoli.DIPENDENTE;
			else ruolo = Ruoli.PROJECT_MANAGER;
			if(titolo == null || descrizione == null) {
				if(ruolo.equals(Ruoli.DIPENDENTE)) 
						response.sendRedirect("../dipendente/ticket-form.jsp?error=invalid_values");
				else response.sendRedirect("../projectManager/ticket-form.jsp?error=invalid_values");
				return;
			}
			
			Ticket t = new Ticket();
			t.setTitle(titolo);
			t.setDescription(descrizione);
			t.setOpener(user.getId());
			t.setCreated_at(new Date());
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
