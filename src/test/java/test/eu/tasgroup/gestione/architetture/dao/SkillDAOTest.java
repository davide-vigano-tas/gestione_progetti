package test.eu.tasgroup.gestione.architetture.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;

import javax.naming.NamingException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import eu.tasgroup.gestione.architecture.dbaccess.DBAccess;
import eu.tasgroup.gestione.architetture.dao.SkillDAO;
import eu.tasgroup.gestione.architetture.dao.UserDAO;
import eu.tasgroup.gestione.businesscomponent.enumerated.Skills;
import eu.tasgroup.gestione.businesscomponent.model.Skill;
import eu.tasgroup.gestione.businesscomponent.model.User;
import test.eu.tasgroup.gestione.DBAccessContext;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SkillDAOTest {
	
	private Connection conn;
	private static Skill skill;
	private static User user1;
	

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
		user1 = new User();
		user1.setNome("Sam");
		user1.setCognome("Mast");
		user1.setUsername("sammast");
		user1.setPassword("pass");
		user1.setEmail("sam@gmail.com");
		
		skill = new Skill();
		skill.setTipo(Skills.JAVA17);
	}

	@BeforeEach
    void setUp() throws NamingException, ClassNotFoundException {
    	DBAccessContext.setDBAccessContext();
    }

	@Test
	@Order(1)
	void testCreateAndGetByTipo() {
		try {
			conn = DBAccess.getConnection();
			UserDAO.getFactory().create(conn, user1);
			user1 = UserDAO.getFactory().getByUsername(conn, user1.getUsername());
			
			Skill[] skills1 = SkillDAO.getFactory().getByTipo(conn, Skills.JAVA17);
			
			SkillDAO.getFactory().create(conn, skill);
			
			Skill[] skills2 = SkillDAO.getFactory().getByTipo(conn, Skills.JAVA17);
			
			assertTrue(skills1.length == skills2.length-1, "Skill non aggiunto correttamente");
			
			skill = skills2[0];
			
			assertNotNull(skill, "Skill non deve essere null");
			
			
			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}
	
	@Test
	@Order(2)
	void testGetById() {
		try {
			conn = DBAccess.getConnection();
			
			skill = SkillDAO.getFactory().getById(conn, skill.getId());
			
			assertNotNull(skill, "Skill non deve essere null");
			
			
			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}
	
	@Test
	@Order(3)
	void testGetAll() {
		try {
			conn = DBAccess.getConnection();
			
			Skill[] skills2 = SkillDAO.getFactory().getAll(conn);
			
			assertTrue(skills2.length >= 1, "Skill non aggiunto correttamente");
			
			
			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}
	

	
	@Test
	@Order(4)
	void testDelete() {
		try {
			conn = DBAccess.getConnection();
			
			SkillDAO.getFactory().delete(conn, skill.getId());
			UserDAO.getFactory().delete(conn, user1.getId());
			skill = SkillDAO.getFactory().getById(conn, skill.getId());
			
			assertNull(skill, "Skill deve essere null");
			
			
			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}

}
