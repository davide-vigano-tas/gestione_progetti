package test.eu.tasgroup.gestione.businesscomponent;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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

import eu.tasgroup.gestione.businesscomponent.PaymentBC;
import eu.tasgroup.gestione.businesscomponent.ProjectBC;
import eu.tasgroup.gestione.businesscomponent.UserBC;
import eu.tasgroup.gestione.businesscomponent.model.Payment;
import eu.tasgroup.gestione.businesscomponent.model.Project;
import eu.tasgroup.gestione.businesscomponent.model.User;
import test.eu.tasgroup.gestione.DBAccessContext;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PaymentBCTest {
	
	private static User cliente;
	private static User projectmanager;
	private static Project project;
	private static Payment payment;
	private UserBC uBC;
	private ProjectBC pBC;
	private PaymentBC payBC;

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

			cliente = uBC.createOrUpdate(cliente);
			projectmanager = uBC.createOrUpdate(projectmanager);
			
			
			project.setIdCliente(cliente.getId());
			project.setIdResponsabile(projectmanager.getId());
			
			project = pBC.createOrUpdate(project);
			
			List<Project> pr = pBC.getListProjectByCliente(cliente.getId());
			project = pr.get(0);
			
			payment.setIdProgetto(project.getId());
			payBC.create(payment);
			
			Payment[] ps = payBC.getByUser(cliente);
			assertNotNull(ps, "Elenco payments non deve essere null");
			assertTrue(ps.length == 1, "Deve contenere un elemento");
			payment = ps[0];
			assertNotNull(payment, "L'elemento non deve essere null");
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed "+e.getMessage());
		}
	}
	
	@Test
	@Order(2)
	void testGetByProject() {
		try {
		
	
			Payment[] ps = payBC.getByProject(project);
			assertNotNull(ps, "Elenco payments non deve essere null");
			assertTrue(ps.length == 1, "Deve contenere un elemento");
			payment = ps[0];
			assertNotNull(payment, "L'elemento non deve essere null");
	
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed "+e.getMessage());
		}
	}
	
	@Test
	@Order(3)
	void testGetById() {
		try {
		
			payment = payBC.getById(payment.getId());

			assertNotNull(payment, "L'elemento non deve essere null");

		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed "+e.getMessage());
		}
	}
	
	@Test
	@Order(3)
	void testGetAll() {
		try {
			Payment[] ps = payBC.getAll();
			assertNotNull(ps, "Elenco payments non deve essere null");
			assertTrue(ps.length >= 1, "Deve contenere almeno un elemento");

		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed "+e.getMessage());
		}
	}

	
	
	@Test
	@Order(4)
	void testDelete() {
		try {
			
			payBC.delete(payment.getId());
			payment = payBC.getById(payment.getId());
			assertNull(payment, "Il pagamento non è stato eliminato con successo");
			
			pBC.delete(project.getId());
			project = pBC.getById(project.getId());
			assertNull(project, "Il progetto non è stato eliminato con successo");
			
		    uBC.delete(cliente);
		    uBC.delete(projectmanager);
			
			cliente = uBC.getByEmail(cliente.getEmail());
			projectmanager = uBC.getByEmail(projectmanager.getEmail());
			assertNull(cliente, "cliente dovrebbe essere null");
			assertNull(projectmanager, "projectmanager dovrebbe essere null");
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}

}
