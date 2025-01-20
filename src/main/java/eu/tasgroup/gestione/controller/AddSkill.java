package eu.tasgroup.gestione.controller;

import java.io.IOException;
import java.util.Arrays;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.enumerated.Skills;
import eu.tasgroup.gestione.businesscomponent.facade.AdminFacade;
import eu.tasgroup.gestione.businesscomponent.model.Skill;


@WebServlet("/admin/addSkill")
public class AddSkill extends HttpServlet {


	private static final long serialVersionUID = -237068466821484677L;
	private AdminFacade af;
	
	

	@Override
	public void init() throws ServletException {
		try {
			af = AdminFacade.getInstance();
		} catch (DAOException | NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
	}



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String skill = request.getParameter("skill");
		
		try {
			if(skill == null) {
				response.sendRedirect("../admin/skills.jsp?error=invalid_skill");
				return;
			}
			Skill[] skills = af.getAllSkills();
			if(Arrays.asList(skills).stream().allMatch( s -> !s.getTipo().equals(Skills.valueOf(skill)))) {
				Skill s = new Skill();
				s.setTipo(Skills.valueOf(skill));
				af.createSkill(s);
			}
			response.sendRedirect("../admin/skills.jsp");
		}catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
	}

}
