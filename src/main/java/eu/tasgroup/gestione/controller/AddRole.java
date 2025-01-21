package eu.tasgroup.gestione.controller;

import java.io.IOException;
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
import eu.tasgroup.gestione.businesscomponent.model.AuditLog;
import eu.tasgroup.gestione.businesscomponent.model.Role;
import eu.tasgroup.gestione.businesscomponent.model.User;


@WebServlet("/admin/addRole")
public class AddRole extends HttpServlet {


	private static final long serialVersionUID = -3441532071007377099L;


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
		if(id == null) {
			response.sendRedirect("../admin/dettagliUtente.jsp?error=not_found");
			return;
		}
		long user_id = Long.parseLong(id);
		String role = request.getParameter("role");
		try {
			User user = af.getUserById(user_id);
			if(user == null) {
				response.sendRedirect("../admin/dettagliUtente.jsp?error=not_found");
				return;
			}
			Role r = new Role();
			r.setIdUser(user_id);
			r.setRole(Ruoli.valueOf(role));
			af.addRole(user, r);
			
			AuditLog log = new AuditLog();
			log.setData(new Date());
			log.setOperazione("Aggiunto ruolo : "+r.getRole().name()+", a: "+user.getUsername());
			log.setUtente((String) request.getSession().getAttribute("username"));
			af.createOrupdateAuditLog(log);
			
			response.sendRedirect("../admin/dettagliUtente.jsp?id="+user_id);
		}catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
	}

}
