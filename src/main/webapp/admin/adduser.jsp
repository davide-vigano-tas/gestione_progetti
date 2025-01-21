

<%@page import="java.util.UUID"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%

	
String csrfToken = UUID.randomUUID().toString();
	request.getSession().setAttribute("csrfToken", csrfToken);
	request.setAttribute("csrfToken", csrfToken);
	
%>


	<div class="modal fade" id="addUserModal" tabindex="-1" role="dialog">
	<form action="/<%= application.getServletContextName()%>/admin/addUser" method="post">
		<div class="modal-dialog modal-md">
			<div class="modal-content">
				<div class="modal-header">
				<!-- Chiusura modal -->
					<button type="button" class="close" data-bs-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="eliminaModalabel">
						Inserisci utente
					</h4>
				</div>
				<div class="modal-body">
				
				<input type="hidden" name="csrfToken" value="<%= request.getAttribute("csrfToken") %>">
				
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
			
				<!-- ---------------------------------  Tipo -->
			<div class="mb-3 row">
			  <label for="type" class="col-sm-2 col-form-label">Tipo</label>
			  <div class="col-sm-6">
			    <div class="input-group">
			      <span class="input-group-text" id="email-icon">
			        <i class="bi bi-person-circle"></i>
			      </span>
			   	<select class="form-select" name = "type" id="type" required >
			   	<option value="DIPENDENTE">Dipendente</option>
			   	<option value="PROJECT_MANAGER">Project Manager</option>
			   	</select>
			    </div>
		
			  </div>
		
			</div>
				
				</div>
				<div class="modal-footer">
				
						<button type="button" class="btn btn-default modal-default" style="witdh: 150px"
						data-bs-dismiss="modal">
							Annulla
						</button>
						<button type="submit" class="btn btn-primary" style="background-color: green; witdh: 150px">
							<i class="bi bi-send-plus-fill"></i>&nbsp;&nbsp;Salva
						</button>
				
				</div>
			</div>
		</div>
		</form>
		

	</div>
	
			
	
