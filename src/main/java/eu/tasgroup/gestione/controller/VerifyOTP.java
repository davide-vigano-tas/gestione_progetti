package eu.tasgroup.gestione.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class VerifyOTP
 */
@WebServlet("/verify-otp")
public class VerifyOTP extends HttpServlet {

	private static final long serialVersionUID = -7239560982001188877L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String otpInput = request.getParameter("otp");
        String otpSession = (String) request.getSession().getAttribute("otp");

        if (otpInput != null && otpInput.equals(otpSession)) {
            request.getSession().setAttribute("authenticated", true);
            response.sendRedirect("cliente/cliente-home.jsp");
        } else {
            response.sendRedirect("otp-verification.jsp?error=OTP errato.");
        }
	}

}
