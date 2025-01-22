package test.eu.tasgroup.gestione.architetture.dao;

import static org.junit.jupiter.api.Assertions.*;

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
import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.architetture.dao.TicketDAO;
import eu.tasgroup.gestione.architetture.dao.UserDAO;
import eu.tasgroup.gestione.businesscomponent.model.Ticket;
import eu.tasgroup.gestione.businesscomponent.model.User;
import test.eu.tasgroup.gestione.DBAccessContext;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TicketDAOTest {
	private Connection conn;
	private static User dipendente;
	private static Ticket ticket;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		dipendente = new User();
		dipendente.setNome("Sam");
		dipendente.setCognome("Mast");
		dipendente.setUsername("sammast-test");
		dipendente.setPassword("pass");
		dipendente.setEmail("samtest@gmail.com");
		
		
		ticket = new Ticket();
		ticket.setCreated_at(new Date());
		ticket.setTitle("Oracle connection");
		ticket.setDescription("I cant access the remote db connection.");
	}

	@BeforeEach
    void setUp() throws NamingException, ClassNotFoundException {
    	DBAccessContext.setDBAccessContext();
    }

	@Test
	@Order(1)
	void testCreateAndGetByDipendente() {
		try {
			conn = DBAccess.getConnection();
			UserDAO.getFactory().create(conn, dipendente);
			dipendente = UserDAO.getFactory().getByUsername(conn, "sammast-test");
			ticket.setOpener(dipendente.getId());
			TicketDAO.getFactory().create(conn, ticket);
			Ticket[] byDip = TicketDAO.getFactory().getByDipendente(conn, dipendente.getId());
			assertEquals(1, byDip.length, "Lunghezza non corretta");
			ticket = byDip[0];
			assertNotNull(ticket, "Ticket non deve essere null");
			
			
			DBAccess.closeConnection(conn);
		} catch (DAOException | NamingException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	@Order(2)
	void testGetByIdAndGetAll() {
		try {
			conn = DBAccess.getConnection();
	
			ticket = TicketDAO.getFactory().getById(conn, ticket.getId());
			assertNotNull(ticket, "Ticket non deve essere null");
			Ticket[] all = TicketDAO.getFactory().getAll(conn);
			assertTrue(all.length >= 1, "Lunghezza errata");
			
			DBAccess.closeConnection(conn);
		} catch (DAOException | NamingException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	@Order(3)
	void testGetOpens() {
		try {
			conn = DBAccess.getConnection();
	
			
			Ticket[] all = TicketDAO.getFactory().getAllOpen(conn);
			assertTrue(all.length >= 1, "Lunghezza errata");
			
			DBAccess.closeConnection(conn);
		} catch (DAOException | NamingException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	
	@Test
	@Order(4)
	void testClosed() {
		try {
			conn = DBAccess.getConnection();
	
			
			TicketDAO.getFactory().closeTicket(conn, ticket.getId(), new Date(new Date().getTime()+100000));
			ticket = TicketDAO.getFactory().getById(conn, ticket.getId());
			assertNotNull(ticket, "Ticket non deve essere null");
			assertNotNull(ticket.getClosed_at(), "Deve essere chiuso");
			
			DBAccess.closeConnection(conn);
		} catch (DAOException | NamingException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@Test
	@Order(5)
	void testGetClosed() {
		try {
			conn = DBAccess.getConnection();
	
			
			Ticket[] all = TicketDAO.getFactory().getAllClosed(conn);
			assertTrue(all.length >= 1, "Lunghezza errata");
			
			DBAccess.closeConnection(conn);
		} catch (DAOException | NamingException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	@Order(6)
	void testDelete() {
		try {
			conn = DBAccess.getConnection();
	
			TicketDAO.getFactory().delete(conn, ticket.getId());
			ticket = TicketDAO.getFactory().getById(conn, ticket.getId());
			assertNull(ticket, "Deve essere null");
			UserDAO.getFactory().delete(conn, dipendente.getId());
			
			DBAccess.closeConnection(conn);
		} catch (DAOException | NamingException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
