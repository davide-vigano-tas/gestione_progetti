<%@page import="eu.tasgroup.gestione.businesscomponent.model.Ticket"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.facade.DipendenteFacade"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.Skill"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.Role"%>
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
<script>
    window.onload = function() {
        const urlParams = new URLSearchParams(window.location.search);
        const optionValue = urlParams.get('option');
        if (optionValue) {
            const selectElement = document.getElementById('userType');
            selectElement.value = optionValue;
        }
        const error = urlParams.get('error');
        const erDiv = document.getElementById('error');
        erDiv.style.display='none';
        if(error) {
           
        	erDiv.style.display='block';
     
            if(error === 'invalid_skill')  {
				erDiv.textContent = 'Skill non valida';
            }
        }
    };

   
</script>
</head>
<jsp:include page="../nav.jsp"/>
<body>
<div class="container">
			<header class="page-header">
			<h3>Skill</h3>
		</header>
		<div id="error" class="alert alert-danger" >
								
					</div>
		
			<div class="table-responsive my-2">
			<table id="itemTable" class="table table-hover">
				<thead>
					<tr>
						<th style="width: 20%;">Id</th>
						<th style="width: 20%;">Titolo</th>
						<th style="width: 20%;">Descrizione</th>
						<th style="width: 20%;">Apertura</th>
						<th style="width: 20%;">Chiusura</th>
						<th style="width: 20%;">&nbsp;</th>
					</tr>
				</thead>
				<tbody>
					
								<%Ticket[] tickets = DipendenteFacade.getInstance().getByDipendente(user.getId());
								for(Ticket el : tickets){ %>
							<tr>
								<td style="vertical-align: middle;">
									<%=el.getId() %>
								</td>
								<td style="vertical-align: middle;">
									<%=el.getTitle() %>
								</td>
								
								<td style="vertical-align: middle;">
									<%=el.getDescription() %>
								</td>
								<td style="vertical-align: middle;">
									<%=formato.format(el.getCreated_at()) %>
								</td>
								
								<% if(el.getClosed_at() == null) {%>
								<td style="vertical-align: middle;">
									Aperto
								</td>
								<td style="vertical-align: middle;">
			<div class="my-2">
				<button type="button" class="btn btn-primary btn-sm" style="background-color:  green;"
					data-bs-toggle="modal" data-bs-target="#updateModal_<%=el.getId()%>">
						<i class="bi bi-person-add"></i>&nbsp;Modifica
					</button>
			</div>
				<jsp:include page="updateTicket.jsp">
					<jsp:param value="<%=el.getId() %>" name="id"/>
				 </jsp:include>
								</td>
								
								
								<%} else { %>
								<td style="vertical-align: middle;">
									Chiuso il <%=formato.format(el.getClosed_at()) %>
								</td>
								<td style="vertical-align: middle;">
									Controlla su <%=user.getEmail() %>
								</td>
								<% } %>
								
								
							</tr>
								<%} %>

					
				</tbody>
			</table>
	

		</div>
</div>


<footer><%@ include file="../footer.html" %></footer>
</body>
</html>

<% } else { 
	 response.sendRedirect("../login.jsp");
	
	}
} else { response.sendRedirect("../login.jsp");} %>

