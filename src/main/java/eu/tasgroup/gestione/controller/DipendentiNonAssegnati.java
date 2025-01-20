package eu.tasgroup.gestione.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.facade.AdminFacade;
import eu.tasgroup.gestione.businesscomponent.model.Role;
import eu.tasgroup.gestione.businesscomponent.model.User;

@WebServlet("/admin/nonassegnati")
public class DipendentiNonAssegnati extends HttpServlet {


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
		
		  User[] nd = null;
		  Role[] user_roles = null;
			try {
				nd = af.getDipendentiNonAssegnati();
				 
			}catch (DAOException | NamingException e) {
				e.printStackTrace();
				throw new ServletException("Errore durante l'esecuzione della query: "+e.getMessage());
			}
		
		
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		for(User u : nd) {
			
			try {
			
				user_roles= af.getRolesById(u.getId());
				String roles = "";
				for(Role role : user_roles) {
					roles += role.getRole().name()+" ";
				}
			out.println("<tr>");
			out.println("<td style=\"vertical-align: middle;\">" +u.getUsername()+"</td>");
			out.println("<td style=\"vertical-align: middle;\">" +u.getEmail() + "</td>");
			out.println("<td style='vertical-align: middle;'>"
					+ "<strong>"+roles+"</strong>"
					+ "</td>");
			
			out.println("<td style=\"vertical-align: middle;\">");
			out.println("<a type=\"submit\" class=\"btn btn-primary btn-sm\""
					+ "	href="+request.getContextPath()+"/admin/dettagliUtente.jsp?id="+u.getId()+">"
					+ "	<i class = \"bi bi-person\"></i>"
					+ "		&nbsp; Dettagli"
					+ "	</a>");
			out.println("</td>");
			out.println("</tr>");
		}catch (DAOException | NamingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new ServletException(e.getMessage());
				}
		}

	}



}
