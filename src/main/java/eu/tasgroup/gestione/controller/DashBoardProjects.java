package eu.tasgroup.gestione.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import eu.tasgroup.gestione.businesscomponent.facade.AdminFacade;
import eu.tasgroup.gestione.businesscomponent.model.Project;
@WebServlet("/admin/dashboardProjects")
public class DashBoardProjects extends HttpServlet {

	private static final long serialVersionUID = 5372189761472132053L;

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Sample data
		try {
			Project[] projects = AdminFacade.getInstance().getAllProjects();
			String[] nomi = new String[projects.length];
			for(int i = 0; i<nomi.length; i++) {
				nomi[i] = projects[i].getNomeProgetto();
				
			}
			int[] perc = new int[projects.length];
			for(int i = 0; i<perc.length; i++) {
				perc[i] = projects[i].getPercentualeCompletamento();
				
			}
	        Map<String, Object> data = new HashMap<>();
	        data.put("categories",nomi);
	        data.put("values", perc );

	        // Convert to JSON
	        String json = new Gson().toJson(data);

	        response.setContentType("application/json");
	        response.getWriter().write(json);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
		
		

    }
}
