package eu.tasgroup.gestione.controller;

import java.io.IOException;
import java.util.Arrays;

import javax.mail.MessagingException;
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
import eu.tasgroup.gestione.businesscomponent.utility.EmailUtil;


@WebServlet("/admin/closeTicket")
public class CloseTicket extends HttpServlet {


	private static final long serialVersionUID = -840276823061410616L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String spiegazione = request.getParameter("spiegazione");
		String id = request.getParameter("id");
		String username =(String) request.getSession().getAttribute("username");
		if(username == null) {
			response.sendRedirect("login.jsp");
			return;
		}
		try {
			Ticket ticket = AdminFacade.getInstance().getTicketById(Long.parseLong(id));
			User utente = AdminFacade.getInstance().getUserById(ticket.getOpener());
			AdminFacade.getInstance().closeTicket(ticket.getId());
			
        	String emailContent = "<!DOCTYPE html><html lang=\"en\">"
                    + "<head><meta charset=\"UTF-8\"></head>"
                    + "<body>"
                    + "<div style='background-color:#f4f4f4;padding:20px;'>"
                    + "<div style='max-width:600px;margin:0 auto;background:#ffffff;padding:20px;border-radius:8px;'>"
                    + "<h1 style='text-align:center;color:#007bff;'>Ticket "+ticket.getId()+" chiuso</h1>"
                    + "<p style='text-align:center;'>Spiegazione:</p>"
                    + "<h4 style='text-align:center;'>"
                    + spiegazione
                    + "</h4>"
                    + "</div></div></body></html>";
        	EmailUtil.sendEmail(utente.getEmail(), "Chiusura ticket", emailContent);
        	response.sendRedirect("../admin/tickets.jsp");
			
		} catch (DAOException | NamingException | MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
		
		
		
		
		
	}

}
