package eu.tasgroup.gestione.businesscomponent.facade;

import java.util.List;

import javax.naming.NamingException;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.ProjectBC;
import eu.tasgroup.gestione.businesscomponent.UserBC;
import eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli;
import eu.tasgroup.gestione.businesscomponent.model.Project;
import eu.tasgroup.gestione.businesscomponent.model.Role;
import eu.tasgroup.gestione.businesscomponent.model.User;

public class ProjectManagerFacade {

	private static ProjectManagerFacade pmf;
	private UserBC userBC;
	private ProjectBC projectBC;

	private ProjectManagerFacade() throws DAOException, NamingException {
		userBC = new UserBC();
		projectBC = new ProjectBC();
	}

	public static ProjectManagerFacade getIstance() throws DAOException, NamingException {
		if (pmf == null)
			pmf = new ProjectManagerFacade();

		return pmf;
	}

	/*------------------------Crea o modifica il Project Manager*/
	public User createOrUpdateCliente(User user) throws DAOException, NamingException {
		User created = userBC.createOrUpdate(user);
		if (user.getId() == 0) {
			Role role = new Role();
			role.setRole(Ruoli.PROJECT_MANAGER);
			userBC.addRole(created, role);
		}
		return created;
	}

	/*-------------------------------Project Manager in base all'id*/
	public User getById(long id) throws DAOException, NamingException {
		return userBC.getById(id);
	}

	/*-------------------------------Project Manager in base allo username*/
	public User getByUsername(String username) throws DAOException, NamingException {
		return userBC.getByUsername(username);
	}

	/*-------------------------------Project Manager in base alla email*/
	public User getByEmail(String email) throws DAOException, NamingException {
		return userBC.getByEmail(email);
	}

	// TODO: lista di tutti i dipendenti
	// TODO: lista dei dipendenti per determinate skill
	// TODO: lista dei dipendenti non assegnati

	/*------------------------------- Gestione dei progetti --------------------------------*/

	/*------------------------------- Crea o aggiorna un progetto */
	public Project createOrUpdateProject(Project project) throws DAOException, NamingException {
		return projectBC.createOrUpdate(project);
	}

	/*------------------------------------Percentuale completamento progetto in base all'id*/
	public int getPercentualeCompletamentoProjectID(long id) throws DAOException, NamingException {
		return projectBC.getPercentualeCompletamento(id);
	}

	/*--------------------------------------Progetto in base all'ID*/
	public Project getProjectById(long id) throws DAOException, NamingException {
		return projectBC.getById(id);
	}

	/*--------------------------------------Progetti associati al Project Manager*/
	public List<Project> getProjectsByCliente(User user) throws DAOException, NamingException {
		return projectBC.getListProjectByResponsabile(user.getId());
	}
	
	/*------------------------------- Gestione delle task --------------------------------*/
}
