package eu.tasgroup.gestione.businesscomponent;

import java.sql.Connection;
import java.util.List;

import javax.naming.NamingException;

import eu.tasgroup.gestione.architecture.dbaccess.DBAccess;
import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.architetture.dao.TimesheetDAO;
import eu.tasgroup.gestione.businesscomponent.model.Timesheet;

public class TimesheetBC {

	private Connection conn;
	private TimesheetDAO timesheetDAO;

	public TimesheetBC() throws DAOException, NamingException {
		conn = DBAccess.getConnection();
		timesheetDAO = TimesheetDAO.getFactory();
	}

	public void createOrUpdate(Timesheet timesheet) throws DAOException {
		try {
			if (timesheetDAO.getById(conn, timesheet.getId()) != null)
				timesheetDAO.update(conn, timesheet);
			else
				timesheetDAO.create(conn, timesheet);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public void delete(long id) throws DAOException {
		try {
			timesheetDAO.delete(conn, id);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public void getById(long id) throws DAOException {
		try {
			timesheetDAO.getById(conn, id);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public void approva(long id, boolean stato) throws DAOException {
		try {
			timesheetDAO.approva(conn, id, stato);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public Timesheet[] getAll() throws DAOException {
		try {
			return timesheetDAO.getAll(conn);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public List<Timesheet> getListByDipendente(long id) throws DAOException {
		try {
			return timesheetDAO.getByDipendente(conn, id);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public List<Timesheet> getListByProject(long id) throws DAOException {
		try {
			return timesheetDAO.getByProject(conn, id);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

}
