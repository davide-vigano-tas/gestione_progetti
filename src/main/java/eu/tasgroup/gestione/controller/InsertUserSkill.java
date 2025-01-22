package eu.tasgroup.gestione.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.enumerated.Skills;
import eu.tasgroup.gestione.businesscomponent.facade.DipendenteFacade;
import eu.tasgroup.gestione.businesscomponent.model.AuditLog;
import eu.tasgroup.gestione.businesscomponent.model.Skill;


@WebServlet("/dipendente/addSkill")
public class InsertUserSkill extends HttpServlet {
	private static final long serialVersionUID = -2057058009291201411L;
	private DipendenteFacade df;
	
	

	@Override
	public void init() throws ServletException {
		try {
			df = DipendenteFacade.getInstance();
		} catch (DAOException | NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
	}



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long idUser = Long.parseLong( request.getParameter("idUser"));
		long idSkill = Long.parseLong( request.getParameter("idSkill"));
		
		try {
			Skill[] skills = df.getSkillsByUser(idUser);
			Skill skill = df.getSkillsById(idSkill);
			if(!Arrays.asList(skills).stream().anyMatch( s -> s.getTipo().equals(skill.getTipo()))) {
				Skill s = df.getSkillsById(idSkill);
				df.addSkill(idUser, s);
				AuditLog log = new AuditLog();
				log.setData(new Date());
				log.setOperazione("Aggiunta skill : "+s.getTipo().name() + " ad utente: " +(String) request.getSession().getAttribute("username"));
				log.setUtente((String) request.getSession().getAttribute("username"));
				df.createOrupdateAuditLog(log);
			}
			response.sendRedirect("../dipendente/dip-skills.jsp");
		}catch (NamingException e) {
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
	}

}
