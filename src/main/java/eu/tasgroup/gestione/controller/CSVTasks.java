package eu.tasgroup.gestione.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.facade.AdminFacade;
import eu.tasgroup.gestione.businesscomponent.model.AuditLog;
import eu.tasgroup.gestione.businesscomponent.model.Project;
import eu.tasgroup.gestione.businesscomponent.model.ProjectTask;

/**
 * Servlet implementation class CSVTasks
 */
@WebServlet("/admin/reportTasks")
public class CSVTasks extends HttpServlet {
       

	private static final long serialVersionUID = 2139339789609852781L;

	private static final Logger LOGGER = Logger.getLogger(CSVTasks.class.getName());

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
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("username");
        if(username == null) {
        	response.sendRedirect("login.jsp");
        }
		String id = request.getParameter("id");
		if(id == null) {
			LOGGER.severe("ID ordine non trovato nella richiesta");
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID ordine non trovato nella richiesta");
			return;
		}
		
		
		
		
		response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8");

        // Suggest a filename for download
        response.setHeader("Content-Disposition", "attachment; filename=\"tasks-of-"+id+".csv\"");
        
        
        try (PrintWriter writer = response.getWriter()) {
        	
        	writer.println("ID;Nome;Descrizione;Dipendente;Stato;Scadenza;Fase");
        	
        	try {
        		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyy");
        		Project p = af.getProjectById(Long.parseLong(id));
        		List<ProjectTask> tasks = af.getTasksByProject(p);
        		for(ProjectTask task: tasks) {
        			writer.println(task.getId()+";"+task.getNomeTask()+";"+task.getDescrizione()+
        					";"+task.getIdDipendente()+";"+task.getStato()+";"+formato.format(task.getScadenza())+
        					";"+task.getFase());
        		}
        		writer.flush();
        		AuditLog log = new AuditLog();
        		log.setData(new Date());
        		log.setOperazione("Stampa CSV task");
        		log.setUtente(username);
        		AdminFacade.getInstance().createOrupdateAuditLog(log);
        	}catch (DAOException | NamingException e) {
				e.printStackTrace();
				throw new ServletException(e.getMessage());
			}
        	
        }
	}

	

}
