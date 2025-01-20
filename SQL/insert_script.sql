INSERT INTO users (nome, cognome, username, password, email) 
VALUES ("Mario", "Rossi", "admin", "3c0ba47b61b35455220830fd2761344c5c60dcf3364a5f47e495c05a7814690a", "mario.rossi@tasgroup.eu");

SELECT * FROM users;

INSERT INTO users_roles (id_users, role) VALUES (103, "ADMIN");