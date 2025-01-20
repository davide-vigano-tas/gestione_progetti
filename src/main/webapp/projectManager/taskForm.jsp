<%@page import="eu.tasgroup.gestione.businesscomponent.enumerated.Fase"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.Project"%>
<%@page import="java.util.List"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.Role"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.facade.ProjectManagerFacade"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Arrays"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="error.jsp"%>
    
    
<%
	if(session.getAttribute("username") != null) {	
		String username = (String) session.getAttribute("username");
		User user = ProjectManagerFacade.getInstance().getProjectManagerByUsername(username);
		Role[] roles = ProjectManagerFacade.getInstance().getRolesById(user.getId());
		if(Arrays.asList(roles).stream().anyMatch(r -> r.getRole().equals(Ruoli.PROJECT_MANAGER))) {
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
            <h4>Benvenuto <%=username %> </h4>
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
            	
            	<form action="/<%= application.getServletContextName()%>/projectManager/insertTask" method="post" class="form-horizontal needs-validation" id="utenteForm">
					<input type="hidden" name="csrfToken" value="<%= request.getAttribute("csrfToken") %>">
					
						<!-- ---------------------------------  Progetto -->
						<div class="mb-3 row">
						  <label for="progetto" class="col-sm-2 col-form-label">Progetto</label>
						  <div class="col-sm-6">
						    <div class="input-group">
						      <span class="input-group-text" id="progetto-icon">
						      	<!-- <i class="bi bi-person"></i>-->
						      </span>
						      <select name="progetto" id="progetto" class="form-control">
						      	<%
						      	List<Project> projects = ProjectManagerFacade.getInstance().getProjectsByResponsabile(
		               					user);
						      		for(int i =0; i < projects.size(); i++){
						      	%>
						      		<option value="<%=projects.get(i).getId() %>"><%=projects.get(i).getNomeProgetto()%></option>
						      	<%
						      		}
						      	%>
						      </select>
						    </div>
						    <div class="invalid-feedback" id="infoBudget">
						      inserire un budget
						    </div>
						  </div>
						</div>
					
						<!-- ---------------------------------  Nome -->
						<div class="mb-3 row">
						  <label for="nome" class="col-sm-2 col-form-label">Nome</label>
						  <div class="col-sm-6">
						    <div class="input-group">
						      <span class="input-group-text" id="nome-icon">
						        <!--  <i class="bi bi-person"></i>-->
						      </span>
						      <input type="text" name="nome" id="nome" class="form-control" placeholder="Nome.." required>
						    </div>
						    <div class="invalid-feedback" id="infoNome">
						      Please provide a valid name.
						    </div>
						  </div>
						</div>
						
						<!-- ---------------------------------  Descrizione -->
						<div class="mb-3 row">
						  <label for="descrizione" class="col-sm-2 col-form-label">Descrizione</label>
						  <div class="col-sm-6">
						    <div class="input-group">
						      <span class="input-group-text" id="descrizione-icon">
						        <!-- <i class="bi bi-person"></i>-->
						      </span>
						      <input type="text" name="descrizione" id="descrizione" class="form-control" placeholder="Descrizione.." required>
						    </div>
						    <div class="invalid-feedback" id="infoDescrizione">
						      inserire una descrizione
						    </div>
						  </div>
						</div>
						
						
						<!-- ---------------------------------  Dipendente -->
						<div class="mb-3 row">
						  <label for="dipendente" class="col-sm-2 col-form-label">Cliente</label>
						  <div class="col-sm-6">
						    <div class="input-group">
						      <span class="input-group-text" id="dipendente-icon">
						      	<i class="bi bi-person"></i>
						      </span>
						      <select name="dipendente" id="dipendente" class="form-control">
						      	<%
						      		User[] dipendenti = ProjectManagerFacade.getInstance().getDipendentiNonAssegnati();
						      		for(int i =0; i < dipendenti.length; i++){
						      	%>
						      		<option value="<%=dipendenti[i].getId() %>"><%=dipendenti[i].getEmail() %></option>
						      	<%
						      		}
						      	%>
						      </select>
						    </div>
						    <div class="invalid-feedback" id="infoBudget">
						      inserire un budget
						    </div>
						  </div>
						</div>
						
						
						<!-- ---------------------------------  scademnza -->
						<div class="mb-3 row">
						  <label for="scadenza" class="col-sm-2 col-form-label">Data Inizio</label>
						  <div class="col-sm-6">
						    <div class="input-group">
						      <span class="input-group-text" id="scadenza-icon">
						        <!-- <i class="bi bi-person"></i>-->
						      </span>
						      <input type="date" name="scadenza" id="scadenza" class="form-control" required>
						    </div>
						    <div class="invalid-feedback" id="scadenzaInfo">
						      inserire una data
						    </div>
						  </div>
						</div>
						
						<!-- ---------------------------------  Dipendente -->
						<div class="mb-3 row">
						  <label for="fase" class="col-sm-2 col-form-label">Fase</label>
						  <div class="col-sm-6">
						    <div class="input-group">
						      <span class="input-group-text" id="fase-icon">
						      	<i class="bi bi-person"></i>
						      </span>
						      <select name="fase" id="fase" class="form-control">
						      	<%
						      		for(Fase fase : Fase.values()){
						      	%>
						      		<option value="<%=fase.name() %>"><%=fase.name() %></option>
						      	<%
						      		}
						      	%>
						      </select>
						    </div>
						    <div class="invalid-feedback" id="infoFase">
						      inserire un budget
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