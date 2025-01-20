package eu.tasgroup.gestione.controller;

import java.io.IOException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli;
import eu.tasgroup.gestione.businesscomponent.facade.AdminFacade;
import eu.tasgroup.gestione.businesscomponent.model.Role;
import eu.tasgroup.gestione.businesscomponent.model.User;
import eu.tasgroup.gestione.businesscomponent.security.EscapeHTML;


@WebServlet("/admin/addUser")
public class AddUser extends HttpServlet {

	private static final long serialVersionUID = 4087559667260089135L;
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
		String nome = EscapeHTML.escapeHtml(request.getParameter("nome"));
		String cognome = EscapeHTML.escapeHtml(request.getParameter("cognome"));
		String username = EscapeHTML.escapeHtml(request.getParameter("username"));
		String password = EscapeHTML.escapeHtml(request.getParameter("password"));
		String email = EscapeHTML.escapeHtml(request.getParameter("email"));
		String tipo = EscapeHTML.escapeHtml(request.getParameter("type"));
		
		
		validateField(nome, "nome", "Il campo nome non può essere vuoto e deve contenere "
				+ "solo lettere.");
		validateField(cognome, "cognome", "Il campo cognome non può essere vuoto e deve contenere "
				+ "solo lettere.");
		validateField(username, "username", "Il campo username non può essere vuoto e deve contebere  da 4 a 10 caratteri");
		validateField(password, "password", "Il campo password non può essere vuoto e deve rispettare i criteri di complessità");
		validateField(email, "email", "Il campo email non può essere vuoto e deve contenere essere valida.");
		try {
		//Sarebbe da verificare anche qui che usernmae e mail non siano presenti
		if(af.getByUsername(username) != null) {
			response.sendRedirect("../admin/users.jsp?error=username_taken");
			return;
		}
		if(af.getByEmail(email) != null) {
			response.sendRedirect("../admin/users.jsp?error=email_taken");
			return;
		}
		User user = new User();
		user.setNome(nome);
		user.setCognome(cognome);
		user.setEmail(email);
		user.setPassword(password);
		user.setUsername(username);
		
		
		
		user = af.createUser(user);
		
		Role role = new Role();
		
		role.setIdUser(user.getId());
		role.setRole(Ruoli.valueOf(tipo));
		
		af.addRole(user, role);
		
		response.sendRedirect("../admin/users.jsp");
	
		
		}catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
	}
	
	private void validateField(String field, String fieldName, String errorMessage) throws ServletException {
		if(field==null||field.trim().isEmpty()) {
			throw new ServletException("Il campo "+ fieldName + " non può essere vuoto");
		}
		
		switch(fieldName) {
		case "nome":
		case "cognome":
			if(!field.matches("^[a-zA-Z ,.'-]{2,30}$")) {
				throw new ServletException(errorMessage);
			}
		break;
		case "username":
			if(!field.matches("^[a-zA-Z0-9!.-]{4,10}$"))
				throw new ServletException(errorMessage);
		break;
		case "password":
			if(!field.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^?!=])[a-zA-Z0-9@#$%^?!=]{7,15}$"))
				throw new ServletException(errorMessage);
		break;
		case "email":
			if(!field.matches("^[\\w.%+-]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,}$"))
				throw new ServletException(errorMessage);
		break;
		}
	}

}
