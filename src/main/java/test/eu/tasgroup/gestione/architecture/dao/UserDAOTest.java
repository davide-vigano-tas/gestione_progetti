package test.eu.tasgroup.gestione.architecture.dao;

import static org.junit.jupiter.api.Assertions.*;

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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import eu.tasgroup.gestione.architecture.dbaccess.DBAccess;
import eu.tasgroup.gestione.architetture.dao.UserDAO;
import eu.tasgroup.gestione.businesscomponent.model.User;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDAOTest {
	
	private Connection conn;
	private static User user1;
	private static User user2;
	

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
		user1 = new User();
		user1.setNome("Sam");
		user1.setCognome("Mast");
		user1.setUsername("sammast");
		user1.setPassword("pass");
		user1.setEmail("sam@gmail.com");
		
		
		user2 = new User();
		user2.setNome("Rob");
		user2.setCognome("Bru");
		user2.setUsername("robbru");
		user2.setPassword("pass");
		user2.setEmail("rob@gmail.com");
	}

 

    @BeforeEach
    void setUp() throws NamingException, ClassNotFoundException {
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

    private DataSource createMockDataSource() {
        return new DataSource() {
            @Override
            public Connection getConnection() throws SQLException {
                return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/gestione_progetti?serverTimezone=Europe/Rome",
                    "utente", "pass");
            }

            @Override
            public Connection getConnection(String username, String password) throws SQLException {
                return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/gestione_progetti?serverTimezone=Europe/Rome",
                    username, password);
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

	@Test
	@Order(1)
	void testCreateAndGetByUsername() {
		try {
			conn = DBAccess.getConnection();
			UserDAO.getFactory().create(conn, user1);
			UserDAO.getFactory().create(conn, user2);
			user1 = UserDAO.getFactory().getByUsername(conn, user1.getUsername());
			user2 = UserDAO.getFactory().getByUsername(conn, user2.getUsername());
			DBAccess.closeConnection(conn);
			assertNotNull(user1, "User1 non dovrebbe essere null");
			assertNotNull(user2, "User2 non dovrebbe essere null");
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}
	
	@Test
	@Order(2)
	void getById() {
		try {
			conn = DBAccess.getConnection();
			user1 = UserDAO.getFactory().getById(conn, user1.getId());
			user2 = UserDAO.getFactory().getById(conn, user2.getId());
			assertNotNull(user1, "User1 non dovrebbe essere null");
			assertNotNull(user2, "User2 non dovrebbe essere null");
			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}
	
	@Test
	@Order(3)
	void getByEmail() {
		try {
			conn = DBAccess.getConnection();
			user1 = UserDAO.getFactory().getByEmail(conn, user1.getEmail());
			user2 = UserDAO.getFactory().getByEmail(conn, user2.getEmail());
			assertNotNull(user1, "User1 non dovrebbe essere null");
			assertNotNull(user2, "User2 non dovrebbe essere null");
			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}
	
	@Test
	@Order(4)
	void testUpdate() {
		try {
			conn = DBAccess.getConnection();
			user1.setNome("Samuel");
			user2.setNome("Roberto");
			
			UserDAO.getFactory().update(conn, user1);
			UserDAO.getFactory().update(conn, user2);
			
			user1 = UserDAO.getFactory().getByEmail(conn, user1.getEmail());
			user2 = UserDAO.getFactory().getByEmail(conn, user2.getEmail());
			assertNotNull(user1, "User1 non dovrebbe essere null");
			assertNotNull(user2, "User2 non dovrebbe essere null");
			assertEquals("Samuel", user1.getNome(), "Nome non aggiornato correttamente");
			assertEquals("Roberto", user2.getNome(), "Nome non aggiornato correttamente");
			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}
	
	
	@Test
	@Order(5)
	void testDelete() {
		try {
			conn = DBAccess.getConnection();
			
			UserDAO.getFactory().delete(conn, user1.getId());
			UserDAO.getFactory().delete(conn, user2.getId());
			
			user1 = UserDAO.getFactory().getByEmail(conn, user1.getEmail());
			user2 = UserDAO.getFactory().getByEmail(conn, user2.getEmail());
			assertNull(user1, "User1 dovrebbe essere null");
			assertNull(user2, "User2 dovrebbe essere null");
			DBAccess.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed: "+e.getMessage());
		}
	}

}
