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
            if(error === 'username_taken') {
            	 console.log(error);
				erDiv.textContent = 'Username già in uso';
            }
            if(error === 'email_taken')  {
				erDiv.textContent = 'Email già in uso';
            }
        }
    };



$(document).ready(function(){
	function fetchDipendentiNonAssegnati() {
		$.ajax({
			url: '<%= request.getContextPath()%>/admin/nonassegnati',
			type: 'GET',
			success:function(response) {
				$('#usersTableBody').html(response);
			},
			error: function(error) {
				console.error(error);
			}
		});
	}

	
	$('#q_form').submit(function(e) {
		e.preventDefault();
		fetchDipendentiNonAssegnati();
	});
	
	
});



</script>
</head>
<jsp:include page="../nav.jsp"/>
<body>
<div class="container">
			<header class="page-header">
			<h3>Utenti</h3>
		</header>
		<div id="error" class="alert alert-danger" >
								
					</div>
		<form id="q_form">
			
				<div class="input-group">
					<span class="input-group-btn">
						<button class="btn btn-default" type="submit">
						<i class="bi bi-search"></i>&nbsp;Dipendenti non assegnati
					</button>
					</span>
				
				</div>
		</form>
			<div class="table-responsive my-2">
			<table id="itemTable" class="table table-hover">
				<thead>
					<tr>
						<th style="width: 20%;">Username</th>
						<th style="width: 20%;">Email</th>
						<th style="width: 20%;">Ruoli</th>
						<th style="width: 20%;">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="usersTableBody">
					<%
					
						User[] users = AdminFacade.getInstance().getAllUsers();
						for(int i = 0; i<users.length; i++) {
							User got = users[i];
							Role[] user_roles = AdminFacade.getInstance().getRolesById(got.getId());
							if(Arrays.asList(user_roles).stream().anyMatch(r -> !r.getRole().equals(Ruoli.ADMIN))){
							
					%>
							<tr>
								
								<td style="vertical-align: middle;">
									<%= got.getUsername() %>
								</td>
								<td style="vertical-align: middle;">
									<%= got.getEmail() %>
								</td>
									<td style="vertical-align: middle;">
										<% String roles_string = "";
										for(Role role : user_roles) { 
												roles_string +=  role.getRole().name() + " ";
										 } %>
										
										<strong><%=roles_string%></strong>
									</td>
							
								<td style="vertical-align: middle;">
									
									<a type="submit" class="btn btn-primary btn-sm" 
									href="/<%= application.getServletContextName()%>/admin/dettagliUtente.jsp?id=<%=got.getId()%>">
											<i class = "bi bi-person"></i>
											&nbsp; Dettagli
									</a>
								
								</td>
							</tr>
					<%
							
						} 
						}%>
				</tbody>
			</table>
			<div class="my-2">
				<button type="button" class="btn btn-primary btn-sm" style="background-color:  green;"
					data-bs-toggle="modal" data-bs-target="#addUserModal">
						<i class="bi bi-person-add"></i>&nbsp;Aggiungi Dipendente
					</button>
			</div>
				<jsp:include page="adduser.jsp"/>
		</div>
</div>


<footer><%@ include file="../footer.html" %></footer>
</body>
</html>

<% } else { 
	 response.sendRedirect("../login.jsp");
	
	}
} else { response.sendRedirect("../login.jsp");} %>

