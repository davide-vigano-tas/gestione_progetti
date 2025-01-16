package test.eu.tasgroup.gestione.businesscomponent;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Date;
import java.util.List;

import javax.naming.NamingException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import eu.tasgroup.gestione.businesscomponent.ProjectBC;
import eu.tasgroup.gestione.businesscomponent.UserBC;
import eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli;
import eu.tasgroup.gestione.businesscomponent.enumerated.StatoProgetto;
import eu.tasgroup.gestione.businesscomponent.model.Project;
import eu.tasgroup.gestione.businesscomponent.model.Role;
import eu.tasgroup.gestione.businesscomponent.model.User;
import test.eu.tasgroup.gestione.DBAccessContext;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProjectBCTest {

	private static Project project1;
	private static User cliente;
	private static User responsabile;
	private static Role role1;
	private static Role role2;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {

		cliente = new User();
		cliente.setNome("Sam");
		cliente.setCognome("Mast");
		cliente.setUsername("sammast");
		cliente.setPassword("pass");
		cliente.setEmail("sam@gmail.com");

		responsabile = new User();
		responsabile.setNome("Rob");
		responsabile.setCognome("Bru");
		responsabile.setUsername("robbru");
		responsabile.setPassword("pass");
		responsabile.setEmail("rob@gmail.com");

		role1 = new Role();
		role1.setRole(Ruoli.CLIENTE);

		role2 = new Role();
		role2.setRole(Ruoli.PROJECT_MANAGER);

		project1 = new Project();

		project1.setNomeProgetto("Progetto Prova");
		project1.setDescrizione("Descrizione progetto di prova");
		project1.setDataInizio(new Date());
		project1.setDataFine(new Date());
		project1.setBudget(10000.00);
		project1.setStato(StatoProgetto.CREATO);
		project1.setCostoProgetto(50000.00);

	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {

		UserBC userBC = new UserBC();
		userBC.delete(cliente);
		
		userBC = new UserBC();
		userBC.delete(responsabile);
	}
	
	@BeforeEach
    void setUp() throws NamingException, ClassNotFoundException {
    	DBAccessContext.setDBAccessContext();	
    }

	@Test
	@Order(1)
	void testCreateOrUpdate() {
		try {
			UserBC userBC = new UserBC();
			cliente = userBC.createOrUpdate(cliente);
			
			userBC = new UserBC();
			responsabile = userBC.createOrUpdate(responsabile);

			userBC = new UserBC();
			userBC.addRole(cliente, role1);
			
			userBC = new UserBC();
			userBC.addRole(responsabile, role2);

			ProjectBC projectBC = new ProjectBC();
			project1.setIdCliente(cliente.getId());
			project1.setIdResponsabile(responsabile.getId());
			project1 = projectBC.createOrUpdate(project1);

			assertNotNull(project1, "Il progetto creato non può essere nullo");

			project1.setStato(StatoProgetto.IN_PROGRESS);
			project1.setPercentualeCompletamento(50);

			projectBC = new ProjectBC();
			project1 = projectBC.createOrUpdate(project1);

			assertNotNull(project1, "Il progetto creato non può essere nullo");
			assertEquals(project1.getStato(), StatoProgetto.IN_PROGRESS);
			assertEquals(project1.getPercentualeCompletamento(), 50);

		} catch (Exception e) {
			e.printStackTrace();
			fail("Create or Update fallito " + e.getMessage());
		}
	}

	@Test
	@Order(2)
	void testGetPercentualeCompletamento() {
		try {
			ProjectBC projectBC = new ProjectBC();
			int completamento = projectBC.getPercentualeCompletamento(project1.getId());

			assertEquals(completamento, 50);
		} catch (Exception e) {
			e.printStackTrace();
			fail("testGetPercentualeCompletamento fallito " + e.getMessage());
		}
	}

	@Test
	@Order(3)
	void testGetById() {
		try {
			ProjectBC projectBC = new ProjectBC();
			Project project = projectBC.getById(project1.getId());

			assertNotNull(project);
		} catch (Exception e) {
			e.printStackTrace();
			fail("testGetById fallito " + e.getMessage());
		}
	}

	@Test
	@Order(4)
	void testGetAll() {
		try {
			ProjectBC projectBC = new ProjectBC();
			Project[] projects = projectBC.getAll();

			assertTrue(projects.length > 0);
		} catch (Exception e) {
			e.printStackTrace();
			fail("testGetAll fallito " + e.getMessage());
		}
	}

	@Test
	@Order(5)
	void testGetListProjectByStatus() {
		try {
			ProjectBC projectBC = new ProjectBC();
			List<Project> projects = projectBC.getListProjectByStatus(StatoProgetto.IN_PROGRESS);

			assertTrue(projects.size() > 0);
		} catch (Exception e) {
			e.printStackTrace();
			fail("testGetListProjectByStatus " + e.getMessage());
		}
	}

	@Test
	@Order(6)
	void testGetListProjectByCliente() {
		try {
			ProjectBC projectBC = new ProjectBC();
			List<Project> projects = projectBC.getListProjectByCliente(cliente.getId());

			assertTrue(projects.size() > 0);
		} catch (Exception e) {
			e.printStackTrace();
			fail("testGetListProjectByCliente " + e.getMessage());
		}
	}

	@Test
	@Order(7)
	void testGetListProjectByResponsabile() {
		try {
			ProjectBC projectBC = new ProjectBC();
			List<Project> projects = projectBC.getListProjectByResponsabile(responsabile.getId());

			assertTrue(projects.size() > 0);
		} catch (Exception e) {
			e.printStackTrace();
			fail("testGetListProjectByResponsabile " + e.getMessage());
		}
	}

	@Test
	@Order(8)
	void testDelete() {
		try {
			ProjectBC projectBC = new ProjectBC();
			projectBC.delete(project1.getId());

			projectBC = new ProjectBC();
			Project[] projects = projectBC.getAll();

			assertEquals(projects.length, 0, "L'array deve essere vuoto");
		} catch (Exception e) {
			e.printStackTrace();
			fail("testDelete fallito " + e.getMessage());
		}
	}

}
