package test.eu.tasgroup.gestione.architetture.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import eu.tasgroup.gestione.architetture.dao.ProjectDAO;
import eu.tasgroup.gestione.architetture.dao.ProjectTaskDAO;
import eu.tasgroup.gestione.architetture.dao.UserDAO;
import eu.tasgroup.gestione.businesscomponent.enumerated.Fase;
import eu.tasgroup.gestione.businesscomponent.enumerated.StatoTask;
import eu.tasgroup.gestione.businesscomponent.model.Project;
import eu.tasgroup.gestione.businesscomponent.model.ProjectTask;
import eu.tasgroup.gestione.businesscomponent.model.User;
import test.eu.tasgroup.gestione.DBAccessContext;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProjectTaskDAOTest {

	private Connection conn;
	
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
		project.setDataFine(new Date(LocalDateTime.now().plusMonths(6).getSecond()*1000));
		
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
	
	@Test
	@Order(1)
	void testCreateAndGetByDipendete() {
		try {
			conn = DBAccess.getConnection();
			UserDAO.getFactory().create(conn, dipendente);
			UserDAO.getFactory().create(conn, cliente);
			UserDAO.getFactory().create(conn, projectmanager);
			
			dipendente = UserDAO.getFactory().getByUsername(conn, dipendente.getUsername());
			cliente = UserDAO.getFactory().getByUsername(conn, cliente.getUsername());
			projectmanager = UserDAO.getFactory().getByUsername(conn, projectmanager.getUsername());
			
			project.setIdCliente(cliente.getId());
			project.setIdResponsabile(projectmanager.getId());
			
			ProjectDAO.getFactory().create(conn, project);
			List<Project> pr = ProjectDAO.getFactory().getListProjectByCliente(conn, cliente.getId());
			project = pr.get(0);
			
			pt.setIdProgetto(project.getId());
			pt.setIdDipendente(dipendente.getId());
			ProjectTaskDAO.getFactory().create(conn, pt);
			
			List<ProjectTask> pts = ProjectTaskDAO.getFactory().getByDipendente(conn, dipendente.getId());
			assertNotNull(pts, "Elenco payments non deve essere null");
			assertEquals(1, pts.size(), "Deve contenere un elemento");
			pt = pts.get(0);
			assertNotNull(pt, "L'elemento non deve essere null");
			DBAccess.closeConnection(conn);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed "+e.getMessage());
		}
	}
	
	@Test
	@Order(2)
	void testGets() {
		try {
			conn = DBAccess.getConnection();
			
			assertEquals(pt, ProjectTaskDAO.getFactory().getById(conn, pt.getId()), "i project task devono essere uguali");
			assertEquals(1, ProjectTaskDAO.getFactory().getAll(conn).length);
			assertEquals(1, ProjectTaskDAO.getFactory().getByProject(conn, project.getId()).size());
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}
	
	@Test
	@Order(3)
	void testUpdates() {
		try {
			conn = DBAccess.getConnection();
			
			assertEquals(dipendente.getId(), pt.getIdDipendente());
			
			pt.setIdDipendente(projectmanager.getId());
			
			ProjectTaskDAO.getFactory().updateDipendente(conn, pt);
			
			assertEquals(0,ProjectTaskDAO.getFactory().getByDipendente(conn, dipendente.getId()).size());
			assertEquals(projectmanager.getId(),ProjectTaskDAO.getFactory().getByDipendente(conn, projectmanager.getId()).get(0).getIdDipendente(), "i dipendenti devono coincidere");
			
			assertEquals(pt.getFase(), Fase.DESIGN);
			assertEquals(pt.getStato(), StatoTask.DA_INIZIARE);
			
			ProjectTaskDAO.getFactory().updateFase(conn, Fase.ANALISI, pt.getId());
			ProjectTaskDAO.getFactory().updateStato(conn, StatoTask.COMPLETATO, pt.getId());
			
			pt = ProjectTaskDAO.getFactory().getById(conn, pt.getId());
			
			assertEquals(pt.getFase(), Fase.ANALISI);
			assertEquals(pt.getStato(), StatoTask.COMPLETATO);
			
			
			pt.setNomeTask("MIAO");
			ProjectTaskDAO.getFactory().update(conn, pt);
			assertEquals("MIAO", ProjectTaskDAO.getFactory().getById(conn, pt.getId()).getNomeTask());
			
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
			
			ProjectTaskDAO.getFactory().delete(conn, pt.getId());
			pt = ProjectTaskDAO.getFactory().getById(conn, pt.getId());
			assertNull(pt, "la project task deve essere vuota");
			
			
			ProjectDAO.getFactory().delete(conn, project.getId());
			project = ProjectDAO.getFactory().getById(conn, project.getId());
			assertNull(project, "Il progetto non è stato eliminato con successo");
			
			UserDAO.getFactory().delete(conn, dipendente.getId());
			UserDAO.getFactory().delete(conn, cliente.getId());
			UserDAO.getFactory().delete(conn, projectmanager.getId());
			
			dipendente = UserDAO.getFactory().getByEmail(conn,dipendente.getEmail());
			cliente = UserDAO.getFactory().getByEmail(conn,cliente.getEmail());
			projectmanager = UserDAO.getFactory().getByEmail(conn, projectmanager.getEmail());
			
			assertNull(dipendente, "dipndente dovrebbe essere null");
			assertNull(cliente, "cliente dovrebbe essere null");
			assertNull(projectmanager, "projectmanager dovrebbe essere null");
			
			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}
}
