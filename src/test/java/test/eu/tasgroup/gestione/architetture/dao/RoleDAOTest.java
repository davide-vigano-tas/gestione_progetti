package test.eu.tasgroup.gestione.architetture.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import java.util.Arrays;

import javax.naming.NamingException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import eu.tasgroup.gestione.architecture.dbaccess.DBAccess;
import eu.tasgroup.gestione.architetture.dao.RoleDAO;
import eu.tasgroup.gestione.architetture.dao.UserDAO;
import eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli;
import eu.tasgroup.gestione.businesscomponent.model.Role;
import eu.tasgroup.gestione.businesscomponent.model.User;
import test.eu.tasgroup.gestione.DBAccessContext;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RoleDAOTest {
	
	private Connection conn;
	private static User user1;
	private static Role role1;
	private static Role role2;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
		user1 = new User();
		user1.setNome("Sam");
		user1.setCognome("Mast");
		user1.setUsername("sammast");
		user1.setPassword("pass");
		user1.setEmail("sam@gmail.com");
		
		
		role1 = new Role();
		role1.setRole(Ruoli.DIPENDENTE);
		
		
		role2 = new Role();
		role2.setRole(Ruoli.PROJECT_MANAGER);
	}
    @BeforeEach
    void setUp() throws NamingException, ClassNotFoundException {
    	DBAccessContext.setDBAccessContext();
    }

	@Test
	@Order(1)
	void testCreateAndGetByUserId() {
		try {
			conn = DBAccess.getConnection();
			UserDAO.getFactory().create(conn, user1);
			user1 = UserDAO.getFactory().getByUsername(conn, user1.getUsername());
			role1.setIdUser(user1.getId());
			role2.setIdUser(user1.getId());
			
			RoleDAO.getFactory().create(conn, role1);
			RoleDAO.getFactory().create(conn, role2);
			
			Ruoli[] roles = RoleDAO.getFactory().getByUserId(conn, user1.getId());
			assertNotNull(roles, "Elenco ruoli non deve essere nullo");
			assertTrue(roles.length >= 2, "Almeno due ruoli");
			
			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}
	
	@Test
	@Order(2)
	void testGetByUserUsername() {
		try {
			conn = DBAccess.getConnection();
			
			Ruoli[] roles = RoleDAO.getFactory().getByUsername(conn, user1.getUsername());
			
			assertNotNull(roles, "Elenco ruoli non deve essere nullo");
			assertTrue(roles.length >= 2, "Almeno due ruoli");

			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}
	
	@Test
	@Order(3)
	void testUpdate() {
		try {
			conn = DBAccess.getConnection();
			
			role1.setRole(Ruoli.PROJECT_MANAGER);
			RoleDAO.getFactory().update(conn, role1, Ruoli.ADMIN);
			
			Ruoli[] roles = RoleDAO.getFactory().getByUsername(conn, user1.getUsername());
			assertNotNull(roles, "Elenco ruoli non deve essere nullo");
			assertTrue(roles.length >= 2, "Almeno due ruoli");
			assertTrue(Arrays.asList(roles).contains(Ruoli.ADMIN), "Deve contenere ADMIN");
			
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
			
			RoleDAO.getFactory().delete(conn,  Ruoli.ADMIN, user1.getId());
			RoleDAO.getFactory().delete(conn,  Ruoli.DIPENDENTE, user1.getId());
			UserDAO.getFactory().delete(conn, user1.getId());
			Ruoli[] roles = RoleDAO.getFactory().getByUsername(conn, user1.getUsername());
			assertNotNull(roles, "Elenco ruoli non deve essere nullo");
			assertTrue(roles.length == 0, "Non deve avere ruoli");
		
			
			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}
	


}
