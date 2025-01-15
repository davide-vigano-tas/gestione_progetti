package test.eu.tasgroup.gestione.architecture.dbaccess;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

import javax.naming.NamingException;

import org.junit.jupiter.api.Test;

import eu.tasgroup.gestione.architecture.dbaccess.DBAccess;
import eu.tasgroup.gestione.architetture.dao.DAOException;

class DBAccessTest {

	Connection conn;
	
	@Test
	void testConnection() {
		try {
			conn = DBAccess.getConnection();
		}catch (DAOException | NamingException e) {
			e.printStackTrace();
			fail("Errore nel tentativo di connessione " + e.getMessage());
		}finally {
			try {
				DBAccess.closeConnection(conn);
			}catch (DAOException e) {
				e.printStackTrace();
				fail("Errore nel tentativo di chiusura della connessione " + e.getMessage());
			}
		}
	}
}
