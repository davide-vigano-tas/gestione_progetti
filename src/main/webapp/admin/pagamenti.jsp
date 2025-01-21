<%@page import="eu.tasgroup.gestione.businesscomponent.model.Payment"%>
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

</head>
<jsp:include page="../nav.jsp"/>
<body>
<div class="container">
			<header class="page-header">
			<h3>Pagamenti</h3>
		</header>

		
			<div class="table-responsive my-2" id="skillsAccordion">
			<table id="itemTable" class="table table-hover">
				<thead>
					<tr>
						<th style="width: 20%;">Id</th>
						<th style="width: 20%;">Progetto</th>
						<th style="width: 20%;">Cifra</th>
					</tr>
				</thead>
				<tbody>
					<%
					
						Payment[] payments = AdminFacade.getInstance().getAllPayments();
						for(int i = 0; i<payments.length; i++) {
							Payment got = payments[i];
						
							
					%>
							<tr>
								
								<td style="vertical-align: middle;">
									<%= got.getId() %>
								</td>
								<td style="vertical-align: middle;">
														
				
								<a type="submit" class="btn btn-default btn-sm" 
									href="/<%= application.getServletContextName()%>/admin/dettagliProgetto.jsp?id=<%=got.getIdProgetto()%>">
											<%=AdminFacade.getInstance().getProjectById(got.getIdProgetto()).getNomeProgetto() %>
									</a>
								</td>
				
							
								<td style="vertical-align: middle;">
									
						 			<%=got.getCifra()%>&nbsp;&euro;						     
								
								</td>
								
							</tr>

					<%
						}%>
						
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

