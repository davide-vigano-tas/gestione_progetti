package eu.tasgroup.gestione.controller;

import java.io.IOException;
import java.util.Arrays;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli;
import eu.tasgroup.gestione.businesscomponent.facade.AdminFacade;
import eu.tasgroup.gestione.businesscomponent.model.Role;
import eu.tasgroup.gestione.businesscomponent.model.User;
import eu.tasgroup.gestione.businesscomponent.security.Algoritmo;
import eu.tasgroup.gestione.businesscomponent.security.EscapeHTML;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	
	
	private static final long serialVersionUID = 5722794182564878684L;
	
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



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String userType = request.getParameter("userType");
		
		username = EscapeHTML.escapeHtml(username);
		password = EscapeHTML.escapeHtml(password);
		userType = EscapeHTML.escapeHtml(userType);
		
		
		HttpSession session = request.getSession();
		
		if(username != null && password != null & userType != null) {
			try {
				
				User user = af.getByUsername(username);
				if(user == null)  {
					response.sendRedirect("login.jsp?error=not_found");
					return ;
				}
				if(!Algoritmo.verificaPassword(password, user.getPassword())) {
					response.sendRedirect("login.jsp?error=incorrect_password");
					return;
				}
					
				
				Role[] roles = af.getRolesById(user.getId());
				session.setAttribute("username", username);
				if(Arrays.asList(roles).stream().anyMatch(r -> r.getRole().equals(Ruoli.CLIENTE))) {
					response.sendRedirect("cliente/home.jsp");
					return;
				} if(Arrays.asList(roles).stream().anyMatch(r -> r.getRole().equals(Ruoli.DIPENDENTE))) {
					response.sendRedirect("dipendente/home.jsp");
					return;
				} if(Arrays.asList(roles).stream().anyMatch(r -> r.getRole().equals(Ruoli.PROJECT_MANAGER))) {
					response.sendRedirect("projectmanager/home.jsp");
					return;
				} if(Arrays.asList(roles).stream().anyMatch(r -> r.getRole().equals(Ruoli.ADMIN))) {
					response.sendRedirect("admin/admin-home.jsp");
					return;
				} 
				
			}catch (Exception e) {
				e.printStackTrace();
				throw new ServletException("Errore durante la verifica dell'utente: "+e.getMessage());
			}
		} else {
			response.sendRedirect("login.jsp");
		}
	}

}
