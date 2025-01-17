<!-- Token -->
<%@page import="eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.facade.AdminFacade"%>
<%@page import="java.util.Arrays"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.Role"%>
<%@page import="java.util.List"%>
<%@ page import="java.util.UUID" %>
<%
	if(session.getAttribute("username") != null) {
		AdminFacade af = AdminFacade.getInstance(); 
		String username = (String) session.getAttribute("username");
    	List<Role> roles = Arrays.asList(af.getRolesByUsername(username));
    	if (roles.stream().anyMatch(r -> r.getRole().equals(Ruoli.CLIENTE))){
			response.sendRedirect(request.getContextPath()+"/cliente/home.jsp");
    	} else if (roles.stream().anyMatch(r -> r.getRole().equals(Ruoli.DIPENDENTE))) {
    		response.sendRedirect(request.getContextPath()+"/dip/dipendente/home.jsp");
		} else if (roles.stream().anyMatch(r -> r.getRole().equals(Ruoli.PROJECT_MANAGER))){
			response.sendRedirect(request.getContextPath()+"/dip/projman/home.jsp");
		} else if (roles.stream().anyMatch(r -> r.getRole().equals(Ruoli.ADMIN))){
			response.sendRedirect(request.getContextPath()+"/admin/home.jsp");
		} 
	} else if(session.getAttribute("username") == null) {
		String csrfToken = UUID.randomUUID().toString();
		request.getSession().setAttribute("csrfToken", csrfToken);
		request.setAttribute("csrfToken", csrfToken);
	}
%>


