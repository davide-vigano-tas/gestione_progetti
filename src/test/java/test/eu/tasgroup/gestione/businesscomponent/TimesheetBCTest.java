package test.eu.tasgroup.gestione.businesscomponent;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eu.tasgroup.gestione.architecture.dbaccess.DBAccess;
import eu.tasgroup.gestione.architetture.dao.ProjectDAO;
import eu.tasgroup.gestione.architetture.dao.ProjectTaskDAO;
import eu.tasgroup.gestione.architetture.dao.RoleDAO;
import eu.tasgroup.gestione.architetture.dao.UserDAO;
import eu.tasgroup.gestione.businesscomponent.TimesheetBC;
import eu.tasgroup.gestione.businesscomponent.enumerated.Fase;
import eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli;
import eu.tasgroup.gestione.businesscomponent.enumerated.StatoProgetto;
import eu.tasgroup.gestione.businesscomponent.model.Project;
import eu.tasgroup.gestione.businesscomponent.model.ProjectTask;
import eu.tasgroup.gestione.businesscomponent.model.Role;
import eu.tasgroup.gestione.businesscomponent.model.Timesheet;
import eu.tasgroup.gestione.businesscomponent.model.User;
import test.eu.tasgroup.gestione.DBAccessContext;

class TimesheetBCTest {

	private Connection conn;
	private TimesheetBC tBC;

	private static Timesheet timeSheet;

	private static ProjectTask projectTask;

	private static Project project1;

	private static User cliente;
	private static User responsabile;
	private static User dipendente;

	private static Role role1;
	private static Role role2;
	private static Role role3;

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

		dipendente = new User();
		dipendente.setNome("Dav");
		dipendente.setCognome("Vig");
		dipendente.setUsername("davvig");
		dipendente.setPassword("pass");
		dipendente.setEmail("dav@gmail.com");

		role1 = new Role();
		role1.setRole(Ruoli.CLIENTE);

		role2 = new Role();
		role2.setRole(Ruoli.PROJECT_MANAGER);

		role3 = new Role();
		role3.setRole(Ruoli.DIPENDENTE);

		project1 = new Project();

		project1.setNomeProgetto("Progetto Prova");
		project1.setDescrizione("Descrizione progetto di prova");
		project1.setDataInizio(new Date());
		project1.setDataFine(new Date());
		project1.setBudget(10000.00);
		project1.setStato(StatoProgetto.CREATO);
		project1.setCostoProgetto(50000.00);

		projectTask = new ProjectTask();
		projectTask.setNomeTask("Task1");
		projectTask.setDescrizione("Descrizione Task");
		projectTask.setScadenza(new Date());

		timeSheet = new Timesheet();
		timeSheet.setOreLavorate(8);
		timeSheet.setData(new Date());

	}

	@BeforeEach
	void setUp() {
		try {
			DBAccessContext.setDBAccessContext();
			tBC = new TimesheetBC();
			conn = DBAccess.getConnection();

			UserDAO.getFactory().create(conn, cliente);
			UserDAO.getFactory().create(conn, responsabile);
			UserDAO.getFactory().create(conn, dipendente);

			cliente = UserDAO.getFactory().getByUsername(conn, cliente.getUsername());
			responsabile = UserDAO.getFactory().getByUsername(conn, responsabile.getUsername());
			dipendente = UserDAO.getFactory().getByUsername(conn, dipendente.getUsername());

			role1.setIdUser(cliente.getId());
			role2.setIdUser(responsabile.getId());
			role3.setIdUser(dipendente.getId());

			RoleDAO.getFactory().create(conn, role1);
			RoleDAO.getFactory().create(conn, role2);
			RoleDAO.getFactory().create(conn, role3);

			project1.setIdCliente(cliente.getId());
			project1.setIdResponsabile(responsabile.getId());

			ProjectDAO.getFactory().create(conn, project1);

			Project[] progetti = ProjectDAO.getFactory().getAll(conn);
			project1.setId(progetti[0].getId());

			projectTask.setIdProgetto(project1.getId());
			projectTask.setIdDipendente(dipendente.getId());
			projectTask.setFase(Fase.PLAN);

			ProjectTaskDAO.getFactory().create(conn, projectTask);
			ProjectTask[] projectTasks = ProjectTaskDAO.getFactory().getAll(conn);

			projectTask.setId(projectTasks[0].getId());

			timeSheet.setIdProgetto(project1.getId());
			timeSheet.setIdDipendente(dipendente.getId());
			timeSheet.setIdTask(projectTask.getId());

			tBC.createOrUpdate(timeSheet);
			Timesheet[] timesheets = tBC.getAll();

			assertEquals(timesheets.length, 1);

			timeSheet.setId(timesheets[0].getId());

			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed setUp : " + e.getMessage());
		}
	}

	@AfterEach
	void tearDown() throws Exception {
		try {
			DBAccessContext.setDBAccessContext();
			conn = DBAccess.getConnection();

			tBC.delete(timeSheet.getId());
			Timesheet[] timesheets = tBC.getAll();
			assertEquals(timesheets.length, 0);

			ProjectTaskDAO.getFactory().delete(conn, projectTask.getId());

			ProjectDAO.getFactory().delete(conn, project1.getId());

			RoleDAO.getFactory().delete(conn, Ruoli.CLIENTE, cliente.getId());
			RoleDAO.getFactory().delete(conn, Ruoli.PROJECT_MANAGER, responsabile.getId());
			RoleDAO.getFactory().delete(conn, Ruoli.DIPENDENTE, dipendente.getId());

			UserDAO.getFactory().delete(conn, cliente.getId());
			UserDAO.getFactory().delete(conn, responsabile.getId());
			UserDAO.getFactory().delete(conn, dipendente.getId());

			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed tearDown : " + e.getMessage());
		}
	}

	@Test
	void testUpdateTimesheet() {
		try {
			conn = DBAccess.getConnection();
			
			timeSheet.setOreLavorate(9);
			GregorianCalendar c = new GregorianCalendar(10, 10, 2025);
			timeSheet.setData(c.getTime());

			tBC.createOrUpdate(timeSheet);
			timeSheet = tBC.getById(timeSheet.getId());

			assertNotNull(timeSheet);
			assertEquals(timeSheet.getOreLavorate(), 9);
			assertNotEquals(timeSheet.getData(), new Date());
		} catch (Exception e) {
			fail("Failed testUpdate : " + e.getMessage());
		}
	}

	@Test
	void testApprova() {
		try {
			conn = DBAccess.getConnection();
			
			tBC.approva(timeSheet.getId(), true);
			timeSheet = tBC.getById(timeSheet.getId());

			assertTrue(timeSheet.isApprovato());

		} catch (Exception e) {
			fail("Failed testApprova : " + e.getMessage());
		}
	}

	@Test
	void testGetByDipendente() {
		try {
			conn = DBAccess.getConnection();
			
			List<Timesheet> list = tBC.getListByDipendente(timeSheet.getIdDipendente());
			assertEquals(list.size(),1);

		} catch (Exception e) {
			fail("Failed testGetByDipendente : " + e.getMessage());
		}
	}

	@Test
	void testGetByProject() {
		try {
			conn = DBAccess.getConnection();
			
			List<Timesheet> list = tBC.getListByProject(timeSheet.getIdProgetto());
			assertEquals(list.size(),1);

		} catch (Exception e) {
			fail("Failed testGetByProject : " + e.getMessage());
		}
	}

}
