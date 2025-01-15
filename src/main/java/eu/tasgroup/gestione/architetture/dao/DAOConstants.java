package eu.tasgroup.gestione.architetture.dao;

public interface DAOConstants {
	/*---------------------- USER - USER ROLE*/

	// SELECT

	String SELECT_USERS = "SELECT * FROM users";
	String SELECT_USER_ID = "SELECT * FROM users WHERE id = ?";
	String SELECT_USER_USERNAME = "SELECT * FROM users WHERE username = ?";
	String SELECT_USER_PASSWORD = "SELECT * FROM users WHERE password = ? AND username = ?";
	String SELECT_USER_EMAIL = "SELECT * FROM users WHERE email = ?";

	String SELECT_USER_ROLES_BY_USERNAME = "SELECT r.role FROM users u "
	        + "JOIN users_roles r ON u.id = r.id_users "
	        + "WHERE u.username = ?";
	
	String SELECT_USER_ROLES_BY_USER_ID = "SELECT r.role FROM users u "
	        + "JOIN users_roles r ON u.id = r.id_users "
	        + "WHERE u.id = ?";

	// INSERT
	String INSERT_USERS = "INSERT INTO users (nome, cognome, username, password, email) VALUES (?, ?, ?, ?, ?)";
	String INSERT_USERS_ROLES = "INSERT INTO users_roles (id_users, role) VALUES (?, ?)"; 

	// UPDATE
	String UPDATE_USER = "UPDATE users SET nome = ?, cognome = ?, username = ?, password = ?, "
	        + "email = ?, tentativi_falliti = ?, locked = ? WHERE id = ?";
	String UPDATE_USER_ROLES = "UPDATE users_roles SET role = ? WHERE id_users = ?";


	/*---------------------- PROJECT*/

	// SELECT
	String SELECT_PROJECTS = "SELECT * FROM projects";
	String SELECT_PROJECT_ID = "SELECT * FROM projects WHERE id = ?";
	String SELECT_PROJECT_STATUS = "SELECT * FROM projects WHERE id = ? AND stato = ?";
	String SELECT_PROJECTS_CLIENTE = "SELECT * FROM projects WHERE id_cliente = ?";
	String SELECT_PROJECTS_RESPONSABILE = "SELECT * FROM projects WHERE id_responsabile = ?";
	String SELECT_PROJECT_COMPLETAMENTO = "SELECT percentuale_completamento FROM projects WHERE id = ?";

	// INSERT
	String INSERT_PROJECT = "INSERT INTO projects (nome_progetto, descrizione, data_inizio, data_fine, budget, id_cliente, "
	        + "id_responsabile, costo_progetto) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

	// UPDATE
	String UPDATE_PROJECT = "UPDATE projects SET nome_progetto = ?, descrizione = ?, data_inizio = ?, "
	        + "data_fine = ?, budget = ?, stato = ?, id_responsabile = ?, percentuale_completamento = ? WHERE id = ?";
}