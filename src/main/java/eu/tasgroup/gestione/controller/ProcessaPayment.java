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
import eu.tasgroup.gestione.businesscomponent.model.Payment;
import eu.tasgroup.gestione.businesscomponent.model.Project;

/**
 * Servlet implementation class ProcessaPayment
 */
@WebServlet("/cliente/processaPayment")
public class ProcessaPayment extends HttpServlet {

	private static final long serialVersionUID = -4590072582067014427L;
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

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Parametri inviati dal form
		String projectIdParam = request.getParameter("projectId");
		String amountParam = request.getParameter("amount");

		try {
			long projectId = Long.parseLong(projectIdParam);
			double amount = Double.parseDouble(amountParam);

			// Ottieni i dettagli del progetto
			Project project = cf.getProjectById(projectId);

			if (project == null) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("{\"success\": false, \"message\": \"Progetto non trovato.\"}");
				return;
			}

			// Calcola il totale già pagato per il progetto
			double totalPaid = cf.getTotalPaymentsByProjectId(projectId);
			double remainingAmount = project.getCostoProgetto() - totalPaid;

			if (amount <= 0 || amount > remainingAmount) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter()
						.write("{\"success\": false, \"message\": \"Importo non valido. Puoi pagare fino a €"
								+ remainingAmount + ".\"}");
				return;
			}

			// Esegui l'inserimento del pagamento
			Payment payment = new Payment();
			payment.setIdProgetto(projectId);
			payment.setCifra(amount);

			cf.createPayment(payment);

			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().write(
					"{\"success\": true, \"message\": \"Pagamento di €" + amount + " registrato con successo.\"}");
			response.sendRedirect("../cliente/cliente-home.jsp");
		} catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("{\"success\": false, \"message\": \"Dati non validi.\"}");
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter()
					.write("{\"success\": false, \"message\": \"Errore durante il salvataggio del pagamento.\"}");
		}
	}

}
