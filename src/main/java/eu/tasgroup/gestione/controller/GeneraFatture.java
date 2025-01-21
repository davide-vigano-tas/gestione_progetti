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

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String idPagamento = request.getParameter("idPagamento");

		// Recupera il pagamento tramite l'ID
		Payment payment;
		try {
			payment = ClienteFacade.getInstance().getPaymentById(Long.parseLong(idPagamento));

			if (payment != null) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition",
						"attachment; filename=fattura_pagamento_" + idPagamento + ".pdf");

				try (OutputStream out = response.getOutputStream()) {
					// Genera il PDF con PDFBox
					InvoicePDFGenerator.generatePDF(out, payment);
				} catch (Exception e) {
					e.printStackTrace();
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Errore durante la generazione della fattura.");
				}
			} else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Pagamento non trovato.");
			}

		} catch (NumberFormatException | DAOException | NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
