package eu.tasgroup.gestione.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.logging.Logger;

import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.facade.AdminFacade;
import eu.tasgroup.gestione.businesscomponent.model.AuditLog;
import eu.tasgroup.gestione.businesscomponent.model.Project;
import eu.tasgroup.gestione.businesscomponent.utility.InvoicePDFGenerator;



@WebServlet("/admin/reportProgetto")
public class ReportProgetto extends HttpServlet {
	

	private static final long serialVersionUID = 588015061619510867L;

	private static final Logger LOGGER = Logger.getLogger(ReportProgetto.class.getName());

	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("username");
        if(username == null) {
        	response.sendRedirect("login.jsp");
        }
		String id_p = request.getParameter("id");
		if(id_p == null) {
			LOGGER.severe("ID ordine non trovato nella richiesta");
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID ordine non trovato nella richiesta");
			return;
		}
		// Recupera il pagamento tramite l'ID
		Project project;
		try {
			project = AdminFacade.getInstance().getProjectById(Long.parseLong(id_p));
			if (project != null) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition",
						"attachment; filename=report_progetto_" + project.getId() + ".pdf");

				try (OutputStream out = response.getOutputStream()) {
					// Genera il PDF con PDFBox
					InvoicePDFGenerator.generatePDF(out, project);
				} catch (Exception e) {
					e.printStackTrace();
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Errore durante la generazione della report.");
				}
				AuditLog log = new AuditLog();
				log.setData(new Date());
				log.setOperazione("Produzione report pdf");
				log.setUtente(username);
				AdminFacade.getInstance().createOrupdateAuditLog(log);
			} else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Progetto non trovato.");
			}

		} catch (NumberFormatException | DAOException | NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
	}

}
