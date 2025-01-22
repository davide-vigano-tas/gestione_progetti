package eu.tasgroup.gestione.businesscomponent;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Date;

import javax.naming.NamingException;

import eu.tasgroup.gestione.architecture.dbaccess.DBAccess;
import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.architetture.dao.RoleDAO;
import eu.tasgroup.gestione.architetture.dao.SkillDAO;
import eu.tasgroup.gestione.architetture.dao.TicketDAO;
import eu.tasgroup.gestione.architetture.dao.UserDAO;
import eu.tasgroup.gestione.architetture.dao.UserSkillDAO;
import eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli;
import eu.tasgroup.gestione.businesscomponent.model.Role;
import eu.tasgroup.gestione.businesscomponent.model.Skill;
import eu.tasgroup.gestione.businesscomponent.model.Ticket;
import eu.tasgroup.gestione.businesscomponent.model.User;
import eu.tasgroup.gestione.businesscomponent.model.UserSkill;

public class TicketBC {
	private Connection conn;
	private TicketDAO ticketDAO;
	
	
	public TicketBC() throws DAOException {
		ticketDAO = TicketDAO.getFactory();
	}
	
	
	public void createOrUpdate(Ticket ticket) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			if(ticket.getId() == 0) ticketDAO.create(conn, ticket);
			else ticketDAO.update(conn, ticket);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}
	
	public Ticket[] getAll() throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			return ticketDAO.getAll(conn);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}
	
	public Ticket getById(long id) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			return ticketDAO.getById(conn, id);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}


	
	public Ticket[] getByDipendente(long id) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			return ticketDAO.getByDipendente(conn, id);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}
	

	
	
	public void updateTicket(Ticket ticket) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			ticketDAO.update(conn, ticket);
			
		} finally {
			DBAccess.closeConnection(conn);
		}
	}
	
	
	public Ticket[] getAllOpen() throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			return ticketDAO.getAllOpen(conn);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}
	
	public Ticket[] getAllClosed() throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			return ticketDAO.getAllClosed(conn);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}
	
	
	public void deleteTicket(long id) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			ticketDAO.delete(conn, id);
		} finally {
			DBAccess.closeConnection(conn);
		}	
	}
	
	public void closeTicket(long id) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			ticketDAO.closeTicket(conn, id, new Date());
		} finally {
			DBAccess.closeConnection(conn);
		}	
	}

}
