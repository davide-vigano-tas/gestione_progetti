package eu.tasgroup.gestione.architecture.dbaccess;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBAccess {

	private static Connection conn;
	private static DataSource dataSource;
	
	
	public static synchronized Connection getConnection() throws NamingException, SQLException {
		//JNDI, prendiamo i parametri dalla configurazione wildfly
		InitialContext contesto = new InitialContext();
		//Legge string jndi
		dataSource = (DataSource) contesto.lookup("java:/MySQLDS");
		conn = dataSource.getConnection();
		return conn;
	}
	
	public static void closeConnection() throws SQLException {
		if(conn!=null) {
			conn.close();
		}
	}
}
