<%@page import="eu.tasgroup.gestione.businesscomponent.model.Skill"%>
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
						<th style="width: 20%;">Skill</th>
						<th style="width: 20%;">&nbsp;</th>
					</tr>
				</thead>
				<tbody>
					<%
					
						Skill[] skills = AdminFacade.getInstance().getAllSkills();
						for(int i = 0; i<skills.length; i++) {
							Skill got = skills[i];
						
							
					%>
							<tr>
								
								<td style="vertical-align: middle;">
									<%= got.getId() %>
								</td>
								<td style="vertical-align: middle;">
									<%= got.getTipo()%>
								</td>
				
							
								<td style="vertical-align: middle;">
									
						   <a class="btn btn-outline-dark btn-lg d-flex align-items-center justify-content-between w-100 mt-4 mb-4" 
								   data-bs-toggle="collapse" 
								   href="#userBySkill" role="button" 
								   aria-expanded="false" 
								   aria-controls="userBySkill">
								    <span class="me-2 fs-3"> 
								        <i class="fa-solid fa-wallet"></i>
								        &nbsp;Dipendenti con skill
								    </span>
								    <i class="fas fa-chevron-down fa-lg"></i>
								</a>
							 	<div class="collapse" id="userBySkill">
						  	<% 
						  	
						  	User[] uBySkill = AdminFacade.getInstance().getDipendentiBySkill(got); %>
						        <table class="table table-striped">
						            <thead>
						                <tr>
						                	<th style="width: 20%;">Nome</th>
											<th style="width: 20%;">Cognome</th>
											<th style="width: 20%;">Username</th>
											<th style="width: 20%;">Email</th>
											<th style="width: 20%;">&nbsp;</th>
						                </tr>
						            </thead>
						            <tbody>
						            	<% for(User u : uBySkill) {%>
						                <tr>
						                    <td style="vertical-align: middle;">
												<%= u.getNome() %>
											</td>
											<td style="vertical-align: middle;">
												<%= u.getCognome() %>
											</td>
											<td style="vertical-align: middle;">
												<%= u.getUsername() %>
											</td>
											<td style="vertical-align: middle;">
											<a type="submit" class="btn btn-primary btn-sm" 
											href="/<%= application.getServletContextName()%>/admin/dettagliUtente.jsp?id=<%=u.getId()%>">
													<i class = "bi bi-person"></i>
													&nbsp; Dettagli
											</a>
											</td>
										
						                </tr>
						                <% } %>
						            </tbody>
						        </table>
					        </div>
						     
								
								</td>
							</tr>

					<%
						}%>
						
				</tbody>
			</table>
			<div class="my-3">
			
			<form action="/<%= application.getServletContextName()%>/admin/addSkill" method="post">
					
						<select class="form-select" name = "type" id="type" required >
						   	<option value="JAVA8">Java8</option>
						   	<option value="JAVA17">Java17</option>
						   	<option value="PYTHON">Python</option>
						   	<option value="ANGULAR">Angular</option>
						   	<option value="SPRING">Spring</option>
						   	<option value="SPRING_BOOT">Spring boot</option>
						   	<option value="JAVASCRIPT">Javascript</option>
						   	<option value="ORACLE_DB">Oracle</option>
						   	<option value="MYSQL_DB">Mysql</option>
						   	<option value="MONGO_DB">Mongo</option>
						   	</select>
						<button type="submit" class="btn btn-primary btn-sm my-3">
							<i class="bi bi-bag-plus-fill"></i>
							&nbsp;Aggiungi una skill
						</button>
					</form>
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

