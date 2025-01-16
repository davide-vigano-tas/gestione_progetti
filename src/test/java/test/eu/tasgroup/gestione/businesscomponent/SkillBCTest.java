package test.eu.tasgroup.gestione.businesscomponent;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import javax.naming.NamingException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.SkillBC;
import eu.tasgroup.gestione.businesscomponent.UserBC;
import eu.tasgroup.gestione.businesscomponent.enumerated.Skills;
import eu.tasgroup.gestione.businesscomponent.model.Skill;
import eu.tasgroup.gestione.businesscomponent.model.User;
import test.eu.tasgroup.gestione.DBAccessContext;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SkillBCTest {

	private static Skill skill;
	private static User user1;
	private UserBC uBC;
	private SkillBC sBC;
	

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
    void setUp() throws NamingException, ClassNotFoundException, DAOException {
    	DBAccessContext.setDBAccessContext();
    	uBC = new UserBC();
    	sBC = new SkillBC();
    }

	@Test
	@Order(1)
	void testCreateAndGetByTipo() {
		try {
			user1 = uBC.createOrUpdate(user1);
			
			Skill[] skills1 = sBC.getByTipo(Skills.JAVA17);
			
			sBC.create(skill);
			
			Skill[] skills2 = sBC.getByTipo(Skills.JAVA17);
			
			assertTrue(skills1.length == skills2.length-1, "Skill non aggiunto correttamente");
			
			skill = skills2[0];
			
			assertNotNull(skill, "Skill non deve essere null");
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}
	
	@Test
	@Order(2)
	void testGetById() {
		try {
			skill = sBC.getByID(skill.getId());
			
			assertNotNull(skill, "Skill non deve essere null");
			
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}
	
	@Test
	@Order(3)
	void testGetAll() {
		try {
			
			
			Skill[] skills2 = sBC.getByAll();
			
			assertTrue(skills2.length >= 1, "Skill non aggiunto correttamente");
			
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}
	

	
	@Test
	@Order(4)
	void testDelete() {
		try {
			sBC.delete(skill.getId());
			uBC.delete(user1);
			skill = sBC.getByID(skill.getId());
			
			assertNull(skill, "Skill deve essere null");
			
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}

}
