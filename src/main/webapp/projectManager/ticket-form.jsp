<!-- Token -->
<%@page import="eu.tasgroup.gestione.businesscomponent.model.User"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.facade.DipendenteFacade"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli"%>
<%@page import="java.util.Arrays"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.Role"%>
<%@page import="java.util.List"%>
<%@ page import="java.util.UUID" %>
<%
	if(session.getAttribute("username") != null) {
		String username = (String) session.getAttribute("username");
		User user = DipendenteFacade.getInstance().getByUsername(username);
		Role[] roles = DipendenteFacade.getInstance().getRolesById(user.getId());
		if(Arrays.asList(roles).stream().anyMatch(r ->  r.getRole().equals(Ruoli.PROJECT_MANAGER))) {
	
%>


<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="../error.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../cdn.html" %>
<link rel="stylesheet" href="/<%= application.getServletContextName() %>/css/style.css"> 

</head>
<body>
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
            if(error === 'invalid_values') {
            	 console.log(error);
				erDiv.textContent = 'Titolo o descrizione non validi';
            }
            
        }
    };

   
</script>


<jsp:include page="../nav.jsp"/>
<div class="container">
	<header class ="page-header my-4">
		<h3>Inserisci la spiegazione del problema</h3>
	</header>
						<div id="error" class="alert alert-danger" >
								
					</div>
	<!-- Registra è la servlet controller -->
	<form action="/<%= application.getServletContextName()%>/insertTicket" method="post" 
	class="form-horizontal" >
	
		<input type="hidden" name="csrfToken" value="<%= request.getAttribute("csrfToken") %>">
		
			<!-- ---------------------------------  Titolo -->
			<div class="mb-3 row">
			  <label for="titolo" class="col-sm-2 col-form-label">Titolo</label>
			  <div class="col-sm-6">
			    <div class="input-group">
			      <span class="input-group-text" id="nome-icon">
			        <i class="bi bi-text-center"></i>
			      </span>
			      <input type="text" name="titolo" id="titolo" class="form-control" placeholder="Titolo.." required>
			    </div>

			  </div>
			</div>
			
			<!-- ---------------------------------  Descrizione -->
			<div class="mb-3 row">
			  <label for="descrizione" class="col-sm-2 col-form-label">Descrizione</label>
			  <div class="col-sm-6">
			    <div class="input-group">
			      <span class="input-group-text" id="cognome-icon">
			        <i class="bi bi-text-paragraph"></i>
			      </span>
			      <textarea name="descrizione" id="descrizione" class="form-control" placeholder="descrizione.." required></textarea>
			    </div>
	
			  </div>
			</div>
		
			
			<!-- Submit Button -->
			<div class="row">
			  <div class="col-sm-6 offset-sm-2">
			    <button type="submit" class="btn btn-primary" id="submitBtn">
			      Invia ticket
			      <i class="bi bi-send"></i>
			    </button>
			  </div>
			</div>
	</form>
</div>
<body>

</body>

	
<footer><%@ include file="../footer.html" %></footer>
</html>
<%} else {
	
	response.sendRedirect("../login.jsp");
}
		
} else {response.sendRedirect("../login.jsp");}%>