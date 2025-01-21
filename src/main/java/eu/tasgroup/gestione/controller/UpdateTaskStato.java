package eu.tasgroup.gestione.controller;

import java.io.IOException;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.enumerated.Fase;
import eu.tasgroup.gestione.businesscomponent.enumerated.StatoTask;
import eu.tasgroup.gestione.businesscomponent.facade.DipendenteFacade;
import eu.tasgroup.gestione.businesscomponent.model.ProjectTask;
import eu.tasgroup.gestione.businesscomponent.security.EscapeHTML;

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
				df.updateProjectTaskStato(stato, id);
			}else if (stato.equals(StatoTask.IN_PROGRESS)) {
				stato = StatoTask.COMPLETATO;
				df.updateProjectTaskStato(stato, id);
				
				// calcolo percentuale progetto
				ProjectTask task = df.getProjectTaskById(id);
				List<ProjectTask> tasks;
				int percentuale = 0;
				double percentualeParziale;
				// Ciclo su tutte le fasi
				for (Fase fase : Fase.values()) {
					// grupppo di task appartenenti alla determinata fase
					tasks = df.getTaskByFaseAndProject(fase, task.getIdProgetto());

					// controllo che ci siano task della fase corrente
					if (tasks.size() != 0) {
						// se la fase è deploy o plan valse 10 sulla percentuale totale
						// 10+20+20+20+20+10
						if (fase != Fase.PLAN && fase != Fase.DEPLOY) {
							percentualeParziale = 20 / tasks.size();
							for (ProjectTask el : tasks) {
								if (el.getStato() == StatoTask.COMPLETATO)
									percentuale += Math.ceil(percentualeParziale);
							}
						} else {
							percentualeParziale = 10 / tasks.size();
							for (ProjectTask el : tasks) {
								if (el.getStato() == StatoTask.COMPLETATO)
									percentuale += Math.ceil(percentualeParziale);
							}
						}
					}
				}
				if(percentuale>100)
					percentuale=100;
				df.updatePercentualeCompletamentoProjectID(task.getIdProgetto(), percentuale);

			}

			response.sendRedirect("dip-tasks.jsp");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
	}

}
