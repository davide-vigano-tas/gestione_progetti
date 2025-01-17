package eu.tasgroup.gestione.businesscomponent.facade;

import java.util.List;

import javax.naming.NamingException;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.ProjectBC;
import eu.tasgroup.gestione.businesscomponent.ProjectTaskBC;
import eu.tasgroup.gestione.businesscomponent.TimesheetBC;
import eu.tasgroup.gestione.businesscomponent.UserBC;
import eu.tasgroup.gestione.businesscomponent.enumerated.Fase;
import eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli;
import eu.tasgroup.gestione.businesscomponent.enumerated.StatoTask;
import eu.tasgroup.gestione.businesscomponent.model.Project;
import eu.tasgroup.gestione.businesscomponent.model.ProjectTask;
import eu.tasgroup.gestione.businesscomponent.model.Role;
import eu.tasgroup.gestione.businesscomponent.model.Skill;
import eu.tasgroup.gestione.businesscomponent.model.Timesheet;
import eu.tasgroup.gestione.businesscomponent.model.User;

public class ProjectManagerFacade {

	private static ProjectManagerFacade pmf;
	private UserBC userBC;
	private ProjectBC projectBC;
	private ProjectTaskBC projectTaskBC;
	private TimesheetBC timesheetBC;

	private ProjectManagerFacade() {
	}

	public static ProjectManagerFacade getIstance() throws DAOException, NamingException {
		if (pmf == null)
			pmf = new ProjectManagerFacade();

		return pmf;
	}

	/*------------------------Crea o modifica il Project Manager*/
	public User createOrUpdateProjectManager(User user) throws DAOException, NamingException {
		userBC = new UserBC();
		User created = userBC.createOrUpdate(user);
		if (user.getId() == 0) {
			Role role = new Role();
			role.setRole(Ruoli.PROJECT_MANAGER);
			userBC.addRole(created, role);
		}
		return created;
	}

	/*-------------------------------Project Manager in base all'id*/
	public User getProjectManagerById(long id) throws DAOException, NamingException {
		userBC = new UserBC();
		return userBC.getById(id);
	}

	/*-------------------------------Project Manager in base allo username*/
	public User getProjectManagerByUsername(String username) throws DAOException, NamingException {
		userBC = new UserBC();
		return userBC.getByUsername(username);
	}

	/*-------------------------------Project Manager in base alla email*/
	public User getProjectManagerByEmail(String email) throws DAOException, NamingException {
		userBC = new UserBC();
		return userBC.getByEmail(email);
	}

	public User[] getByRole(Ruoli role) throws DAOException, NamingException {
		userBC = new UserBC();
		return userBC.getByRole(role);
	}
	
	public User[] getBySkill(Skill skill) throws DAOException, NamingException {
		userBC = new UserBC();
		return userBC.getBySkill(skill);
	}
	
	/*--------------------------- lista dei dipendenti non assegnati */
	public User[] getDipendentiNonAssegnati() throws DAOException, NamingException {
		return userBC.getDipendentiNonAssegnati();
	}

	/*------------------------------- Gestione dei progetti --------------------------------*/

	/*------------------------------- Crea o aggiorna un progetto */
	public Project createOrUpdateProject(Project project) throws DAOException, NamingException {
		projectBC = new ProjectBC();
		return projectBC.createOrUpdate(project);
	}

	/*------------------------------------Percentuale completamento progetto in base all'id*/
	public int getPercentualeCompletamentoProjectID(long id) throws DAOException, NamingException {
		projectBC = new ProjectBC();
		return projectBC.getPercentualeCompletamento(id);
	}

	/*--------------------------------------Progetto in base all'ID*/
	public Project getProjectById(long id) throws DAOException, NamingException {
		projectBC = new ProjectBC();
		return projectBC.getById(id);
	}

	/*--------------------------------------Progetti associati al Project Manager*/
	public List<Project> getProjectsByCliente(User user) throws DAOException, NamingException {
		projectBC = new ProjectBC();
		return projectBC.getListProjectByResponsabile(user.getId());
	}

	/*------------------------------- Gestione delle task --------------------------------*/
	
	public ProjectTask createOrUpdateProjectTask(ProjectTask pt) throws DAOException, NamingException {
		projectTaskBC = new ProjectTaskBC();
		return projectTaskBC.createOrUpdate(pt);
	}
	
	public ProjectTask updateProjectTaskFase(Fase fase, long id) throws DAOException, NamingException {
		projectTaskBC = new ProjectTaskBC();
		return projectTaskBC.updateFase(fase,id);
	}
	
	public ProjectTask updateProjectTaskStato(StatoTask stato, long id) throws DAOException, NamingException {
		projectTaskBC = new ProjectTaskBC();
		return projectTaskBC.updateStato(stato, id);
	}
	
	public void deleteProjectTask(long id) throws DAOException, NamingException {
		projectTaskBC = new ProjectTaskBC();
		projectTaskBC.delete(id);
	}
	
	public ProjectTask getProjectTaskByID(long id) throws DAOException, NamingException {
		projectTaskBC = new ProjectTaskBC();
		return projectTaskBC.getByID(id);
	}
	
	public ProjectTask[] getAllProjectTask() throws DAOException, NamingException {
		projectTaskBC = new ProjectTaskBC();
		return projectTaskBC.getAll();
	}
	
	public List<ProjectTask> getListProjectTaskByDipendente(long idDip) throws DAOException, NamingException {
		projectTaskBC = new ProjectTaskBC();
		return projectTaskBC.getByDipendente(idDip);
	}
	
	public List<ProjectTask> getListProjectTaskByProject(long idProject) throws DAOException, NamingException {
		projectTaskBC = new ProjectTaskBC();
		return projectTaskBC.getByProject(idProject);
	}
	

	/*------------------------------- Gestione dei timesheet --------------------------------*/
	
	public Timesheet getTimesheetById(long id) throws DAOException, NamingException {
		timesheetBC = new TimesheetBC();
		return timesheetBC.getById(id);
	}
	
	public void approvaTimesheet(long id, boolean stato) throws DAOException, NamingException {
		timesheetBC = new TimesheetBC();
		timesheetBC.approva(id, stato);
	}
	
	public Timesheet[] getAllTimesheet() throws DAOException, NamingException {
		timesheetBC = new TimesheetBC();
		return timesheetBC.getAll();
	}
}
