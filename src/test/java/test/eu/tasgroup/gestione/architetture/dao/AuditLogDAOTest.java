package test.eu.tasgroup.gestione.architetture.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import java.util.Date;

import javax.naming.NamingException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import eu.tasgroup.gestione.architecture.dbaccess.DBAccess;
import eu.tasgroup.gestione.architetture.dao.AuditLogDAO;
import eu.tasgroup.gestione.businesscomponent.model.AuditLog;
import test.eu.tasgroup.gestione.DBAccessContext;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuditLogDAOTest {

	private Connection conn;
	private static AuditLog al;
	private static AuditLog al2;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		al = new AuditLog();
		al.setUtente("Marco");
		al.setOperazione("Task x completata");
		al.setData(new Date());
		al2 = new AuditLog();
		al2.setUtente("Paolo");
		al2.setOperazione("LICENZIATO");
		al2.setData(new Date());
	}
	
    @BeforeEach
    void setUp() throws NamingException, ClassNotFoundException {
    	DBAccessContext.setDBAccessContext();
    }

	@Test
	@Order(1)
	void testCreateGet() {
		try {
			conn = DBAccess.getConnection();
			
			AuditLogDAO.getFactory().create(conn, al);
			AuditLogDAO.getFactory().create(conn, al2);
			
			AuditLog[] als = AuditLogDAO.getFactory().getAll(conn);
			assertNotEquals(als[0], als[1]);
			assertEquals(2, als.length, "la dimensione deve essere di 2");
			
			assertEquals(al.getUtente(), AuditLogDAO.getFactory().getById(conn,als[0].getId()).getUtente(),"gli utenti del log devono coincidere");
			
			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}
	
	@Test
	@Order(2)
	void testUpdate() {
		try {
			conn = DBAccess.getConnection();
			
			AuditLog[] als = AuditLogDAO.getFactory().getAll(conn);
			AuditLog alSupp = new AuditLog();
			alSupp.setId(als[0].getId());
			alSupp.setUtente("il gatto davanti casa");
			alSupp.setOperazione(als[0].getOperazione());
			alSupp.setData(als[0].getData());
			
			
			AuditLogDAO.getFactory().update(conn, alSupp);
			assertNotEquals(als[0], AuditLogDAO.getFactory().getById(conn, als[0].getId()),"il nuovo log deve essere diverso da quello vecchio");
			assertEquals(alSupp, AuditLogDAO.getFactory().getById(conn, als[0].getId()), "i log devono coincidere");
			
			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}
	
	@Test
	@Order(3)
	void testDelete() {
		try {
			conn = DBAccess.getConnection();
			
			AuditLog[] als = AuditLogDAO.getFactory().getAll(conn);
			
			AuditLogDAO.getFactory().delete(conn, als[0].getId());
			AuditLogDAO.getFactory().delete(conn, als[1].getId());
			
			als = AuditLogDAO.getFactory().getAll(conn);
			
			assertTrue(als.length == 0, "la dimensionde deve essere 0");
		
			
			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}
	

}
