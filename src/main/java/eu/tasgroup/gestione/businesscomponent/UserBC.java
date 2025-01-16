package eu.tasgroup.gestione.businesscomponent;

import java.sql.Connection;
import java.util.Arrays;

import javax.naming.NamingException;

import eu.tasgroup.gestione.architecture.dbaccess.DBAccess;
import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.architetture.dao.RoleDAO;
import eu.tasgroup.gestione.architetture.dao.SkillDAO;
import eu.tasgroup.gestione.architetture.dao.UserDAO;
import eu.tasgroup.gestione.architetture.dao.UserSkillDAO;
import eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli;
import eu.tasgroup.gestione.businesscomponent.model.Role;
import eu.tasgroup.gestione.businesscomponent.model.Skill;
import eu.tasgroup.gestione.businesscomponent.model.User;
import eu.tasgroup.gestione.businesscomponent.model.UserSkill;

public class UserBC {
	private Connection conn;
	private UserDAO userDAO;
	private UserSkillDAO userSkillDAO;
	private SkillDAO skillDAO;
	private RoleDAO roleDAO;
	
	
	public UserBC() throws DAOException {
		userDAO = UserDAO.getFactory();
		userSkillDAO = UserSkillDAO.getFactory();
		skillDAO = SkillDAO.getFactory();
		roleDAO = RoleDAO.getFactory();
	}
	
	
	public User createOrUpdate(User user) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			if(user.getId() == 0) userDAO.create(conn, user);
			else userDAO.update(conn, user);
			return userDAO.getByUsername(conn, user.getUsername());
		} finally {
			DBAccess.closeConnection(conn);
		}
	}
	
	public User[] getAll() throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			return userDAO.getAll(conn);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}
	
	public User getById(long id) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			return userDAO.getById(conn, id);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public User getByUsername(String username) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			return userDAO.getByUsername(conn, username);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}
	
	public User getByEmail(String email) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			return userDAO.getByUsername(conn, email);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}
	
	public User[] getBySkill(Skill skill) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			return userDAO.getBySkill(conn, skill.getTipo());
		} finally {
			DBAccess.closeConnection(conn);
		}
	}
	
	public void addSkill(User user, Skill skill) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			Skill[] sk = skillDAO.getByUser(conn, user.getId());
			for(Skill s : sk) {
				if(s.getTipo() == skill.getTipo()) skill = s;
			}
			UserSkill us = new UserSkill();
			us.setIdCompetenze(skill.getId());
			us.setIdUtente(user.getId());
			userSkillDAO.create(conn, us);
			
			
		} finally {
			DBAccess.closeConnection(conn);
		}
	}
	
	public void addRole(User user, Role role) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			Ruoli[] roles = roleDAO.getByUserId(conn, user.getId());
			if(!Arrays.asList(roles).contains(role.getRole())) {
				role.setIdUser(user.getId());
				roleDAO.create(conn, role);
			}
			
			
		} finally {
			DBAccess.closeConnection(conn);
		}
	}
	public void delete(User user) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			userDAO.delete(conn, user.getId());
		} finally {
			DBAccess.closeConnection(conn);
		}
	}
}
