package test.eu.tasgroup.gestione.architetture.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import eu.tasgroup.gestione.architecture.dbaccess.DBAccess;
import eu.tasgroup.gestione.architetture.dao.ProjectDAO;
import eu.tasgroup.gestione.architetture.dao.RoleDAO;
import eu.tasgroup.gestione.architetture.dao.UserDAO;
import eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli;
import eu.tasgroup.gestione.businesscomponent.enumerated.StatoProgetto;
import eu.tasgroup.gestione.businesscomponent.model.Project;
import eu.tasgroup.gestione.businesscomponent.model.Role;
import eu.tasgroup.gestione.businesscomponent.model.User;
import test.eu.tasgroup.gestione.DBAccessContext;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProjectDAOTest {

	private Connection conn;
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

	@BeforeEach
	void setUp() throws Exception {
		try {
			DBAccessContext.setDBAccessContext();
			conn = DBAccess.getConnection();

			UserDAO.getFactory().create(conn, cliente);
			UserDAO.getFactory().create(conn, responsabile);

			cliente = UserDAO.getFactory().getByUsername(conn, cliente.getUsername());
			responsabile = UserDAO.getFactory().getByUsername(conn, responsabile.getUsername());

			role1.setIdUser(cliente.getId());
			role2.setIdUser(responsabile.getId());

			RoleDAO.getFactory().create(conn, role1);
			RoleDAO.getFactory().create(conn, role2);

			project1.setIdCliente(cliente.getId());
			project1.setIdResponsabile(responsabile.getId());

			ProjectDAO.getFactory().create(conn, project1);

			Project[] progetti = ProjectDAO.getFactory().getAll(conn);
			assertTrue(progetti.length == 1);

			project1.setId(progetti[0].getId());

			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed setUp : " + e.getMessage());
		}
	}

	@AfterEach
	void tearDown() throws ClassNotFoundException, NamingException {
		try {
			DBAccessContext.setDBAccessContext();
			conn = DBAccess.getConnection();

			ProjectDAO.getFactory().delete(conn, project1.getId());

			Project[] progetti = ProjectDAO.getFactory().getAll(conn);
			assertEquals(progetti.length, 0);

			RoleDAO.getFactory().delete(conn, Ruoli.CLIENTE, cliente.getId());
			RoleDAO.getFactory().delete(conn, Ruoli.PROJECT_MANAGER, responsabile.getId());

			UserDAO.getFactory().delete(conn, cliente.getId());
			UserDAO.getFactory().delete(conn, responsabile.getId());

			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed tearDown : " + e.getMessage());
		}
	}

	@Test
	@Order(1)
	void testUpdate() {
		project1.setStato(StatoProgetto.IN_PROGRESS);
		project1.setPercentualeCompletamento(50);;
		try {
			conn = DBAccess.getConnection();
			ProjectDAO.getFactory().update(conn, project1);
			
			int completamento = ProjectDAO.getFactory().getCompletamentoByProjectID(conn, project1.getId());
			assertEquals(completamento, 50);
			
			List<Project> progetti = ProjectDAO.getFactory().getListProjectByStatus(conn, StatoProgetto.IN_PROGRESS);
			assertNotNull(progetti);

			progetti = ProjectDAO.getFactory().getListProjectByStatus(conn, StatoProgetto.COMPLETATO);
			assertEquals(progetti.size(), 0);

		} catch (Exception e) {
			fail("Failed testUpdate : " + e.getMessage());
		}
	}

	@Test
	@Order(2)
	void testGetById() {
		try {
			conn = DBAccess.getConnection();
			Project progetto = ProjectDAO.getFactory().getById(conn, project1.getId());
			assertNotNull(progetto);
		} catch (Exception e) {
			fail("Failed testGetById : " + e.getMessage());
		}
	}

	@Test
	@Order(3)
	void testGetListProjectByCliente() {
		try {
			conn = DBAccess.getConnection();
			List<Project> progetti = ProjectDAO.getFactory().getListProjectByCliente(conn, cliente.getId());
			assertNotNull(progetti);

		} catch (Exception e) {
			fail("Failed testGetListProjectByStatus : " + e.getMessage());
		}
	}

	@Test
	@Order(4)
	void testGetListProjectByResponsabile() {
		try {
			conn = DBAccess.getConnection();
			List<Project> progetti = ProjectDAO.getFactory().getListProjectByResponsabile(conn, responsabile.getId());
			assertNotNull(progetti);

		} catch (Exception e) {
			fail("Failed testGetListProjectByStatus : " + e.getMessage());
		}
	}

}
