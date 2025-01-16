package eu.tasgroup.gestione.businesscomponent;

import java.sql.Connection;
import java.util.List;

import javax.naming.NamingException;

import eu.tasgroup.gestione.architecture.dbaccess.DBAccess;
import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.architetture.dao.ProjectDAO;
import eu.tasgroup.gestione.businesscomponent.enumerated.StatoProgetto;
import eu.tasgroup.gestione.businesscomponent.model.Project;

public class ProjectBC {
	private Connection conn;
	private ProjectDAO projectDAO;

	public ProjectBC() throws DAOException, NamingException {
		conn = DBAccess.getConnection();
		projectDAO = ProjectDAO.getFactory();
	}

	public Project createOrUpdate(Project project) throws DAOException {
		try {
			if (projectDAO.getById(conn, project.getId()) != null) {
				projectDAO.update(conn, project);
				return projectDAO.getById(conn, project.getId());
			} else {
				projectDAO.create(conn, project);

				// Restituisco elemento appena creato
				Project newProject = new Project();
				Project[] projects = projectDAO.getAll(conn);

				newProject = projects[projects.length - 1];

				for (int i = 0; i < projects.length - 1; i++) {
					if (projects[i].getId() > newProject.getId())
						newProject = projects[i];
				}

				return newProject;
			}

		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public void delete(long id) throws DAOException {
		try {
			projectDAO.delete(conn, id);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public int getPercentualeCompletamento(long id) throws DAOException {
		try {
			return projectDAO.getCompletamentoByProjectID(conn, id);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public Project getById(long id) throws DAOException {
		try {
			return projectDAO.getById(conn, id);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public Project[] getAll() throws DAOException {
		try {
			return projectDAO.getAll(conn);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public List<Project> getListProjectByStatus(StatoProgetto stato) throws DAOException {
		try {
			return projectDAO.getListProjectByStatus(conn, stato);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}
	
	public List<Project> getListProjectByCliente(long id) throws DAOException {
		try {
			return projectDAO.getListProjectByCliente(conn, id);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}
	
	public List<Project> getListProjectByResponsabile(long id) throws DAOException {
		try {
			return projectDAO.getListProjectByResponsabile(conn, id);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

}
