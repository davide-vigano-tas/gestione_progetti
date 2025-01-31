package eu.tasgroup.gestione.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.enumerated.Fase;
import eu.tasgroup.gestione.businesscomponent.enumerated.StatoProgetto;
import eu.tasgroup.gestione.businesscomponent.enumerated.StatoTask;
import eu.tasgroup.gestione.businesscomponent.facade.DipendenteFacade;
import eu.tasgroup.gestione.businesscomponent.model.AuditLog;
import eu.tasgroup.gestione.businesscomponent.model.Project;
import eu.tasgroup.gestione.businesscomponent.model.ProjectTask;
import eu.tasgroup.gestione.businesscomponent.security.EscapeHTML;
import eu.tasgroup.gestione.businesscomponent.utility.EmailUtil;

/**
 * Servlet implementation class NewProject
 */
@WebServlet("/dipendente/updateStatoTask")
public class UpdateTaskStato extends HttpServlet {
	private static final long serialVersionUID = -951592250789737528L;
	private DipendenteFacade df;

	@Override
	public void init() throws ServletException {
		try {
			df = DipendenteFacade.getInstance();
		} catch (DAOException | NamingException e) {
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			Long id = Long.parseLong(EscapeHTML.escapeHtml(request.getParameter("taskId")));
			StatoTask stato = StatoTask.valueOf(request.getParameter("statoTask").toUpperCase());

			if (stato.equals(StatoTask.DA_INIZIARE)) {
				stato = StatoTask.IN_PROGRESS;
				
				Project project = df.getProjectById(df.getProjectTaskById(id).getIdProgetto());
				if(project.getStato() == StatoProgetto.CREATO) {
					project.setStato(StatoProgetto.IN_PROGRESS);
					df.createOrUpdateProject(project);
				}
				
				df.updateProjectTaskStato(stato, id);
			}else if (stato.equals(StatoTask.IN_PROGRESS)) {
				stato = StatoTask.COMPLETATO;
				df.updateProjectTaskStato(stato, id);
				
				// calcolo percentuale progetto
				ProjectTask task = df.getProjectTaskById(id);
				List<ProjectTask> tasks;
				double percentuale = 0;
				double percentualeParziale;
				// Ciclo su tutte le fasi
				for (Fase fase : Fase.values()) {
					double percentualeFase =0;
					// grupppo di task appartenenti alla determinata fase
					tasks = df.getTaskByFaseAndProject(fase, task.getIdProgetto());

					// controllo che ci siano task della fase corrente
					if (tasks.size() != 0) {
						// se la fase è deploy o plan valse 10 sulla percentuale totale
						// 10+20+20+20+20+10
						if (fase != Fase.PLAN && fase != Fase.DEPLOY) {
							percentualeParziale = 20 / tasks.size();
							for (ProjectTask el : tasks) {
								if (el.getStato() == StatoTask.COMPLETATO) {
									percentuale += percentualeParziale;
									percentualeFase += percentualeParziale;
								}
							}
							if(percentualeFase>=20) {
					        	String emailContent = "<!DOCTYPE html><html lang=\"en\">"
					                    + "<head><meta charset=\"UTF-8\"></head>"
					                    + "<body>"
					                    + "<div style='background-color:#f4f4f4;padding:20px;'>"
					                    + "<div style='max-width:600px;margin:0 auto;background:#ffffff;padding:20px;border-radius:8px;'>"
					                    + "<h1 style='text-align:center;color:#007bff;'>Fase: "+fase+" completata</h1>"
					                    + "</div></div></body></html>";
					        	EmailUtil.sendEmail(df.getById(df.getProjectById(task.getIdProgetto()).getIdResponsabile()).getEmail(), "Fase completata", emailContent);
							}
						} else {
							percentualeParziale = 10 / tasks.size();
							for (ProjectTask el : tasks) {
								if (el.getStato() == StatoTask.COMPLETATO)
									percentuale += percentualeParziale;
								percentualeFase += percentualeParziale;
							}
							if(percentualeFase>=10) {
					        	String emailContent = "<!DOCTYPE html><html lang=\"en\">"
					                    + "<head><meta charset=\"UTF-8\"></head>"
					                    + "<body>"
					                    + "<div style='background-color:#f4f4f4;padding:20px;'>"
					                    + "<div style='max-width:600px;margin:0 auto;background:#ffffff;padding:20px;border-radius:8px;'>"
					                    + "<h1 style='text-align:center;color:#007bff;'>Fase: "+fase+" completata</h1>"
					                    + "</div></div></body></html>";
					        	EmailUtil.sendEmail(df.getById(df.getProjectById(task.getIdProgetto()).getIdResponsabile()).getEmail(), "Fase completata", emailContent);
							}
						}
					}
				}
				if(percentuale>=100) {
					percentuale=100;

					Project project = df.getProjectById(df.getProjectTaskById(id).getIdProgetto());
					project.setStato(StatoProgetto.COMPLETATO);
					df.createOrUpdateProject(project);
				}
				
				int value= (int) percentuale;
				df.updatePercentualeCompletamentoProjectID(task.getIdProgetto(), value);

			}
			AuditLog log = new AuditLog();
			log.setData(new Date());
			log.setOperazione("Stato task aggiornanato a : "+stato + " da utente: " +(String) request.getSession().getAttribute("username"));
			log.setUtente((String) request.getSession().getAttribute("username"));
			df.createOrupdateAuditLog(log);
			response.sendRedirect("dip-tasks.jsp");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
	}

}
