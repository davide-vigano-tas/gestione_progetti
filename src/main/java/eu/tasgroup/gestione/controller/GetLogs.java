package eu.tasgroup.gestione.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.facade.AdminFacade;
import eu.tasgroup.gestione.businesscomponent.model.AuditLog;

@WebServlet("/admin/getlogs")
public class GetLogs extends HttpServlet {


	private static final long serialVersionUID = 7984219039114887705L;

	private AdminFacade af;

	@Override
	public void init() throws ServletException {
		try {
			af = AdminFacade.getInstance();
		} catch (DAOException | NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		  AuditLog[] nd = null;
		  SimpleDateFormat formato = null;
			try {
				nd = af.getAllAuditLogs();
				formato = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
				 
			}catch (DAOException | NamingException e) {
				e.printStackTrace();
				throw new ServletException("Errore durante l'esecuzione della query: "+e.getMessage());
			}
		
		
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		for(AuditLog u : nd) {
			
			try {
			
				
			out.println("<tr>");
			out.println("<td style=\"vertical-align: middle;\">" +u.getId()+"</td>");
			out.println("<td style=\"vertical-align: middle;\">" +u.getUtente() + "</td>");
			out.println("<td style='vertical-align: middle;'>"
					+ u.getOperazione()
					+ "</td>");
			
			
			out.println("<td style='vertical-align: middle;'>"
					+ formato.format(u.getData())
					+ "</td>");
			
			out.println("<td style=\"vertical-align: middle;\">");
			out.println("<form action="+request.getContextPath()+"/admin/deleteLog?id="+u.getId()+" method='post'>"
			   + "<button type='submit' class='btn btn-danger btn-sm'>"
			   + "<i class='bi bi-trash'></i>"
			   	+ "</button>" +
	           "</form>");
			out.println("</td>");
			out.println("</tr>");
		}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new ServletException(e.getMessage());
				}
		}

	}



}
