package test.eu.tasgroup.gestione.businesscomponent.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import eu.tasgroup.gestione.businesscomponent.security.Algoritmo;

class AlgoritmoTest {

	@Test
	void testConverti() {
		
		try {
			String password = "Pass01$";
			String hashedPassword = Algoritmo.converti(password);
			System.out.println("hashed password: "+hashedPassword);
			assertNotNull(hashedPassword, "Hashed password should not be null");
			assertTrue(hashedPassword.contains(":"), "La password hashata dovrebbe contenere "
					+ "sale e hash separati da due punti");
			
			
		}catch (Exception e) {
			e.printStackTrace();
			fail("eccezione sollevata dirante l'hashing della password "+e.getMessage());
		}
		
	}
	
	@Test
	void testVerificaPassword() {
		try {
			String password = "Pass01$";
			String hashedPassword = Algoritmo.converti(password);
			
			boolean isValid = Algoritmo.verificaPassword(password, hashedPassword);
			assertTrue(isValid, "La password dovrebbe essere valida");
			
			boolean isNotValid = Algoritmo.verificaPassword("wrong", hashedPassword);
			assertFalse(isNotValid, "La password non dovrebbe essere valida");
			
		}catch (Exception e) {
			e.printStackTrace();
			fail("eccezione sollevata dirante l'hashing della password "+e.getMessage());
		}
	}
}
