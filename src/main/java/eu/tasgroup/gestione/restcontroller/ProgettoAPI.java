package eu.tasgroup.gestione.restcontroller;

import java.io.IOException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.ProjectBC;
import eu.tasgroup.gestione.businesscomponent.model.Project;

public class ProgettoAPI extends HttpServlet {

	private static final long serialVersionUID = -3400849678402625991L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		String id = request.getParameter("id");
		try {
			ProjectBC projectBC = new ProjectBC();
			if (id != null) {
				Project progetto = projectBC.getById(Long.parseLong(id));

				if (progetto != null) {
					String jsonResponse = progettoJsonConvert(progetto);
					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().write(jsonResponse);
				} else {
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					response.getWriter().write("{\"error\": \"Progetto non trovato\"}");
				}
			} else {
				Project[] progetti = projectBC.getAll();
				StringBuilder jsonResponse = new StringBuilder("[");

				for (Project p : progetti) {
					jsonResponse.append(progettoJsonConvert(p)).append(",");
				}

				// rimuovo l'ultima virgola

				if (progetti.length > 0) {
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
	
	private String progettoJsonConvert(Project progetto) {
	    return "{\n" + 
	           "  \"id\": " + progetto.getId() + ",\n" + 
	           "  \"nomeProgetto\": \"" + progetto.getNomeProgetto() + "\",\n" + 
	           "  \"descrizione\": \"" + progetto.getDescrizione() + "\",\n" + 
	           "  \"dataInizio\": \"" + progetto.getDataInizio() + "\",\n" + 
	           "  \"dataFine\": \"" + progetto.getDataFine() + "\",\n" + 
	           "  \"budget\": " + progetto.getBudget() + ",\n" + 
	           "  \"stato\": \"" + progetto.getStato() + "\",\n" + 
	           "  \"idCliente\": " + progetto.getIdCliente() + ",\n" + 
	           "  \"idResponsabile\": " + progetto.getIdResponsabile() + ",\n" + 
	           "  \"percentualeCompletamento\": " + progetto.getPercentualeCompletamento() + ",\n" + 
	           "  \"costoProgetto\": " + progetto.getCostoProgetto() + "\n" + 
	           "}";
	}
}
