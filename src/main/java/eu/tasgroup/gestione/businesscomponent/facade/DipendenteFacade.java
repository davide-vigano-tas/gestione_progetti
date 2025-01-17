package eu.tasgroup.gestione.businesscomponent.facade;

import java.util.List;

import javax.naming.NamingException;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.ProjectBC;
import eu.tasgroup.gestione.businesscomponent.ProjectTaskBC;
import eu.tasgroup.gestione.businesscomponent.SkillBC;
import eu.tasgroup.gestione.businesscomponent.TimesheetBC;
import eu.tasgroup.gestione.businesscomponent.UserBC;
import eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli;
import eu.tasgroup.gestione.businesscomponent.enumerated.StatoTask;
import eu.tasgroup.gestione.businesscomponent.model.Project;
import eu.tasgroup.gestione.businesscomponent.model.ProjectTask;
import eu.tasgroup.gestione.businesscomponent.model.Role;
import eu.tasgroup.gestione.businesscomponent.model.Skill;
import eu.tasgroup.gestione.businesscomponent.model.Timesheet;
import eu.tasgroup.gestione.businesscomponent.model.User;

public class DipendenteFacade {
	
	private static DipendenteFacade df;
	private UserBC userBC;
	private ProjectBC projectBC;
	private TimesheetBC tBC;
	private ProjectTaskBC ptBC;
	private SkillBC sBC;
	
	private DipendenteFacade() throws DAOException, NamingException {
		
		userBC = new UserBC();
		projectBC = new ProjectBC();
		tBC = new TimesheetBC();
		ptBC = new ProjectTaskBC();
		sBC = new SkillBC();
	}
	
	public static DipendenteFacade getInstance() throws DAOException, NamingException {
		if(df == null) 
			df = new DipendenteFacade();
		return df;
	}
	
	// ---------- crea o modifica dipendente
	public User createOrUpdateDipendente(User user) throws DAOException, NamingException {
		User created = userBC.createOrUpdate(user);
		if(user.getId() == 0) {
			Role role = new Role();
			role.setRole(Ruoli.DIPENDENTE);
			userBC.addRole(created, role);
		}
		return created;
	}
	
	/*-------------------------------dip in base all'id*/
	public User getById(long id) throws DAOException, NamingException {
		
		return userBC.getById(id);
	}
	
	/*-------------------------------dip in base allo username*/
	public User getByUsername(String username) throws DAOException, NamingException {
		return userBC.getByUsername(username);
	}
	
	public User getByEmail(String email) throws DAOException, NamingException {
		return userBC.getByEmail(email);
	}
	
	/*------------------------------------Percentuale completamento progetto in base all'id*/
	public int getPercentualeCompletamentoProjectID(long id) throws DAOException, NamingException {
		return projectBC.getPercentualeCompletamento(id);
	}
	
	/*--------------------------------------Progetto in base all'ID*/
	public Project getProjectById(long id) throws DAOException, NamingException {
		return projectBC.getById(id);
	}
	
	// -------------crate or update timesheet
	public void createOrUpdateTimesheet(Timesheet timesheet) throws DAOException, NamingException {
		tBC.createOrUpdate(timesheet);
	}
	
	//-----------------------timesheet by id
	public Timesheet timesheetByID(long id) throws DAOException, NamingException {
		return tBC.getById(id);
	}
	
	//-----------------------timesheet by dip
	public List<Timesheet> timesheetByDipendente(long id) throws DAOException, NamingException {
		return tBC.getListByDipendente(id);
	}
	
	//-----------------------delete timesheet 
	public void deleteTimesheet(long id) throws DAOException, NamingException {
		tBC.delete(id);
	}
	
	//------------------Update stato ProjectTask
	public ProjectTask updateProjectTaskStato(StatoTask stato, long id) throws DAOException {
		return ptBC.updateStato(stato, id);
	}
	
	//------------------ ProjectTask by id
	public ProjectTask getProjectTaskById(long id) throws DAOException {
		return ptBC.getByID(id);
	}
	
	//---------/////////ProjectTask by dipendente
	public List<ProjectTask> getProjectTaskByDipendente(long id) throws DAOException {
		return ptBC.getByDipendente(id);
	}
	
	//------------------------- ProjectTask by proj
	public List<ProjectTask> getProjectTaskByProject(long id) throws DAOException {
		return ptBC.getByProject(id);
	}
	
	//------------------------get all skill
	public Skill[] getAllSkill() throws DAOException, NamingException {
		return sBC.getByAll();
	}
	
	//----------add skill
	public void addSkill(long id, Skill skill) throws DAOException, NamingException {
		userBC.addSkill(userBC.getById(id), skill);
	}
}
