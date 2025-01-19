package test.eu.tasgroup.gestione.businesscomponent;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

import javax.naming.NamingException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import eu.tasgroup.gestione.architecture.dbaccess.DBAccess;
import eu.tasgroup.gestione.architetture.dao.RoleDAO;
import eu.tasgroup.gestione.architetture.dao.SkillDAO;
import eu.tasgroup.gestione.businesscomponent.UserBC;
import eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli;
import eu.tasgroup.gestione.businesscomponent.enumerated.Skills;
import eu.tasgroup.gestione.businesscomponent.model.Role;
import eu.tasgroup.gestione.businesscomponent.model.Skill;
import eu.tasgroup.gestione.businesscomponent.model.User;
import test.eu.tasgroup.gestione.DBAccessContext;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserBCTest {
	
	private static User user;
	private static Skill skill;
	private UserBC ubc;
	private static Role role;
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		user = new User();
		user.setNome("Sam");
		user.setCognome("Mast");
		user.setUsername("sammast");
		user.setPassword("pass");
		user.setEmail("sam@gmail.com");
		
		skill = new Skill();
		skill.setTipo(Skills.JAVA17);
		
		role = new Role();
	
		role.setRole(Ruoli.ADMIN);
		
	}

	@BeforeEach
    void setUp() throws NamingException, ClassNotFoundException {
    	DBAccessContext.setDBAccessContext();
    	
    }

	@Test
	@Order(1)
	void testAddSkill() {
		try {
			ubc = new UserBC();
			user = ubc.createOrUpdate(user);
			Connection conn = DBAccess.getConnection();
			SkillDAO.getFactory().create(conn, skill);
			Skill[] skills = SkillDAO.getFactory().getByTipo(conn, Skills.JAVA17);
			skill = skills[0];
			ubc.addSkill(user, skill);
			
			User[] usBysk = ubc.getBySkill(skill);
			assertTrue(usBysk.length == 1, "Deve contenere una skill");
			
			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}

	@Test
	@Order(2)
	void testAddRole() {
		try {
			ubc = new UserBC();
			role.setIdUser(user.getId());
			ubc.addRole(user, role);
			Connection conn = DBAccess.getConnection();
			Ruoli[] roles = RoleDAO.getFactory().getByUserId(conn, user.getId());
			DBAccess.closeConnection(conn);
			User[] users = ubc.getByRole(Ruoli.ADMIN);
			assertTrue(roles.length == 1, "Lunghezza errata");
			assertTrue(users.length ==1);
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}
	

	@Test
	@Order(3)
	void testUpdateRoleAndGetByUser() {
		try {
			ubc = new UserBC();
			System.out.println(role);
			ubc.updateRole(role, Ruoli.DIPENDENTE);
			Role[] roles = ubc.getRolesById(user.getId());
			assertTrue(roles.length == 1, "Lunghezza errata");
			roles = ubc.getRolesByUsername(user.getUsername());
			assertTrue(roles.length == 1, "Lunghezza errata");
			role = roles[0];
			
			assertEquals(Ruoli.DIPENDENTE, role.getRole(), "Ruolo non aggiornato");
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}
	
	
	@Test
	@Order(3)
	void testDelete() {
		try {
			ubc = new UserBC();
			
			ubc.deleteRole(Ruoli.DIPENDENTE, user);
			
			Connection conn = DBAccess.getConnection();
			
			Role[] roles = ubc.getRolesById(user.getId());
			assertTrue(roles.length == 0, "Lunghezza errata");
			SkillDAO.getFactory().delete(conn, skill.getId());
			DBAccess.closeConnection(conn);
			
			ubc.delete(user);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}
}
