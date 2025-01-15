package eu.tasgroup.gestione.businesscomponent.utility;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil {     ////////Destinatario 
	public static void sendResetMail(String recipient, String resetLink) {
		final String username = "samuelmastro66@gmail.com";
		//Token
		final String password = "pfyp motf avzg oagt";
		
		try {
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");
			
			Session session = Session.getInstance(props, new Authenticator() {
				//Prende lo username  e la password e controlla che siano valide
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});
			
			
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("samuelmastro66@gmail.com", "Support Team"));
			//Campo TO
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
			message.setSubject("Reimposta la tua password");
			
			// HTML content or the email
			
			String htmlContent = "<html xmlns='http://www.w3.org/1999/xhtml'>" + 
			   "<head>"
			   + "<meta charset='UTF-8'"
			   + "<title>Reimposta Password</title>"
			   + "</head>"
			   + "<body style='margin: 0; padding: 0; font-family: Arial, sans-serif;'>"
			   + "<div style='background-color: #F4F4F4; padding: 40px;'>"
			   + "<div style='max-width: 800px; margin: 0 auto; background-color: #FFFFFF; "
			   + "padding: 40px; border-radius: 8px; box-shadow: 0, 2px, 4px rgba(0,0,0,0.3);'>"
			   + "<h2 style ='color: #333, font-size: 26px; margin-bottom: 20px;'>Reimposta la password</h2>"
			   + "<p>Ciao, </p>"
			   + "<p>Abbiamo ricevuto una richiesta per reimpostare la password. </p>"
			   + "<p>Per completare il processo clicca sul pulsante sotto: </p>"
			   + "<p style='text-align: left;'>"
			   + "<a href='" + resetLink + "' style='display: inline-block; padding: 15px 25px;"
			   + "font-size: 16px; color: #FFFFFF; background-color: #007BFF; "
			   + "text-decoration: none; "
			   + "border-radius: 5px; font-weight: bold'>Reimposta password</a>"
			   + "</p>"
			   + "<p>Se non hai richiesto la reimpostazione della password puoi"
			   + "ignorare questa email. "
			   + "La tua password rimarr&agrave; invariata.</p>"
			   + "<p>Per qualsiasi problema, non esitare a contattare "
			   + "il nostro supporto clienti.</p>"
			   + "<p>Grazie, <br>Il team</p>"
			   + "</div>"
			   + "</div>"
			   + "</body>"
			   + "</html>";
			
			message.setContent(htmlContent, "text/html");
			Transport.send(message);
			
		}catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}
}
