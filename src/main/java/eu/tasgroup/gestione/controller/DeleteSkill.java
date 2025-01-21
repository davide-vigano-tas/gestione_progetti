package eu.tasgroup.gestione.controller;

import java.io.IOException;
import java.util.Date;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.facade.AdminFacade;
import eu.tasgroup.gestione.businesscomponent.model.AuditLog;
import eu.tasgroup.gestione.businesscomponent.model.Skill;


@WebServlet("/admin/deleteSkill")
public class DeleteSkill extends HttpServlet {


	private static final long serialVersionUID = 8804615766958336638L;
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
		String id = request.getParameter("id");
		
		try {
			if(id == null) {
				response.sendRedirect("../admin/skills.jsp?error=invalid_skill");
				return;
			}
			Skill skill = af.getSkillById(Long.parseLong(id));
			if(skill == null) {
				response.sendRedirect("../admin/skills.jsp?error=not_found");
				return;
			}
			af.deleteSkill(skill);
			AuditLog log = new AuditLog();
			log.setData(new Date());
			log.setOperazione("Eliminata skill : "+skill.getTipo().name());
			log.setUtente((String) request.getSession().getAttribute("username"));
			af.createOrupdateAuditLog(log);
			response.sendRedirect("../admin/skills.jsp");
		}catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
	}

}
