<%@page import="eu.tasgroup.gestione.businesscomponent.model.ProjectTask"%>
<%@page import="java.util.List"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.Project"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.Role"%>
<%@page import="java.util.Arrays"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.User"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.facade.AdminFacade"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="../error.jsp"%>
    <%
	if(session.getAttribute("username") != null) {	
		String username = (String) session.getAttribute("username");
		User user = AdminFacade.getInstance().getByUsername(username);
		Role[] roles = AdminFacade.getInstance().getRolesById(user.getId());
		if(Arrays.asList(roles).stream().anyMatch(r -> r.getRole().equals(Ruoli.ADMIN))) {
			String id = request.getParameter("id");
			long user_id= Long.parseLong(id);
			Project retrieved = AdminFacade.getInstance().getProjectById(user_id);
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
   %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../cdn.html" %>
<title>Tas home</title>
<link rel="stylesheet" href="/<%= application.getServletContextName() %>/css/style.css"> 
<script src="../js/data_table.js"></script>
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
	
        .col-md-3 > a {
            color: #03122F;
            border-radius: 25px;
            min-width: 7.5em;
            border: 0.5rem solid rgba(251, 123, 6, 1);
            transition: 0.7s;
        }
        .col-md-3 > a:hover {
            color: #03122F;
            border-radius: 25px;
            border: 0.5rem solid #a70fff;
        }
	
	
</style>

</head>
<jsp:include page="../nav.jsp"/>
<body>
<div class="container">
<div class="card shadow-sm mt-5">
			   <div class="card-header">
            <h4>Progetto: <%=retrieved.getNomeProgetto() %></h4>
        </div>
        <div class="card-body">
            <div class="row mb-3">
				<div class="col-md-6">
					<p>
						<strong>Descrizione: <%=retrieved.getDescrizione() %></strong>
					</p>
					<p>
						<strong>Stato: <%=retrieved.getStato() %></strong>
					</p>
			
					<p>
						<strong>Inizio: <%=formato.format(retrieved.getDataInizio())%></strong>
					</p>
					<p>
						<strong>Fine: <%=formato.format(retrieved.getDataFine())%></strong>
					</p>
				</div>
				<div class="col-md-6">
					<p>
						<strong>Budget: <%=retrieved.getBudget() %></strong> 
					</p>

					<p>
						<strong>Costo: <%=retrieved.getCostoProgetto() %></strong>
					</p>
					
					<p>
						<strong>Percentuale completamento: <%=retrieved.getPercentualeCompletamento() %></strong>
					</p>
				</div>
				<div class="col-md-6">
					<div>
						<strong>Cliente:</strong> 
								<a type="submit" class="btn btn-default btn-sm" 
									href="/<%= application.getServletContextName()%>/admin/dettagliUtente.jsp?id=<%=AdminFacade.getInstance().getUserById(retrieved.getIdCliente()).getId()%>">
											<%=AdminFacade.getInstance().getUserById(retrieved.getIdCliente()).getEmail() %>
									</a>
					</div>

					<div>
						<strong>Responsabile:</strong> 
								<a type="submit" class="btn btn-default btn-sm" 
									href="/<%= application.getServletContextName()%>/admin/dettagliUtente.jsp?id=<%=AdminFacade.getInstance().getUserById(retrieved.getIdResponsabile()).getId()%>">
											<%=AdminFacade.getInstance().getUserById(retrieved.getIdResponsabile()).getEmail() %>
									</a>
					</div>
					

				</div>
				
			</div>
			<div class="row mb-3">

            <div class="table-responsive">
            	<table class="table table-striped" id="tabella">
               		<thead>
               			<tr>
               				<th>Id</th>
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
               			List<ProjectTask> tasks = AdminFacade.getInstance().getTasksByProject(retrieved);
               			for(int i = 0; i< tasks.size(); i++){
               			%>
               			<tr>
               				<td><%=tasks.get(i).getId() %></td>
              
               				<td><%=tasks.get(i).getNomeTask() %></td>
               				<td><%=tasks.get(i).getDescrizione() %></td>
               				
               				
               				<td><%=tasks.get(i).getStato() %></td>
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
			</div>
</div>


<footer><%@ include file="../footer.html" %></footer>
</body>
</html>

<% } else { 
	 response.sendRedirect("../login.jsp");
	
	}
} else { response.sendRedirect("../login.jsp");} %>

