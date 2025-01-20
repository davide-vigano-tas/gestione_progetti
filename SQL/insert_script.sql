INSERT INTO users (nome, cognome, username, password, email) 
VALUES ("Mario", "Rossi", "admin", "bqrXrKopvgKTLEZkDNMGrw==:7d216e85bae0f0b1dbbbabd181a84f3cf199c9eb76ae688dedbcacb8ac014cb5", "mario.rossi@tasgroup.eu");


SELECT * FROM users;

INSERT INTO users_roles (id_users, role) VALUES (104, "ADMIN");

SELECT * FROM users_roles;