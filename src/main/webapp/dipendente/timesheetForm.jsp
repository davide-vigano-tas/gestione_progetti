<%@page import="eu.tasgroup.gestione.businesscomponent.model.Project"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.ProjectTask"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Arrays"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.Role"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.User"%>
<%@page import="java.util.List"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.Timesheet"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.facade.DipendenteFacade"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="error.jsp"%>
    
<%
	if(session.getAttribute("username") != null) {	
		String username = (String) session.getAttribute("username");
		User user = DipendenteFacade.getInstance().getByUsername(username);
		Role[] roles = DipendenteFacade.getInstance().getRolesById(user.getId());
		if(Arrays.asList(roles).stream().anyMatch(r -> r.getRole().equals(Ruoli.DIPENDENTE))) {
			SimpleDateFormat formato = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss");
   %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../cdn.html" %>
<title>Tas home</title>
<link rel="stylesheet" href="/<%= application.getServletContextName() %>/css/style.css"> 

<style>
	
	.content-section {
		margin-top: 20px;
		padding: 20px;
		background-color: #F8F9FA;
		border: 1px solid #ddd;
		border-radius: 10px;
		box-shadow: 0 2px 4px rba(0,0,0,0.1);
	}
	.content-section h2 {
		margin-top: 0;
		font-size: 24px;
	}
	
	.content-section p {
		font-size: 14px;
		line-height: 1.6;
	}
	
	
	.content-section img {
		max-width: 130px;
		height: auto;
		margin-right: 25px;
		border-radius: 10px;
	}
	.login{
		border-radius: 30px;
		
	}
	
	.login:hover {
		border-radius: 20px;
		 filter: brightness(110%);
	}
	
	.col-md-3 > a{
		color: #03122F;
		border-radius: 25px;
		min-width: 7.5em;
		border: 0.5rem solid  rgba(251,123,6,1);
		transition: 0.7s;
	}
	
	.col-md-3 > a:hover{
		color: #03122F;
		
		border-radius: 25px;
		border: 0.5rem solid  #a70fff;
	}
	
</style>
</head>
<jsp:include page="../nav.jsp"/>
<body>
<div class="container">
	<div class="card shadow-sm mt-5">
        <div class="card-header">
            <h4>Timesheets</h4>
        </div>
        <div class="card-body">
            <div class="row mb-3">
				<div class="col-md-6">
					<p>
						<strong>Nome:  <%=user.getNome() %></strong>
					</p>
					<p>
						<strong>Cognome: <%=user.getCognome() %></strong>
					</p>
					<p>
						<strong>Username: <%=username %></strong>
					</p>
					<p>
						<strong>Email: <%=user.getEmail() %></strong>
					</p>
				</div>
				<div class="col-md-6">
					<p>
						<strong>Tentativi Errati: <%=user.getTentativiFalliti() %></strong> 
					</p>
					<p>
						<strong>Account Bloccato: <%=user.isLocked() %></strong>
					</p>
					<p>
						<strong>Data Creazione: <%=user.getDataCreazione().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) %></strong>
					</p>
				</div>
			</div>
			
            <hr>
            
            <div>
            	<form action="/<%= application.getServletContextName()%>/dipendente/insertTimesheet" method="post" class="form-horizontal needs-validation" id="utenteForm">
					<input type="hidden" name="csrfToken" value="<%= request.getAttribute("csrfToken") %>">
					
						<!-- --------------------------dipendente -->
						<input type="hidden" name="dipendente" id="dipendente" class="form-control" value="<%=user.getId()%>">
						<%
							Project project = new Project();
						%>
						<div class="mb-3 row">
						  	<label for="project" class="col-sm-2 col-form-label">Project</label>
						  	<div class="col-sm-6">
						    <div class="input-group">
							<input  disabled="disabled" id="project" value="<%=project.getNomeProgetto()!=null ? project.getNomeProgetto(): "selezionare la task"%>">
							</div>
							</div>
						</div>
						<!-- ---------------------------------  Task -->
						<div class="mb-3 row">
						  <label for="task" class="col-sm-2 col-form-label">Task</label>
						  <div class="col-sm-6">
						    <div class="input-group">
						      <span class="input-group-text" id="task-icon">
						      	<!-- <i class="bi bi-person"></i>-->
						      </span>
						      <select name="task" id="task" class="form-control">
						      	<%
						      	List<ProjectTask> tasks = DipendenteFacade.getInstance().getProjectTaskByDipendente(user.getId());
						      		for(int i =0; i < tasks.size(); i++){
						      	%>
						      		<option value="<%=tasks.get(i).getId() %>"><%=tasks.get(i).getNomeTask()%></option>
						      		
						      	<%
						      	//TODO
										project = DipendenteFacade.getInstance().getProjectById(tasks.get(i).getIdProgetto());						      	
						      		}
						      	%>
						      </select>
						    </div>
						    <div class="invalid-feedback" id="infoTask">
						      inserire un budget
						    </div>
						  </div>
						</div>
					
						
						<!-- ---------------------------------  ore -->
						<div class="mb-3 row">
						  <label for="ore" class="col-sm-2 col-form-label">Ore lavorate</label>
						  <div class="col-sm-6">
						    <div class="input-group">
						      <span class="input-group-text" id="ore-icon">
						        <!-- <i class="bi bi-person"></i>-->
						      </span>
						      <input type="number" name="ore" id="ore" class="form-control" required>
						    </div>
						    <div class="invalid-feedback" id="oreInfo">
						      inserire una data
						    </div>
						  </div>
						</div>
						
						<!-- ---------------------------------  data -->
						<div class="mb-3 row">
						  <label for="data" class="col-sm-2 col-form-label">Data</label>
						  <div class="col-sm-6">
						    <div class="input-group">
						      <span class="input-group-text" id="data-icon">
						        <!-- <i class="bi bi-person"></i>-->
						      </span>
						      <input type="date" name="data" id="data" class="form-control" required>
						    </div>
						    <div class="invalid-feedback" id="dataInfo">
						      inserire una data
						    </div>
						  </div>
						</div>
						
						<!-- Submit Button -->
						<div class="row">
						  <div class="col-sm-6 offset-sm-2">
						    <button type="submit" class="btn btn-primary" id="submitBtn">
						      crea nuova task
						      <i class="bi bi-send"></i>
						    </button>
						  </div>
						</div>
				</form>
            
            </div>
            
        </div>
    </div>
</div>


<footer><%@ include file="../footer.html" %></footer>
</body>
</html>

<% } else { 
	 response.sendRedirect("/gestionale-progetti/login.jsp");
	
	}
} else { response.sendRedirect("/	gestionale-progetti/login.jsp");} %>