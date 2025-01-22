package eu.tasgroup.gestione.restcontroller;

import java.io.IOException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.facade.DipendenteFacade;
import eu.tasgroup.gestione.businesscomponent.model.User;

public class DipendenteAPI extends HttpServlet {

	private static final long serialVersionUID = 1705224693728185410L;

	private DipendenteFacade df;

	@Override
	public void init() throws ServletException {
		try {
			df = DipendenteFacade.getInstance();
		} catch (DAOException | NamingException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		String id = request.getParameter("id");
		try {

			if (id != null) {
				User dipendente = df.getById(Long.parseLong(id));

				if (dipendente != null) {
					String jsonResponse = userJsonConvert(dipendente);
					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().write(jsonResponse);
				} else {
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					response.getWriter().write("{\"error\": \"Dipendente non trovato\"}");
				}
			} else {
				User[] dipendenti = df.getAllDipendenti();
				StringBuilder jsonResponse = new StringBuilder("[");

				for (User dipendente : dipendenti) {
					jsonResponse.append(userJsonConvert(dipendente)).append(",");
				}

				// rimuovo l'ultima virgola

				if (dipendenti.length > 0) {
					jsonResponse.setLength(jsonResponse.length() - 1);
				}

				jsonResponse.append("]");

				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write(jsonResponse.toString());
			}

		} catch (NumberFormatException | DAOException | NamingException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"error\": \"Errore interno del server non trovato\"}");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Recupero parametri
		String nome = request.getParameter("nome");
		String cognome = request.getParameter("cognome");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");

		// Validazione parametri
		String validationError = validateParameters(nome, cognome, username, password, email);
		if (validationError != null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("{\"error\": \"" + validationError + "\"}");
			return;
		}

		// Creazione del nuovo dipendente
		User nuovoDipendente = new User();
		nuovoDipendente.setNome(nome);
		nuovoDipendente.setCognome(cognome);
		nuovoDipendente.setUsername(username);
		nuovoDipendente.setPassword(password);
		nuovoDipendente.setEmail(email);

		try {
			if (df.getByEmail(nuovoDipendente.getEmail()) != null) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("{\"error\": \"Email già presente\"}");
			} else if (df.getByUsername(nuovoDipendente.getUsername()) != null) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("{\"error\": \"Username già presente\"}");
			} else {
				df.createOrUpdateDipendente(nuovoDipendente);
				response.setStatus(HttpServletResponse.SC_CREATED);
			}
		} catch (DAOException | NamingException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"error\": \"Errore interno del server\"}");
			e.printStackTrace();
		}
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Recupero parametri
		String id = request.getParameter("id");
		try {
			User dipendente = df.getById(Long.parseLong(id));

			if (dipendente != null) {
				String nome = request.getParameter("nome");
				if (nome != null && !nome.isEmpty())
					dipendente.setNome(nome);
				String cognome = request.getParameter("cognome");
				if (cognome != null && !cognome.isEmpty())
					dipendente.setCognome(cognome);

				String username = request.getParameter("username");
				if (username != null && !username.isEmpty() && df.getByUsername(username) == null)
					dipendente.setUsername(username);

				String email = request.getParameter("email");
				if (email != null && !email.isEmpty() && df.getByEmail(email) == null)
					dipendente.setEmail(email);

				response.setStatus(HttpServletResponse.SC_OK);

			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("{\"error\": \"Dipendendente non presente\"}");
			}

		} catch (DAOException | NamingException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"error\": \"Errore interno del server\"}");
			e.printStackTrace();
		}
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		try {
			if (id != null) {
				User dipendete = df.getById(Long.parseLong(id));

				if (dipendete != null) {
					df.deleteDipendente(Long.parseLong(id));
					response.setStatus(HttpServletResponse.SC_OK);
				} else {
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					response.getWriter().write("{\"error\": \"A quel ID non è associato nessun dipendente\"}");
				}
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("{\"error\": \"ID mancante\"}");
			}
		} catch (NumberFormatException | DAOException | NamingException e) {
			e.printStackTrace();
		}
	}

	private String userJsonConvert(User user) {
		return "{\n" + "  \"id\": " + user.getId() + ",\n" + "  \"nome\": \"" + user.getCognome() + "\",\n"
				+ "  \"cognome\": \"" + user.getEmail() + "\",\n" + "  \"username\": \"" + user.getUsername() + "\",\n"
				+ "  \"password\": \"" + user.getPassword() + "\",\n" + "  \"email\": \"" + user.getEmail() + "\",\n"
				+ "  \"tentativiFalliti\": " + user.getTentativiFalliti() + ",\n" + "  \"locked\": " + user.isLocked()
				+ ",\n" + "  \"dataCreazione\": \"" + user.getDataCreazione() + "\"\n" + "}";
	}

	private String validateParameters(String nome, String cognome, String username, String password, String email) {
		if (nome == null || nome.isEmpty())
			return "Nome richiesto per creare un cliente";
		if (cognome == null || cognome.isEmpty())
			return "Cognome richiesto per creare un cliente";
		if (username == null || username.isEmpty())
			return "Username richiesto per creare un cliente";
		if (password == null || password.isEmpty())
			return "Password richiesto per creare un cliente";
		if (email == null || email.isEmpty())
			return "Email richiesto per creare un cliente";
		return null;
	}

}
