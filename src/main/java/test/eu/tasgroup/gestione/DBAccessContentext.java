package test.eu.tasgroup.gestione;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.NamingManager;
import javax.sql.DataSource;

import eu.tasgroup.gestione.architecture.dbaccess.DBAccess;

public class DBAccessContentext {

	public static void setDBAccessContext() throws ClassNotFoundException, NamingException {
		// Ensure MySQL JDBC driver is loaded
		Class.forName("com.mysql.cj.jdbc.Driver");

		// Set up a custom InitialContextFactory for JNDI
		if (!NamingManager.hasInitialContextFactoryBuilder()) {
			NamingManager.setInitialContextFactoryBuilder(env -> new InitialContextFactory() {
				@Override
				public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
					return new SimpleInitialContext();
				}
			});
		}

		// Bind the mock DataSource to the JNDI context
		InitialContext initialContext = new InitialContext();
		initialContext.bind("java:/MySQLDS", createMockDataSource());
		DBAccess.setTestContext(initialContext);
	}

	private static DataSource createMockDataSource() {
		return new DataSource() {
			@Override
			public Connection getConnection() throws SQLException {
				return DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/gestione_progetti?serverTimezone=Europe/Rome", "utente", "pass");
			}

			@Override
			public Connection getConnection(String username, String password) throws SQLException {
				return DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/gestione_progetti?serverTimezone=Europe/Rome", username, password);
			}

			@Override
			public PrintWriter getLogWriter() throws SQLException {
				return null;
			}

			@Override
			public void setLogWriter(PrintWriter out) throws SQLException {
			}

			@Override
			public void setLoginTimeout(int seconds) throws SQLException {
			}

			@Override
			public int getLoginTimeout() throws SQLException {
				return 0;
			}

			@Override
			public Logger getParentLogger() {
				return null;
			}

			@Override
			public <T> T unwrap(Class<T> iface) {
				return null;
			}

			@Override
			public boolean isWrapperFor(Class<?> iface) {
				return false;
			}
		};
	}

	// Simple InitialContext to simulate JNDI bindings
	static class SimpleInitialContext extends InitialContext {
		private final Hashtable<String, Object> bindings = new Hashtable<>();

		public SimpleInitialContext() throws NamingException {
		}

		@Override
		public Object lookup(String name) throws NamingException {
			if (bindings.containsKey(name)) {
				return bindings.get(name);
			}
			throw new NamingException("Name " + name + " not found");
		}

		@Override
		public void bind(String name, Object obj) throws NamingException {
			bindings.put(name, obj);
		}
	}
}