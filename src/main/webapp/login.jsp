<%@page import="java.util.UUID"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli"%>
<%@page import="java.util.Arrays"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.Role"%>
<%@page import="java.util.List"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.facade.AdminFacade"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="../error.jsp"%>
    
   <%
   
   if(session.getAttribute("username") != null) {
		AdminFacade af = AdminFacade.getInstance(); 
		String username = (String) session.getAttribute("username");
   	List<Role> roles = Arrays.asList(af.getRolesByUsername(username));
   	if (roles.stream().anyMatch(r -> r.getRole().equals(Ruoli.CLIENTE))){
			response.sendRedirect(request.getContextPath()+"/cliente/cliente-home.jsp");
   	} else if (roles.stream().anyMatch(r -> r.getRole().equals(Ruoli.DIPENDENTE))) {
   		response.sendRedirect(request.getContextPath()+"/dipendente/dipendente-home.jsp");
		} else if (roles.stream().anyMatch(r -> r.getRole().equals(Ruoli.PROJECT_MANAGER))){
			response.sendRedirect(request.getContextPath()+"/projectManager/projectManager-home.jsp");
		} else if (roles.stream().anyMatch(r -> r.getRole().equals(Ruoli.ADMIN))){
			response.sendRedirect(request.getContextPath()+"/admin/admin-home.jsp");
		} 
   } else  {
	   
	   String csrfToken = UUID.randomUUID().toString();
		request.getSession().setAttribute("csrfToken", csrfToken);
		request.setAttribute("csrfToken", csrfToken);
   %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="cdn.html" %>
<title>Login page</title>
<link rel="stylesheet"
	href="/<%=application.getServletContextName()%>/css/style.css">
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

<style>
.rounded-panel {
	border: 0px;
	border-radius: 5px;
	overflow: hidden;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
	width: 80%;
	max-width: 600px;
	margin: 30px auto;
}

.panel-heading {
	border-top-left-radius: 5px;
	border-top-right-radius: 5px;
}

.panel-footer {
	border-bottom-left-radius: 5px;
	border-bottom-right-radius: 5px;
}

.panel-body {
	border-radius: 0;
	padding: 30px;
}
</style>

</head>
<body>
	<jsp:include page="nav.jsp" />
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div class="panel panel-primary rounded-panel">
					<div class="panel-heading text-center">
						<h3 class="panel-title mt-4">Login</h3>
					</div>
					<div class="panel-body">
					<div id="error" class="alert alert-danger" >
								
					</div>
						<form action="/<%=application.getServletContextName()%>/login"
							method="post" class="form-horizontal" id="utenteForm">
							
							<input type="hidden" name="csrfToken" value="<%= request.getAttribute("csrfToken") %>">

							<!---------------------------------------------------------- Username -->
							<div class="form-group mb-2">
								<label class="col-md-2 control-label">Username</label>
								<div class="col-md-10">
									<div class="input-group">
										<span class="input-group-addon"><i
											class="glyphicon glyphicon-user"></i> </span> <input type="text"
											name="username" id="username" placeholder="Username..."
											class="form-control" maxlength='10'>

									</div>
								</div>
							</div>

							<!---------------------------------------------------------- Password -->
							<div class="form-group mb-2">
								<label class="col-md-2 control-label">Password</label>
								<div class="col-md-10">
									<div class="input-group">
										<span class="input-group-addon"><i
											class="glyphicon glyphicon-lock"></i> </span> <input type="password"
											name="password" id="password" placeholder="Password..."
											class="form-control">

									</div>
								</div>
							</div>

							<!---------------------------------------------------------- User Type -->
							<div class="form-group mb-2">
								<label class="col-md-2 control-label">Tipologia</label>
								<div class="col-md-10">
									<div class="input-group">
										<span class="input-group-addon"><i
											class="glyphicon glyphicon-cog"></i> </span> 
											<select name="userType"
											id="userType" class="form-control form-select" required>
											<option value="CLIENTE">Cliente</option>
											<option value="DIPENDENTE">Dipendente</option>
											<option value="PROJECT_MANAGER">Project Manager</option>
											<option value="ADMIN">Admin</option>
										</select>
									</div>
								</div>
							</div>

							<!---------------------------------------------------------- Submit button -->
							<div class="form-group mb-2">
								<div class="col-md-12 col-md-offset-2">
									<button type="submit" class="btn btn-primary">
										Login&nbsp;&nbsp; <span class="glyphicon glyphicon-log-in"></span>
									</button>
								</div>

							</div>

						</form>
						<%
						String error = (String) session.getAttribute("error");
						String success = (String) session.getAttribute("success");
						if (error != null) {
						%>
						<div class="alert alert-danger" role="alert">
							<%=error%>
						</div>
						<%
						session.removeAttribute("error");
						} else if (success != null) {
						%>
						<div class="alert alert-success" role="alert">
							<%=success%>
						</div>
						<%
						session.removeAttribute("success");
						}
						%>

					</div>
			
				</div>
			</div>
		</div>
	</div>

</body>
</html>
<% } %>