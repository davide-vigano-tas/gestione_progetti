package eu.tasgroup.gestione.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
import eu.tasgroup.gestione.businesscomponent.enumerated.StatoTask;
import eu.tasgroup.gestione.businesscomponent.facade.ProjectManagerFacade;
import eu.tasgroup.gestione.businesscomponent.model.ProjectTask;
import eu.tasgroup.gestione.businesscomponent.security.EscapeHTML;

/**
 * Servlet implementation class NewProject
 */
@WebServlet("/projectManager/insertTask")
public class InsertTask extends HttpServlet {
	private static final long serialVersionUID = -8051496007511743068L;
	private ProjectManagerFacade pmf;
	
	@Override
	public void init() throws ServletException {
		try {
			pmf = ProjectManagerFacade.getInstance();
		} catch (DAOException | NamingException e) {
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			
			Long idProgetto = Long.parseLong(EscapeHTML.escapeHtml(request.getParameter("progetto")));
			String nome = EscapeHTML.escapeHtml(request.getParameter("nome"));
			String descrizione = EscapeHTML.escapeHtml(request.getParameter("descrizione"));
			
			Long idDipendente = Long.parseLong(EscapeHTML.escapeHtml(request.getParameter("dipendente")));
			
			Date scadenza = formato.parse(request.getParameter("scadenza"));
			
			Fase fase = Fase.valueOf(request.getParameter("fase").toUpperCase());
			
			ProjectTask task = new ProjectTask();
			task.setIdProgetto(idProgetto);
			task.setNomeTask(nome);
			task.setDescrizione(descrizione);
			task.setIdDipendente(idDipendente);
			task.setScadenza(scadenza);
			task.setFase(fase);
			
			pmf.createOrUpdateProjectTask(task);
			
			// calcolo percentuale progetto
			List<ProjectTask> tasks;
			int percentuale = 0;
			double percentualeParziale;
			// Ciclo su tutte le fasi
			for (Fase faseCiclo : Fase.values()) {
				// grupppo di task appartenenti alla determinata fase
				tasks = pmf.getTaskByFaseAndProject(faseCiclo, task.getIdProgetto());

				// controllo che ci siano task della fase corrente
				if (tasks.size() != 0) {
					// se la fase è deploy o plan valse 10 sulla percentuale totale
					// 10+20+20+20+20+10
					if (faseCiclo != Fase.PLAN && faseCiclo != Fase.DEPLOY) {
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
			pmf.updatePercentualeCompletamentoProjectID(task.getIdProgetto(), percentuale);
			
			response.sendRedirect("pm-tasks.jsp");
		}catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
	}

}
