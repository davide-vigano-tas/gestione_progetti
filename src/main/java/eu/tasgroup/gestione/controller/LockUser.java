package eu.tasgroup.gestione.controller;

import java.io.IOException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.facade.AdminFacade;
import eu.tasgroup.gestione.businesscomponent.model.User;

/**
 * Servlet implementation class BlockUser
 */
@WebServlet("/admin/blocca")
public class LockUser extends HttpServlet {


	private static final long serialVersionUID = 1943090764159944410L;

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
		String id = request.getParameter("id");
		long user_id = Long.parseLong(id);
		
		try {
			User user = af.getUserById(user_id);
			if(user == null) {
				response.sendRedirect("../admin/users.jsp?error=not_found");
				return;
			}
			
			user.setLocked(true);
			af.createUser(user);
			
			response.sendRedirect("../admin/dettagliUtente.jsp?id="+user_id);
		}catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
	}

}
