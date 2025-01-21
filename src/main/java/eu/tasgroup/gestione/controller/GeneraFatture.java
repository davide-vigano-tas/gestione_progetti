package eu.tasgroup.gestione.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.facade.ClienteFacade;
import eu.tasgroup.gestione.businesscomponent.model.Payment;
import eu.tasgroup.gestione.businesscomponent.utility.InvoicePDFGenerator;

@WebServlet("/cliente/generaFattura")
public class GeneraFatture extends HttpServlet {

	private static final long serialVersionUID = -1021273040492394604L;

	private ClienteFacade cf;

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
		String idPagamento = request.getParameter("idPagamento");
		String username = (String) request.getSession().getAttribute("username");
		
		// Recupera il pagamento tramite l'ID
		Payment payment;
		String operazione = new String();
		
		try {
			payment = ClienteFacade.getInstance().getPaymentById(Long.parseLong(idPagamento));
			
			operazione = "Genera fattura per il pagamento " + idPagamento;
			cf.saveLogMessage(username, operazione);
			
			if (payment != null) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition",
						"attachment; filename=fattura_pagamento_" + idPagamento + ".pdf");

				try (OutputStream out = response.getOutputStream()) {
					// Genera il PDF con PDFBox
					InvoicePDFGenerator.generatePDF(out, payment);
					
					operazione = "Fattura generata correttamente";
					cf.saveLogMessage(username, operazione);
				} catch (Exception e) {
					e.printStackTrace();
					operazione = "Errore nel generare la fattura";
					cf.saveLogMessage(username, operazione);
					
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Errore durante la generazione della fattura.");
				}
			} else {
				operazione = "Errore nel generare la fattura, pagamento non trovato";
				cf.saveLogMessage(username, operazione);
				
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Pagamento non trovato.");
			}

		} catch (NumberFormatException | DAOException | NamingException e) {
			e.printStackTrace();
		}
	}
}
