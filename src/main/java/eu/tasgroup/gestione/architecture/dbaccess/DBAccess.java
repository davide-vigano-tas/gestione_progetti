package eu.tasgroup.gestione.architecture.dbaccess;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import eu.tasgroup.gestione.architetture.dao.DAOException;

public class DBAccess {

    private static DataSource dataSource;

    // Otteniamo una connessione dal pool di connessioni configurato in WildFly
    public static synchronized Connection getConnection() throws NamingException, DAOException {
        try {
            // Inizializzazione di dataSource solo se non è già stato fatto
            if (dataSource == null) {
                InitialContext contesto = new InitialContext();
                // Legge la configurazione JNDI per la connessione MySQL
                dataSource = (DataSource) contesto.lookup("java:/MySQLDS");
            }
            return dataSource.getConnection();
        } catch (SQLException sql) {
            throw new DAOException(sql);
        }
    }

    public static void closeConnection(Connection conn) throws DAOException {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException sql) {
            throw new DAOException(sql);
        }
    }
}
