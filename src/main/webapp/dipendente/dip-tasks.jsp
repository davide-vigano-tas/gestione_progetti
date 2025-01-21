<%@page import="eu.tasgroup.gestione.businesscomponent.enumerated.StatoTask"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.facade.DipendenteFacade"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.ProjectTask"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.Project"%>
<%@page import="java.util.List"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.Role"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Arrays"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="../error.jsp"%>
    
    
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
            
            <div class="row my-4 justify-content-evenly">
                <div class="col-md-3 text-center">
                	<a class="btn" href="taskForm.jsp">
		            	<i class="bi bi-plus" style="font-size: 2rem;"></i><br>
                    	<strong>Nuovo</strong>
                	</a>
                </div>
                
            </div>
            <div>
            	<table class="table table-striped" id="tabella">
               		<thead>
               			<tr>
               				<th>Id</th>
               				<th>Progetto</th>
               				<th>Nome</th>
               				<th>Descrizione</th>
               				<th>Dipendente</th>
               				<th>Stato</th>
               				<th>Scadenza</th>
               				<th>Fase</th>
               			</tr>
               		</thead>
               		<tbody>
               			<%
               			List<ProjectTask> tasks =DipendenteFacade.getInstance().getProjectTaskByDipendente(user.getId());
               			for(int i = 0; i< tasks.size(); i++){
               			%>
               			<tr>
               				<td><%=tasks.get(i).getId() %></td>
               				<td><%=DipendenteFacade.getInstance().getProjectById(tasks.get(i).getIdProgetto()).getNomeProgetto() %></td>
               				<td><%=tasks.get(i).getNomeTask() %></td>
               				<td><%=tasks.get(i).getDescrizione() %></td>
               				
               				<td><%=user.getEmail()%></td>
               				<td>
               					<button class="btn 
               					<%if(tasks.get(i).getStato().equals(StatoTask.DA_INIZIARE)){ %>
               					btn-primary" data-bs-toggle="modal"
									data-bs-target="#statoModal"
               					<%} else if(tasks.get(i).getStato().equals(StatoTask.IN_PROGRESS)){ %>
               					btn-warning" data-bs-toggle="modal"
									data-bs-target="#statoModal"
               					<%} else if(tasks.get(i).getStato().equals(StatoTask.COMPLETATO)){ %>
               					btn-success" disabled="disabled"
               					<%} %>
               					 data-bs-toggle="modal"
									data-bs-target="#statoModal"
									onclick="prepareStatoModal('<%=tasks.get(i).getId()%>','<%=tasks.get(i).getStato()%>')">
		               				<%=tasks.get(i).getStato() %>
								</button>
               				</td>
               				<td><%=tasks.get(i).getScadenza() %></td>
               				<td><%=tasks.get(i).getFase() %></td>
               				
               				
               				
               				
               				
               			</tr>
               			<%
               			}
               			%>
               		</tbody>
               	</table>
            </div>
        </div>
    </div>
    
    <div class="modal fade" id="statoModal" tabindex="-1"
			aria-labelledby="statoModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title" id="statoModalLabel">Stato Task</h5>
						<button type="button" class="btn-close" data-bs-dismiss="modal"
							aria-label="Close"></button>
					</div>
					<form id="statoForm" method="POST"
						action="/<%=application.getServletContextName()%>/dipendente/updateStatoTask">
						<div class="modal-body">
							<h4 id="modalTitle">Vuoi iniziare la task?</h4>
							<input type="hidden" name="taskId" id="modalTaskId">
							<input type="hidden" name="statoTask" id="modalStatoTask">
							
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-secondary"
								data-bs-dismiss="modal">Annulla</button>
							<button id="modalButton" type="submit" class="btn btn-success">Inizia</button>
						</div>
					</form>
				</div>
			</div>
		</div>
    
</div>


<footer><%@ include file="../footer.html" %></footer>
<script>
        function prepareStatoModal(taskId,statoTask) {
            document.getElementById("modalTaskId").value = taskId;
            document.getElementById("modalStatoTask").value = statoTask;
            if(statoTask== "IN_PROGRESS"){
            	document.getElementById("modalTitle").textContent="Task completato?"
            	document.getElementById("modalButton").textContent="Completa"
            }
        }

    </script>
</body>
</html>

<% } else { 
	 response.sendRedirect("/gestionale-progetti/login.jsp");
	
	}
} else { response.sendRedirect("/	gestionale-progetti/login.jsp");} %>