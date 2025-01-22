package eu.tasgroup.gestione.controller;

import java.io.IOException;
import java.util.Arrays;

import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli;
import eu.tasgroup.gestione.businesscomponent.facade.AdminFacade;
import eu.tasgroup.gestione.businesscomponent.model.Role;
import eu.tasgroup.gestione.businesscomponent.model.User;
import eu.tasgroup.gestione.businesscomponent.security.Algoritmo;
import eu.tasgroup.gestione.businesscomponent.security.EscapeHTML;
import eu.tasgroup.gestione.businesscomponent.utility.EmailUtil;
import eu.tasgroup.gestione.businesscomponent.utility.OTPUtil;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	
	
	private static final long serialVersionUID = 5722794182564878684L;
	
	private AdminFacade af;

	@Override
	public void init() throws ServletException {
		try {
			af = AdminFacade.getInstance();
		} catch (DAOException | NamingException e) {
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
	}



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String userType = request.getParameter("userType");
		
		username = EscapeHTML.escapeHtml(username);
		password = EscapeHTML.escapeHtml(password);
		userType = EscapeHTML.escapeHtml(userType);
		
		
		HttpSession session = request.getSession();
		
		if(username != null && password != null & userType != null) {
			try {
				
				User user = af.getByUsername(username);
				if(user == null)  {
					response.sendRedirect("login.jsp?error=not_found");
					return ;
				}
				if(user.isLocked()) {
					response.sendRedirect("login.jsp?error=locked");
					return ;
				}
				if(!Algoritmo.verificaPassword(password, user.getPassword())) {
					user.setTentativiFalliti(user.getTentativiFalliti()+1);
					if(user.getTentativiFalliti() == 5) {
						user.setLocked(true);
					}
					response.sendRedirect("login.jsp?error=incorrect_password");
					return;
				}
					
				
				Role[] roles = af.getRolesById(user.getId());


				if(Arrays.asList(roles).stream().anyMatch(r -> r.getRole().equals(Ruoli.CLIENTE)) && userType.equals(Ruoli.CLIENTE.name())) {	
					
		            String otp = OTPUtil.generateOTP();
		            session.setAttribute("otp", otp);
		            System.err.println("otp: "+otp);

		            // Invia OTP via email
		            try {
		            	
		            	String emailContent = "<!DOCTYPE html><html lang=\"en\">"
		                        + "<head><meta charset=\"UTF-8\"></head>"
		                        + "<body>"
		                        + "<div style='background-color:#f4f4f4;padding:20px;'>"
		                        + "<div style='max-width:600px;margin:0 auto;background:#ffffff;padding:20px;border-radius:8px;'>"
		                        + "<h1 style='text-align:center;color:#007bff;'>Verifica il tuo accesso</h1>"
		                        + "<p style='text-align:center;'>Il tuo codice OTP è:</p>"
		                        + "<h2 style='text-align:center;color:#007bff;'>"
		                        + otp
		                        + "</h2>"
		                        + "<p style='text-align:center;'>Questo codice è valido per 5 minuti.</p>"
		                        + "</div></div></body></html>";
		            	
		                EmailUtil.sendEmail(user.getEmail(), "Il tuo codice OTP", emailContent);
		            } catch (MessagingException e) {
		                e.printStackTrace();
		                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nell'invio dell'email.");
		                return;
		            }

		            // Reindirizza alla pagina per inserire l'OTP
		            response.sendRedirect("otp-verification.jsp");
		            
		            //response.sendRedirect("cliente/cliente-home.jsp");
					return;
				} if(Arrays.asList(roles).stream().anyMatch(r -> r.getRole().equals(Ruoli.DIPENDENTE)) && userType.equals(Ruoli.DIPENDENTE.name())) {
					session.setAttribute("username", username);
					session.setMaxInactiveInterval(1800);
					response.sendRedirect("dipendente/dipendente-home.jsp");

					return;
				} if(Arrays.asList(roles).stream().anyMatch(r -> r.getRole().equals(Ruoli.PROJECT_MANAGER)) && userType.equals(Ruoli.PROJECT_MANAGER.name())) {
					session.setAttribute("username", username);
					session.setMaxInactiveInterval(1800);
					response.sendRedirect("projectManager/projectManager-home.jsp");
					return;
				} if(Arrays.asList(roles).stream().anyMatch(r -> r.getRole().equals(Ruoli.ADMIN)) && userType.equals(Ruoli.ADMIN.name())) {
					session.setAttribute("username", username);
					session.setMaxInactiveInterval(1800);
					response.sendRedirect("admin/admin-home.jsp");
					return;
				} 
				response.sendRedirect("login.jsp");
				
			}catch (Exception e) {
				e.printStackTrace();
				throw new ServletException("Errore durante la verifica dell'utente: "+e.getMessage());
			}
		} else {
			response.sendRedirect("login.jsp");
		}
	}

}