<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="error.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="cdn.html" %>
<link rel="stylesheet" href="/<%= application.getServletContextName() %>/css/style.css"> 
<script>
  // Form validation logic
		  document.addEventListener('DOMContentLoaded', function () {
		  const form = document.querySelector('form');
		  const inputs = form.querySelectorAll('input');
		  const submitBtn = document.getElementById('submitBtn');
		
		  function checkValidity(input) {
		    const value = input.value.trim();
		    const name = input.name;
		
		    if (name === 'nome') {
		      const infoNome = document.getElementById('infoNome');
		      const nomeRegex = /^[a-zA-Z ,.\'-]{2,30}$/;  // Only letters and certain characters, 2 to 30 characters
		      infoNome.style.display='none';
		      if (!value) {
		        input.classList.add('is-invalid');
		        input.classList.remove('is-valid');
		        infoNome.style.display='block';
		        infoNome.textContent = 'Il campo nome non può essere vuoto';
		        return false;
		      }
		
		      if (!nomeRegex.test(value)) {
		        input.classList.add('is-invalid');
		        input.classList.remove('is-valid');
		        infoNome.style.display='block';
		        infoNome.textContent = 'Da 2 a 30 caratteri. (Solo lettere e caratteri validi)';
		        return false;
		      }
		
		      input.classList.add('is-valid');
		      input.classList.remove('is-invalid');
		      infoNome.textContent = '';
		      return true;
		    }
		
		    if (name === 'cognome') {
		      const infoCognome = document.getElementById('infoCognome');
		      const cognomeRegex = /^[a-zA-Z ,.\'-]{2,30}$/;
		      infoCognome.style.display='none';
		      if (!value) {
		        input.classList.add('is-invalid');
		        input.classList.remove('is-valid');
		        infoCognome.style.display='block';
		        infoCognome.textContent = 'Il campo cognome non può essere vuoto';
		        return false;
		      }
		
		      if (!cognomeRegex.test(value)) {
		        input.classList.add('is-invalid');
		        input.classList.remove('is-valid');
		        infoCognome.style.display='block';
		        infoCognome.textContent = 'Da 2 a 30 caratteri. (Solo lettere e caratteri validi)';
		        return false;
		      }
		
		      input.classList.add('is-valid');
		      input.classList.remove('is-invalid');
		      infoCognome.textContent = '';
		      return true;
		    }
		
		    if (name === 'username') {
		      const infoUsername = document.getElementById('infoUsername');
		      const usernameRegex = /^[a-zA-Z0-9!.-]{4,10}$/;
		      infoUsername.style.display='none';
		      if (!value) {
		        input.classList.add('is-invalid');
		        input.classList.remove('is-valid');
		        infoUsername.style.display='block';
		        infoUsername.textContent = 'Il campo username non può essere vuoto';
		        return false;
		      }
		
		      if (!usernameRegex.test(value)) {
		        input.classList.add('is-invalid');
		        input.classList.remove('is-valid');
		        infoUsername.style.display='block';
		        infoUsername.textContent = 'Da 4 a 10 caratteri. (Lettere e/o numeri e/o .-!)';
		        return false;
		      }
		
		      input.classList.add('is-valid');
		      input.classList.remove('is-invalid');
		      infoUsername.textContent = '';
		      return true;
		    }
		
		    if (name === 'password') {
		      const infoPassword = document.getElementById('infoPassword');
		      const passwordRegex = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^?!=])[a-zA-Z0-9@#$%^?!=]{7,15}$/;
		      infoPassword.style.display='none';
			   if (!value) {
		        input.classList.add('is-invalid');
		        input.classList.remove('is-valid');
		        infoPassword.style.display='block';
		        infoPassword.textContent = 'La password non può essere vuota';
		        return false;
		      }

		      if (!passwordRegex.test(value)) {
			        input.classList.add('is-invalid');
			        input.classList.remove('is-valid');
			        infoPassword.style.display='block';
			        infoPassword.textContent = 'Da 7 a 15 caratteri. (Almeno una minuscola, maiuscola, numero e carattere speciale)';
			        return false;
			   }
		
		      input.classList.add('is-valid');
		      input.classList.remove('is-invalid');
		      infoPassword.textContent = '';
		      return true;
		    }
		
		    if (name === 'email') {
		      const infoEmail = document.getElementById('infoEmail');
		      const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
		      infoEmail.style.display='none';
		      if (!value) {
		        input.classList.add('is-invalid');
		        input.classList.remove('is-valid');
		        infoEmail.style.display='block';
		        infoEmail.textContent = 'Il campo email non può essere vuoto';
		        return false;
		      }
		
		      if (!emailRegex.test(value)) {
		        input.classList.add('is-invalid');
		        input.classList.remove('is-valid');
		        infoEmail.style.display='block';
		        infoEmail.textContent = 'Inserisci una email valida';
		        return false;
		      }
		
		      input.classList.add('is-valid');
		      input.classList.remove('is-invalid');
		      infoEmail.textContent = '';
		      return true;
		    }
		
		    return true;
		  }
		
		  function validateForm() {
		    let isValid = true;
		
		    inputs.forEach(input => {
		      if (!checkValidity(input)) {
		        isValid = false;
		      }
		    });
		
		    return isValid;
		  }


		  form.addEventListener('click', function (e) {
			   validateForm();
			});
		  form.addEventListener('input', function (e) {
			   validateForm();
			});
			
		  submitBtn.addEventListener('click', function (e) {
		    e.preventDefault();  // Prevent form submission if validation fails
		    if (validateForm()) {
		      form.submit();  // Submit the form if validation is successful
		    } else {
		      alert('Please correct the errors in the form.');
		    }
		  });
		});


 
</script>
</head>
<body>

<jsp:include page="nav.jsp"/>
<div class="container">
	<header class ="page-header my-4">
		<h3>Inserisci dati per la registrazione</h3>
	</header>
	<!-- Registra è la servlet controller -->
	<form action="/<%= application.getServletContextName()%>/registra" method="post" 
	class="form-horizontal needs-validation" id="utenteForm">
	
		<input type="hidden" name="csrfToken" value="<%= request.getAttribute("csrfToken") %>">
		<input type="hidden" name="azione" id="azione" value="registrazione">
		
			<!-- ---------------------------------  Nome -->
			<div class="mb-3 row">
			  <label for="nome" class="col-sm-2 col-form-label">Nome</label>
			  <div class="col-sm-6">
			    <div class="input-group">
			      <span class="input-group-text" id="nome-icon">
			        <i class="bi bi-person"></i>
			      </span>
			      <input type="text" name="nome" id="nome" class="form-control" placeholder="Nome.." required>
			    </div>
			    <div class="invalid-feedback" id="infoNome">
			      Please provide a valid name.
			    </div>
			  </div>
			</div>
			
			<!-- ---------------------------------  Cognome -->
			<div class="mb-3 row">
			  <label for="cognome" class="col-sm-2 col-form-label">Cognome</label>
			  <div class="col-sm-6">
			    <div class="input-group">
			      <span class="input-group-text" id="cognome-icon">
			        <i class="bi bi-person"></i>
			      </span>
			      <input type="text" name="cognome" id="cognome" class="form-control" placeholder="Cognome.." required>
			    </div>
			    <div class="invalid-feedback" id="infoCognome">
			      Please provide a valid surname.
			    </div>
			  </div>
			</div>
			
			<!-- ---------------------------------  Username -->
			<div class="mb-3 row">
			  <label for="username" class="col-sm-2 col-form-label">Username</label>
			  <div class="col-sm-6">
			    <div class="input-group">
			      <span class="input-group-text" id="username-icon">
			        <i class="bi bi-person"></i>
			      </span>
			      <input type="text" name="username" id="username" class="form-control" placeholder="Username.." required maxlength="10">
			    </div>
			    <div class="invalid-feedback" id="infoUsername">
			      Username must be at most 10 characters long.
			    </div>
			  </div>
			</div>
			
			<!-- ---------------------------------  Password -->
			<div class="mb-3 row">
			  <label for="password" class="col-sm-2 col-form-label">Password</label>
			  <div class="col-sm-6">
			    <div class="input-group">
			      <span class="input-group-text" id="password-icon">
			        <i class="bi bi-lock"></i>
			      </span>
			      <input type="password" name="password" id="password" class="form-control" placeholder="Password.." required>
			    </div>
			    <div class="invalid-feedback" id="infoPassword">
			      Please provide a valid password.
			    </div>
			  </div>
			</div>
			
			<!-- ---------------------------------  Email -->
			<div class="mb-3 row">
			  <label for="email" class="col-sm-2 col-form-label">Email</label>
			  <div class="col-sm-6">
			    <div class="input-group">
			      <span class="input-group-text" id="email-icon">
			        <i class="bi bi-envelope"></i>
			      </span>
			      <input type="email" name="email" id="email" class="form-control" placeholder="Email.." required>
			    </div>
			    <div class="invalid-feedback" id="infoEmail">
			      Please provide a valid email.
			    </div>
			  </div>
			  <div class="col-sm-6 offset-sm-2 text-danger">
			    <small id="emailError"></small>
			  </div>
			</div>
			
			<!-- Submit Button -->
			<div class="row">
			  <div class="col-sm-6 offset-sm-2">
			    <button type="submit" class="btn btn-primary" id="submitBtn">
			      Registrati
			      <i class="bi bi-send"></i>
			    </button>
			  </div>
			</div>
	</form>
</div>
<body>

</body>

	
<footer><%@ include file="footer.html" %></footer>
</html>