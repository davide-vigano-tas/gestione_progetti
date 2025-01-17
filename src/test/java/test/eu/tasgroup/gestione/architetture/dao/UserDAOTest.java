package test.eu.tasgroup.gestione.architetture.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import eu.tasgroup.gestione.architetture.dao.RoleDAO;
import eu.tasgroup.gestione.architetture.dao.UserDAO;
import eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli;
import eu.tasgroup.gestione.businesscomponent.model.Role;
import eu.tasgroup.gestione.businesscomponent.model.User;
import test.eu.tasgroup.gestione.DBAccessContext;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDAOTest {
	
	private Connection conn;
	private static User user1;
	private static User user2;
	private static Role role;
	

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
		user1 = new User();
		user1.setNome("Sam");
		user1.setCognome("Mast");
		user1.setUsername("sammast");
		user1.setPassword("pass");
		user1.setEmail("sam@gmail.com");
		
		
		user2 = new User();
		user2.setNome("Rob");
		user2.setCognome("Bru");
		user2.setUsername("robbru");
		user2.setPassword("pass");
		user2.setEmail("rob@gmail.com");
		
		role = new Role();
		role.setRole(Ruoli.DIPENDENTE);
	
	}

    @BeforeEach
    void setUp() throws NamingException, ClassNotFoundException {
    	DBAccessContext.setDBAccessContext();
    }

	@Test
	@Order(1)
	void testCreateAndGetByUsername() {
		try {
			conn = DBAccess.getConnection();
			UserDAO.getFactory().create(conn, user1);
			UserDAO.getFactory().create(conn, user2);
			user1 = UserDAO.getFactory().getByUsername(conn, user1.getUsername());
			user2 = UserDAO.getFactory().getByUsername(conn, user2.getUsername());
			role.setIdUser(user1.getId());
			RoleDAO.getFactory().create(conn, role);
			DBAccess.closeConnection(conn);
			assertNotNull(user1, "User1 non dovrebbe essere null");
			assertNotNull(user2, "User2 non dovrebbe essere null");
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}
	
	@Test
	@Order(2)
	void getById() {
		try {
			conn = DBAccess.getConnection();
			user1 = UserDAO.getFactory().getById(conn, user1.getId());
			user2 = UserDAO.getFactory().getById(conn, user2.getId());
			assertNotNull(user1, "User1 non dovrebbe essere null");
			assertNotNull(user2, "User2 non dovrebbe essere null");
			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}
	
	@Test
	@Order(3)
	void getByEmail() {
		try {
			conn = DBAccess.getConnection();
			user1 = UserDAO.getFactory().getByEmail(conn, user1.getEmail());
			user2 = UserDAO.getFactory().getByEmail(conn, user2.getEmail());
			assertNotNull(user1, "User1 non dovrebbe essere null");
			assertNotNull(user2, "User2 non dovrebbe essere null");
			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}
	
	@Test
	@Order(4)
	void testUpdate() {
		try {
			conn = DBAccess.getConnection();
			user1.setNome("Samuel");
			user2.setNome("Roberto");
			
			UserDAO.getFactory().update(conn, user1);
			UserDAO.getFactory().update(conn, user2);
			
			user1 = UserDAO.getFactory().getByEmail(conn, user1.getEmail());
			user2 = UserDAO.getFactory().getByEmail(conn, user2.getEmail());
			assertNotNull(user1, "User1 non dovrebbe essere null");
			assertNotNull(user2, "User2 non dovrebbe essere null");
			assertEquals("Samuel", user1.getNome(), "Nome non aggiornato correttamente");
			assertEquals("Roberto", user2.getNome(), "Nome non aggiornato correttamente");
			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}
	
	@Test
	@Order(5)
	void testDipendentiNonAssegnati() {
		try {
			conn = DBAccess.getConnection();
			User[] dip = UserDAO.getFactory().getDipendentiNonAssegnati(conn);
			assertTrue(dip.length == 1, "Lunghezza Errata");
			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}

	
	@Test
	@Order(6)
	void testDelete() {
		try {
			conn = DBAccess.getConnection();
			RoleDAO.getFactory().delete(conn, Ruoli.DIPENDENTE, user1.getId());
			UserDAO.getFactory().delete(conn, user1.getId());
			UserDAO.getFactory().delete(conn, user2.getId());
			
			user1 = UserDAO.getFactory().getByEmail(conn, user1.getEmail());
			user2 = UserDAO.getFactory().getByEmail(conn, user2.getEmail());
			assertNull(user1, "User1 dovrebbe essere null");
			assertNull(user2, "User2 dovrebbe essere null");
			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}

}
