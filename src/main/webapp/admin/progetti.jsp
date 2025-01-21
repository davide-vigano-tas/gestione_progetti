<%@page import="eu.tasgroup.gestione.businesscomponent.facade.AdminFacade"%>
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
    pageEncoding="UTF-8" errorPage="../error.jsp"%>
    
    
<%
	if(session.getAttribute("username") != null) {	
		String username = (String) session.getAttribute("username");
		User user = ProjectManagerFacade.getInstance().getProjectManagerByUsername(username);
		Role[] roles = ProjectManagerFacade.getInstance().getRolesById(user.getId());
		if(Arrays.asList(roles).stream().anyMatch(r -> r.getRole().equals(Ruoli.ADMIN))) {
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
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
            <h4>Elenco progetti</h4>
        </div>
        <div class="card-body">
            

            <div>
            	<table class="table table-striped" id="tabella">
               		<thead>
               			<tr>
               				<th>Id</th>
               				<th>Nome</th>
               				<th>Descrizione</th>
               				<th>Inizio</th>
               				<th>FIne</th>
               				<th>Completamento</th>
               				<th>Cliente</th>
               				<th>Responsabile</th>
               				<th>budget</th>
               				<th>costo</th>
               			</tr>
               		</thead>
               		<tbody>
               			<%
               			Project[] projects = AdminFacade.getInstance().getAllProjects();
               			for(int i = 0; i< projects.length; i++){
               			%>
               			<tr>
               				<td>
               					<a type="submit" class="btn btn-sm" 
									href="/<%= application.getServletContextName()%>/admin/dettagliProgetto.jsp?id=<%=projects[i].getId() %>">
											<%=projects[i].getId() %>
									</a>

               				</td>
               				<td><%=projects[i].getNomeProgetto() %></td>
               				<td><%=projects[i].getDescrizione() %></td>
               				<td><%=formato.format(projects[i].getDataInizio())%></td>
               				<td><%=formato.format(projects[i].getDataFine()) %></td>
               				<td><%=projects[i].getPercentualeCompletamento() %>%</td>
               				<td>
               				
               			<a type="submit" class="btn btn-default btn-sm" 
									href="/<%= application.getServletContextName()%>/admin/dettagliUtente.jsp?id=<%=AdminFacade.getInstance().getUserById(projects[i].getIdCliente()).getId()%>">
											<%=AdminFacade.getInstance().getUserById(projects[i].getIdCliente()).getEmail() %>
									</a>
               				
               				
               				</td>
               				<td>
               				<a type="submit" class="btn btn-default btn-sm" 
									href="/<%= application.getServletContextName()%>/admin/dettagliUtente.jsp?id=<%=AdminFacade.getInstance().getUserById(projects[i].getIdResponsabile()).getId()%>">
											<%=AdminFacade.getInstance().getUserById(projects[i].getIdResponsabile()).getEmail() %>
									</a>
               				
               				
               				</td>
               				<td><%=projects[i].getBudget() %>&nbsp;&euro;</td>
               				<td><%=projects[i].getCostoProgetto() %>&nbsp;&euro;</td>
               				
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


<footer><%@ include file="../footer.html" %></footer>
</body>
</html>

<% } else { 
	 response.sendRedirect("/gestionale-progetti/login.jsp");
	
	}
} else { response.sendRedirect("/gestionale-progetti/login.jsp");} %>