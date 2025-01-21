SET lc_time_names = 'it_IT';

-- USER

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    cognome VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    tentativi_falliti INT NOT NULL DEFAULT 0, 
    locked BOOLEAN DEFAULT FALSE,
    data_creazione TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- USER ROLE

CREATE TABLE users_roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    role VARCHAR(30) NOT NULL,
    id_users INT NOT NULL,
    CONSTRAINT fk_id_users FOREIGN KEY (id_users) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT unique_role_user UNIQUE(role, id_users)
);

-- PROJECT
CREATE TABLE projects (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome_progetto VARCHAR(50) NOT NULL,
    descrizione VARCHAR(255),
    data_inizio DATE NOT NULL,
    data_fine DATE NOT NULL,
    budget DECIMAL(10,2) NOT NULL,
    stato ENUM('CREATO', 'IN_PROGRESS', 'COMPLETATO', 'ANNULATO') DEFAULT 'CREATO',
    id_cliente INT NOT NULL, 
    id_responsabile INT NOT NULL,
    percentuale_completamento INT DEFAULT 0 CHECK (percentuale_completamento BETWEEN 0 AND 100),
    costo_progetto DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_id_cliente FOREIGN KEY (id_cliente) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_id_responsabile FOREIGN KEY (id_responsabile) REFERENCES users(id) ON DELETE CASCADE
);

-- PAYMENT
CREATE TABLE payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_progetto INT NOT NULL,
    cifra DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_payment_project FOREIGN KEY (id_progetto) REFERENCES projects(id) ON DELETE CASCADE
);

-- SKILL
CREATE TABLE skills (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(30) NOT NULL
);

-- USER SKILL
CREATE TABLE user_skills (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_competenze INT NOT NULL,
    id_utente INT NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (id_utente) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_skill FOREIGN KEY (id_competenze) REFERENCES skills(id) ON DELETE CASCADE
);

-- PROJECT TASK
CREATE TABLE project_tasks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_progetto INT NOT NULL,
    nome_task VARCHAR(100) NOT NULL,
    descrizione VARCHAR(255) NOT NULL,
    id_dipendente INT,
    stato ENUM('DA_INIZIARE', 'IN_PROGRESS', 'COMPLETATO') DEFAULT 'DA_INIZIARE',
    scadenza DATE NOT NULL,
    fase ENUM('PLAN', 'ANALISI', 'DESIGN', 'BUILD', 'TEST', 'DEPLOY') DEFAULT 'PLAN',
    CONSTRAINT fk_project FOREIGN KEY (id_progetto) REFERENCES projects(id) ON DELETE CASCADE,
    CONSTRAINT fk_employee_task FOREIGN KEY (id_dipendente) REFERENCES users(id) ON DELETE SET NULL
);

-- TIMESHEETS
CREATE TABLE timesheets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_dipendente INT NOT NULL,
    id_progetto INT NOT NULL,
    id_task INT NOT NULL,
    ore_lavorate DECIMAL(5,2) NOT NULL,
    data DATE NOT NULL,
    approvato BOOLEAN,
    CONSTRAINT fk_employee_timesheet FOREIGN KEY (id_dipendente) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_project_timesheet FOREIGN KEY (id_progetto) REFERENCES projects(id) ON DELETE CASCADE,
    CONSTRAINT fk_task_timesheet FOREIGN KEY (id_task) REFERENCES project_tasks(id) ON DELETE CASCADE
);

-- AUDIT LOG
CREATE TABLE audit_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    utente VARCHAR(50) NOT NULL,
    operazione VARCHAR(255) NOT NULL,
    data TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
