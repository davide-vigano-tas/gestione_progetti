<%@page import="eu.tasgroup.gestione.businesscomponent.facade.ProjectManagerFacade"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.Ticket"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.facade.DipendenteFacade"%>
<%@page import="java.util.UUID"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%
long id =  Long.valueOf(request.getParameter("id"));
Ticket a = null;
if(request.getParameter("id") == null) {
	response.sendRedirect("dip-tickets.jsp");
} else {
	
String csrfToken = UUID.randomUUID().toString();
	request.getSession().setAttribute("csrfToken", csrfToken);
	request.setAttribute("csrfToken", csrfToken);
	a = ProjectManagerFacade.getInstance().getTicketById(id);
	if (a == null) {
	    response.sendRedirect("dip-tickets.jsp");
	    return;
	}

%>


	<div class="modal fade" id="updateModal_<%=id%>" tabindex="-1" role="dialog">
	<form action="/<%= application.getServletContextName()%>/updateTicket" method="post" id="form-modal">
		<div class="modal-dialog modal-md">
			<div class="modal-content">
				<div class="modal-header">
				<!-- Chiusura modal -->
					<button type="button" class="close" data-bs-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="eliminaModalabel">
						Aggiorna Ticket
					</h4>
				</div>
				<div class="modal-body">
				
				<input type="hidden" name="csrfToken" value="<%= request.getAttribute("csrfToken") %>">
				 <input type="hidden" name="id" id="id" 
			      value="<%= a.getId()%>">
	<!-- ---------------------------------  Nome -->
			<div class="mb-3 row">
			  <label for="titolo" class="col-sm-2 col-form-label">Titolo</label>
			  <div class="col-sm-6">
			    <div class="input-group">
			      <span class="input-group-text" id="nome-icon">
			        <i class="bi bi-person"></i>
			      </span>
			      <input type="text" name="titolo" id="titolo" class="form-control" placeholder="titolo.." required
			      value="<%= a.getTitle()%>">
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
			      <textarea name="descrizione" id="descrizione" 
			      class="form-control" placeholder="descrizione.." required><%=a.getDescription()%></textarea>
			    </div>
	
			  </div>
			</div>
				
				</div>
				<div class="modal-footer">
				
						<button type="button" class="btn btn-default modal-default" style="witdh: 150px"
						data-bs-dismiss="modal">
							Annulla
						</button>
						<button type="submit" class="btn btn-primary" id="submitBtn" style="background-color: green; witdh: 150px">
							<i class="bi bi-send-plus-fill"></i>&nbsp;&nbsp;Salva
						</button>
				
				</div>
			</div>
		</div>
		</form>
		

	</div>
	<%} %>
			
	
