package eu.tasgroup.gestione.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebFilter({"/registra", "/login", "/admin/addUser"})
public class CSRFFilter extends HttpFilter implements Filter {


	private static final long serialVersionUID = -8328912410045061422L;

	
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	public void destroy() {
		// TODO Auto-generated method stub
	}


	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpServletResponse httpResp = (HttpServletResponse) response;
		String CSRFToken = httpReq.getParameter("csrfToken");
		String sessionCSRFToken = (String) httpReq.getSession().getAttribute("csrfToken");
		if(CSRFToken == null || !CSRFToken.equals(sessionCSRFToken)) {
			httpResp.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden"); //403
			return;
		}
		
		
		chain.doFilter(request, response);
	}




}
