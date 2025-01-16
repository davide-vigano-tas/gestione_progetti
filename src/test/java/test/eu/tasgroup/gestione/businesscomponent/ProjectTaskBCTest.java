package test.eu.tasgroup.gestione.businesscomponent;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
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

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.ProjectBC;
import eu.tasgroup.gestione.businesscomponent.ProjectTaskBC;
import eu.tasgroup.gestione.businesscomponent.UserBC;
import eu.tasgroup.gestione.businesscomponent.enumerated.Fase;
import eu.tasgroup.gestione.businesscomponent.enumerated.StatoTask;
import eu.tasgroup.gestione.businesscomponent.model.Project;
import eu.tasgroup.gestione.businesscomponent.model.ProjectTask;
import eu.tasgroup.gestione.businesscomponent.model.User;
import test.eu.tasgroup.gestione.DBAccessContext;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProjectTaskBCTest {

	private static User dipendente;
	private static User cliente;
	private static User projectmanager;
	private static Project project;
	private static ProjectTask pt;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		dipendente = new User();
		dipendente.setNome("Marco");
		dipendente.setCognome("Polo");
		dipendente.setUsername("marcoPolo");
		dipendente.setPassword("pass");
		dipendente.setEmail("marcoPolo@gmail.com");

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
		project.setDataFine(new Date(LocalDateTime.now().plusMonths(6).getSecond() * 1000));

		pt = new ProjectTask();
		pt.setNomeTask("accarezzare il gatto davanti casa");
		pt.setDescrizione("gia che ci sei dagli da mangiare che ha fame");
		pt.setScadenza(new Date());
		pt.setFase(Fase.DESIGN);
	}

	@BeforeEach
	void setUp() throws NamingException, ClassNotFoundException {
		DBAccessContext.setDBAccessContext();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		ProjectBC projectBC = new ProjectBC(); 
		projectBC.delete(project.getId());
		
		UserBC userBC = new UserBC(); 
		userBC.delete(cliente);
		
		userBC = new UserBC(); 
		userBC.delete(dipendente);
		
		userBC = new UserBC(); 
		userBC.delete(projectmanager);
		
	}

	@Test
	@Order(1)
	void testCreateOrUpdate() {
		try {
			UserBC userBC = new UserBC();
			cliente = userBC.createOrUpdate(cliente);

			userBC = new UserBC();
			dipendente = userBC.createOrUpdate(dipendente);

			userBC = new UserBC();
			projectmanager = userBC.createOrUpdate(projectmanager);

			project.setIdCliente(cliente.getId());
			project.setIdResponsabile(projectmanager.getId());

			ProjectBC projectBC = new ProjectBC();
			project = projectBC.createOrUpdate(project);

			pt.setIdProgetto(project.getId());
			pt.setIdDipendente(dipendente.getId());

			ProjectTaskBC projectTaskBC = new ProjectTaskBC();
			pt = projectTaskBC.createOrUpdate(pt);

			projectTaskBC = new ProjectTaskBC();
			pt.setNomeTask("la mucca fa mu ma la mia fa business component");
			pt = projectTaskBC.createOrUpdate(pt);

			assertTrue(pt.getNomeTask().equals("la mucca fa mu ma la mia fa business component"));
			
			projectTaskBC = new ProjectTaskBC();
			ProjectTask[] pts = projectTaskBC.getByAll();

			assertEquals(pts.length, 1);

		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed " + e.getMessage());
		}
	}

	@Test
	@Order(2)
	void testUpdateFase() {
		ProjectTaskBC projectTaskBC;
		try {
			projectTaskBC = new ProjectTaskBC();
			pt = projectTaskBC.updateFase(Fase.TEST, pt.getId());
			
			assertEquals(pt.getFase(), Fase.TEST);
			
			projectTaskBC = new ProjectTaskBC();
			ProjectTask[] pts = projectTaskBC.getByAll();

			assertEquals(pts.length, 1);

		} catch (DAOException | NamingException e) {
			e.printStackTrace();
			fail("Failed " + e.getMessage());
		}
	}

	@Test
	@Order(3)
	void testUpdateStato() {
		ProjectTaskBC projectTaskBC;
		try {
			projectTaskBC = new ProjectTaskBC();
			pt = projectTaskBC.updateStato(StatoTask.COMPLETATO, pt.getId());

			assertEquals(pt.getStato(), StatoTask.COMPLETATO);
			
			projectTaskBC = new ProjectTaskBC();
			ProjectTask[] pts = projectTaskBC.getByAll();

			assertEquals(pts.length, 1);

		} catch (DAOException | NamingException e) {
			e.printStackTrace();
			fail("Failed " + e.getMessage());
		}
	}

	@Test
	@Order(4)
	void testGetByID() {
		ProjectTaskBC projectTaskBC;
		try {
			projectTaskBC = new ProjectTaskBC();
			pt = projectTaskBC.getByID(pt.getId());

			assertNotNull(pt);
			
			projectTaskBC = new ProjectTaskBC();
			ProjectTask[] pts = projectTaskBC.getByAll();

			assertEquals(pts.length, 1);

		} catch (DAOException | NamingException e) {
			e.printStackTrace();
			fail("Failed " + e.getMessage());
		}
	}

	@Test
	@Order(5)
	void testGetByAll() {
		ProjectTaskBC projectTaskBC;
		try {
			projectTaskBC = new ProjectTaskBC();
			ProjectTask[] pts = projectTaskBC.getByAll();

			assertEquals(pts.length, 1);

		} catch (DAOException | NamingException e) {
			e.printStackTrace();
			fail("Failed " + e.getMessage());
		}
	}

	@Test
	@Order(6)
	void testGetByDipendente() {
		ProjectTaskBC projectTaskBC;
		try {
			projectTaskBC = new ProjectTaskBC();
			List<ProjectTask> pts = projectTaskBC.getByDipendente(dipendente.getId());

			assertNotNull(pts);
			assertEquals(1, pts.size());

		} catch (DAOException | NamingException e) {
			e.printStackTrace();
			fail("Failed " + e.getMessage());
		}
	}

	@Test
	@Order(7)
	void testDelete() {
		ProjectTaskBC projectTaskBC;
		try {
			projectTaskBC = new ProjectTaskBC();
			projectTaskBC.delete(pt.getId());
			
			projectTaskBC = new ProjectTaskBC();
			List<ProjectTask> pts = projectTaskBC.getByDipendente(dipendente.getId());

			assertNotNull(pts);
			assertEquals(0, pts.size());

		} catch (DAOException | NamingException e) {
			e.printStackTrace();
			fail("Failed " + e.getMessage());
		}
	}
}
