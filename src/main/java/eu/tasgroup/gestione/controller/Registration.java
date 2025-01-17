package eu.tasgroup.gestione.controller;

import java.io.IOException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.facade.ClienteFacade;
import eu.tasgroup.gestione.businesscomponent.model.User;
import eu.tasgroup.gestione.businesscomponent.security.Algoritmo;
import eu.tasgroup.gestione.businesscomponent.security.EscapeHTML;

/**
 * Servlet implementation class Registration
 */
@WebServlet("/registra")
public class Registration extends HttpServlet {
       
 
	private static final long serialVersionUID = 3348905594221304356L;

	private ClienteFacade cf;
   

	@Override
	public void init() throws ServletException {
		try {
			cf = ClienteFacade.getInstance();
		} catch (DAOException | NamingException e) {
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String nome = EscapeHTML.escapeHtml(request.getParameter("nome"));
			String cognome = EscapeHTML.escapeHtml(request.getParameter("cognome"));
			String username = EscapeHTML.escapeHtml(request.getParameter("username"));
			String password = EscapeHTML.escapeHtml(request.getParameter("password"));
			String email = EscapeHTML.escapeHtml(request.getParameter("email"));
			
			validateField(nome, "nome", "Il campo nome non può essere vuoto e deve contenere "
					+ "solo lettere.");
			validateField(cognome, "cognome", "Il campo cognome non può essere vuoto e deve contenere "
					+ "solo lettere.");
			validateField(username, "username", "Il campo username non può essere vuoto e deve contebere  da 4 a 10 caratteri");
			validateField(password, "password", "Il campo password non può essere vuoto e deve rispettare i criteri di complessità");
			validateField(email, "email", "Il campo email non può essere vuoto e deve contenere essere valida.");
			
			//Sarebbe da verificare anche qui che usernmae e mail non siano presenti
			if(cf.getByUsername(username) != null) throw new IllegalArgumentException("Username già in uso");
			if(cf.getByEmail(email) != null) throw new IllegalArgumentException("Username già in uso");
		
			User user = new User();
			user.setNome(nome);
			user.setCognome(cognome);
			user.setPassword(Algoritmo.converti(password));
			user.setUsername(username);
			user.setEmail(email);
			
			cf.createOrUpdateCliente(user);
			
			response.sendRedirect("login.jsp?option=cliente");
			
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
