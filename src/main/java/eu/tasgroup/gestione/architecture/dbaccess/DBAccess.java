package eu.tasgroup.gestione.architecture.dbaccess;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import eu.tasgroup.gestione.architetture.dao.DAOException;

public class DBAccess {

	private static Connection conn;
	private static DataSource dataSource;

	public static synchronized Connection getConnection() throws NamingException, DAOException {
		try {
			// JNDI, prendiamo i parametri dalla configurazione wildfly
			InitialContext contesto = new InitialContext();
			// Legge string jndi
			dataSource = (DataSource) contesto.lookup("java:/MySQLDS");
			conn = dataSource.getConnection();
			return conn;
		} catch (SQLException sql) {
			throw new DAOException(sql);
		}
	}

	public static void closeConnection() throws DAOException {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException sql) {
			throw new DAOException(sql);
		}
	}
}
