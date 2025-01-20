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
			User retrieved = AdminFacade.getInstance().getUserById(user_id);
			Role[] user_roles = AdminFacade.getInstance().getRolesById(user_id);
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
    };
    window.onload = function() {
        	const urlParams = new URLSearchParams(window.location.search);
        const error = urlParams.get('error');
        const erDiv = document.getElementById('error');
        erDiv.style.display='none';
        if(error) {
           
        	erDiv.style.display='block';
            if(error === 'not_found') {
            	 console.log(error);
				erDiv.textContent = 'Username non trovato';
            }
            if(error === 'incorrect_password')  {
				erDiv.textContent = 'Password errata';
            }
        }
        };
   
</script>
</head>
<jsp:include page="../nav.jsp"/>
<body>
<div class="container">
<div class="card shadow-sm mt-5">
			   <div class="card-header">
            <h4>Informazioni utente: <%=retrieved.getUsername() %></h4>
        </div>
        <div class="card-body">
            <div class="row mb-3">
				<div class="col-md-6">
					<p>
						<strong>Nome: <%=retrieved.getNome() %></strong>
					</p>
					<p>
						<strong>Cognome: <%=retrieved.getCognome() %></strong>
					</p>
			
					<p>
						<strong>Email: <%=retrieved.getEmail() %></strong>
					</p>
				</div>
				<div class="col-md-6">
					<p>
						<strong>Tentativi Errati: <%=retrieved.getTentativiFalliti() %></strong> 
					</p>
					
							<% if(retrieved.isLocked()) { %>
						<div class="my-2">
						<form action="/<%= application.getServletContextName()%>/admin/sblocca" method="post">
							<input type="hidden" name="id" value="<%= retrieved.getId()%>">
							<button type="submit" class="btn btn-primary btn-sm">
							<i class="bi bi-unlock"></i>
								&nbsp; Sblocca
							</button>
						</form>
						</div>

						
						<% } else { %>
							<div class="my-2">
						<form action="/<%= application.getServletContextName()%>/admin/blocca" method="post">
							<input type="hidden" name="id" value="<%= retrieved.getId()%>">
							<button type="submit" class="btn btn-primary btn-sm">
							<i class="bi bi-lock"></i>
								&nbsp; Blocca
							</button>
						</form>
						</div>
						<% } %>
					
					<p>
						<strong>Data Creazione: <%=retrieved.getDataCreazione().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) %></strong>
					</p>
				</div>
			</div>
			<div class="row mb-3">
				<div class="col-md-6">
					<div class="my-2">
						<strong>Ruoli: </strong>
						<% String roles_string = "";
						for(Role role : user_roles) { 
								roles_string +=  role.getRole().name() + " ";
						 } %>
						
						<p><strong><%=roles_string%></strong></p>
					</div>
					<div id="error" class="alert alert-danger" >
								
					</div>
					
					<div class="my-2">
					<form action="/<%= application.getServletContextName()%>/admin/addRole" method="post">
						<input type="hidden" name="id" value="<%= retrieved.getId()%>">
						<select class="form-select" required name="role">
							
							<option value="DIPENDENTE">Dipendente</option>
							<option value="PROJECT_MANAGER">Project Manager</option>
							<option value="ADMIN">Admin</option>
						</select>
						<button type="submit" class="btn btn-primary btn-sm my-3">
							<i class="bi bi-person-fill-gear"></i>
							&nbsp;Aggiungi un ruolo
						</button>
					</form>
					</div>
					
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

