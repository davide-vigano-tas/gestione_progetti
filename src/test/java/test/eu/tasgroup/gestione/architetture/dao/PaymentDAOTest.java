package test.eu.tasgroup.gestione.architetture.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import eu.tasgroup.gestione.architecture.dbaccess.DBAccess;
import eu.tasgroup.gestione.architetture.dao.PaymentDAO;
import eu.tasgroup.gestione.architetture.dao.ProjectDAO;
import eu.tasgroup.gestione.architetture.dao.UserDAO;
import eu.tasgroup.gestione.businesscomponent.model.Payment;
import eu.tasgroup.gestione.businesscomponent.model.Project;
import eu.tasgroup.gestione.businesscomponent.model.User;
import test.eu.tasgroup.gestione.DBAccessContext;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PaymentDAOTest {
	
	private Connection conn;
	private static User cliente;
	private static User projectmanager;
	private static Project project;
	private static Payment payment;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
		cliente = new User();
		cliente.setNome("Sam");
		cliente.setCognome("Mast");
		cliente.setUsername("sammast");
		cliente.setPassword("pass");
		cliente.setEmail("sam@gmail.com");
		
		
		projectmanager = new User();
		projectmanager.setNome("Rob");
		projectmanager.setCognome("Bru");
		projectmanager.setUsername("robbru");
		projectmanager.setPassword("pass");
		projectmanager.setEmail("rob@gmail.com");
		
		
		project = new Project();
		project.setNomeProgetto("Progetto");
		project.setBudget(300000);
		project.setCostoProgetto(500000);
		project.setDescrizione("Descrizione");
		project.setDataInizio(new Date());
		project.setDataFine(new Date(LocalDateTime.now().plusMonths(6).getSecond()*1000));
		
		payment = new Payment();
		payment.setCifra(300000);
	}

	@BeforeEach
    void setUp() throws NamingException, ClassNotFoundException {
    	DBAccessContext.setDBAccessContext();
    }

	@Test
	@Order(1)
	void testCreateAndGetByUser() {
		try {
			conn = DBAccess.getConnection();
			UserDAO.getFactory().create(conn, cliente);
			UserDAO.getFactory().create(conn, projectmanager);
			
			cliente = UserDAO.getFactory().getByUsername(conn, cliente.getUsername());
			projectmanager = UserDAO.getFactory().getByUsername(conn, projectmanager.getUsername());
			
			project.setIdCliente(cliente.getId());
			project.setIdResponsabile(projectmanager.getId());
			
			ProjectDAO.getFactory().create(conn, project);
			List<Project> pr = ProjectDAO.getFactory().getListProjectByCliente(conn, cliente.getId());
			project = pr.get(0);
			
			payment.setIdProgetto(project.getId());
			PaymentDAO.getFactory().create(conn, payment);
			
			Payment[] ps = PaymentDAO.getFactory().getByUser(conn, cliente);
			assertNotNull(ps, "Elenco payments non deve essere null");
			assertTrue(ps.length == 1, "Deve contenere un elemento");
			payment = ps[0];
			assertNotNull(payment, "L'elemento non deve essere null");
			DBAccess.closeConnection(conn);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed "+e.getMessage());
		}
	}
	
	@Test
	@Order(2)
	void testGetByProject() {
		try {
		
			conn = DBAccess.getConnection();
			Payment[] ps = PaymentDAO.getFactory().getByProject(conn, project);
			assertNotNull(ps, "Elenco payments non deve essere null");
			assertTrue(ps.length == 1, "Deve contenere un elemento");
			payment = ps[0];
			assertNotNull(payment, "L'elemento non deve essere null");
			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed "+e.getMessage());
		}
	}
	
	@Test
	@Order(3)
	void testGetById() {
		try {
		
			conn = DBAccess.getConnection();
			payment = PaymentDAO.getFactory().getById(conn, payment.getId());

			assertNotNull(payment, "L'elemento non deve essere null");
			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed "+e.getMessage());
		}
	}
	
	@Test
	@Order(3)
	void testGetAll() {
		try {
		
			conn = DBAccess.getConnection();
			Payment[] ps = PaymentDAO.getFactory().getAll(conn);
			assertNotNull(ps, "Elenco payments non deve essere null");
			assertTrue(ps.length >= 1, "Deve contenere almeno un elemento");
			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed "+e.getMessage());
		}
	}

	
	
	@Test
	@Order(4)
	void testDelete() {
		try {
			conn = DBAccess.getConnection();
			
			PaymentDAO.getFactory().delete(conn, payment.getId());
			payment = PaymentDAO.getFactory().getById(conn, payment.getId());
			assertNull(payment, "Il pagamento non è stato eliminato con successo");
			
			ProjectDAO.getFactory().delete(conn, project.getId());
			project = ProjectDAO.getFactory().getById(conn, project.getId());
			assertNull(project, "Il progetto non è stato eliminato con successo");
			
			UserDAO.getFactory().delete(conn, cliente.getId());
			UserDAO.getFactory().delete(conn, projectmanager.getId());
			
			cliente = UserDAO.getFactory().getByEmail(conn,cliente.getEmail());
			projectmanager = UserDAO.getFactory().getByEmail(conn, projectmanager.getEmail());
			assertNull(cliente, "cliente dovrebbe essere null");
			assertNull(projectmanager, "projectmanager dovrebbe essere null");
			
			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}
}
