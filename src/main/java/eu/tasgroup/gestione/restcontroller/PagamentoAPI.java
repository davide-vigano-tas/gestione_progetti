package eu.tasgroup.gestione.restcontroller;

import java.io.IOException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.PaymentBC;
import eu.tasgroup.gestione.businesscomponent.model.Payment;


public class PagamentoAPI extends HttpServlet {

	private static final long serialVersionUID = -4505580327647457066L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		String id = request.getParameter("id");
		try {
			PaymentBC paymentBC = new PaymentBC();
			if (id != null) {
				Payment payment = paymentBC.getById(Long.parseLong(id));

				if (payment != null) {
					String jsonResponse = paymentJsonConvert(payment);
					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().write(jsonResponse);
				} else {
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					response.getWriter().write("{\"error\": \"Pagamento non trovato\"}");
				}
			} else {
				Payment[] payments = paymentBC.getAll();
				StringBuilder jsonResponse = new StringBuilder("[");

				for (Payment p : payments) {
					jsonResponse.append(paymentJsonConvert(p)).append(",");
				}

				// rimuovo l'ultima virgola

				if (payments.length > 0) {
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
	
	private String paymentJsonConvert(Payment payment) {
	    return "{\n" + 
	           "  \"id\": " + payment.getId() + ",\n" + 
	           "  \"idProgetto\": " + payment.getIdProgetto() + ",\n" + 
	           "  \"cifra\": " + payment.getCifra() + "\n" + 
	           "}";
	}
}
