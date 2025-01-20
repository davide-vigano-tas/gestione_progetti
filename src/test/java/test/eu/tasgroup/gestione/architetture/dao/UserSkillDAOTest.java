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
import eu.tasgroup.gestione.architetture.dao.UserSkillDAO;
import eu.tasgroup.gestione.businesscomponent.enumerated.Skills;
import eu.tasgroup.gestione.businesscomponent.model.Skill;
import eu.tasgroup.gestione.businesscomponent.model.User;
import eu.tasgroup.gestione.businesscomponent.model.UserSkill;
import test.eu.tasgroup.gestione.DBAccessContext;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserSkillDAOTest {
	
	private Connection conn;
	private static User user1;
	private static Skill skill;
	private static UserSkill uk;
	

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
		user1 = new User();
		user1.setNome("Sam");
		user1.setCognome("Mast");
		user1.setUsername("sammast");
		user1.setPassword("pass");
		user1.setEmail("sam@gmail.com");
		
		skill = new Skill();
		skill.setTipo(Skills.ANGULAR);
		
		uk = new UserSkill();
	}

	@BeforeEach
    void setUp() throws NamingException, ClassNotFoundException {
    	DBAccessContext.setDBAccessContext();
    }
	@Test
	@Order(1)
	void testCreate() {
		try {
			conn = DBAccess.getConnection();
			UserDAO.getFactory().create(conn, user1);
			user1 = UserDAO.getFactory().getByUsername(conn, user1.getUsername());
		
			
			SkillDAO.getFactory().create(conn, skill);
			
			Skill[] skills2 = SkillDAO.getFactory().getByTipo(conn, Skills.ANGULAR);
			
			
			skill = skills2[0];
			
			uk.setIdCompetenze(skill.getId());
			uk.setIdUtente(user1.getId());
			
			UserSkillDAO.getFactory().create(conn, uk);
			
			
			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}
	
	@Test
	@Order(2)
	void testGetAll() {
		try {
			conn = DBAccess.getConnection();
			UserSkill[] usk = UserSkillDAO.getFactory().getAll(conn);
			assertTrue(usk.length >= 1, "Almeno un elemento");
			for(UserSkill userskill : usk) {
				assertNotNull(userskill, "Non deve contenere elementi nulli");
				if(userskill.getIdCompetenze()==uk.getIdCompetenze() &&
						userskill.getIdUtente() == uk.getIdUtente()) {
					uk = userskill;
					break;
				}
			}
			assertTrue(uk.getId() != 0, "Id non valido");
			
			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}
	
	@Test
	@Order(3)
	void testGetById() {
		try {
			conn = DBAccess.getConnection();
			uk = UserSkillDAO.getFactory().getById(conn, uk.getId());
			assertNotNull(uk, "Elemento non deve essere nullo");
			
			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}
	
	@Test
	@Order(4)
	void testGetSkillByUser() {
		try {
			conn = DBAccess.getConnection();
			Skill[] skills = SkillDAO.getFactory().getByUser(conn, user1.getId());
			assertTrue(skills.length == 1, "Lunghezza errata");
			
			
			
			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}
	
	@Test
	@Order(5)
	void testGetUserBySkill() {
		try {
			conn = DBAccess.getConnection();
			User[] users = UserDAO.getFactory().getBySkill(conn, skill.getTipo());
			assertTrue(users.length >= 1, "Lunghezza errata: almeno 1");
			
			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}
	
	@Test
	@Order(5)
	void testDelete() {
		try {
			conn = DBAccess.getConnection();
			UserSkillDAO.getFactory().delete(conn, uk.getId());
			uk = UserSkillDAO.getFactory().getById(conn, uk.getId());
			
			SkillDAO.getFactory().delete(conn, skill.getId());
			UserDAO.getFactory().delete(conn, user1.getId());
			
			assertNull(uk, "Elemento deve essere nullo");
			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}

}
