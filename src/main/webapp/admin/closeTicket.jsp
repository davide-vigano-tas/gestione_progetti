<%@page import="eu.tasgroup.gestione.businesscomponent.facade.AdminFacade"%>
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
	response.sendRedirect("tickets.jsp");
} else {
	
	a = AdminFacade.getInstance().getTicketById(id);
	if (a == null) {
	    response.sendRedirect("tickets.jsp");
	    return;
	}

%>


	<div class="modal fade" id="closeModal_<%=id%>" tabindex="-1" role="dialog">
	<form action="/<%= application.getServletContextName()%>/admin/closeTicket" method="post" id="form-modal">
		<div class="modal-dialog modal-md">
			<div class="modal-content">
				<div class="modal-header">
				<!-- Chiusura modal -->
					<button type="button" class="close" data-bs-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="eliminaModalabel">
						Chiudi ticket
					</h4>
				</div>
				<div class="modal-body">
				
								 <input type="hidden" name="id" id="id" 
			      value="<%= a.getId()%>">

			
				
			<!-- ---------------------------------  Descrizione -->
			<div class="mb-3 row">
			  <label for="spiegazione" class="col-sm-2 col-form-label">Spiegazione</label>
			  <div class="col-sm-6">
			    <div class="input-group" style="margin-left: 50px;">
			      <span class="input-group-text" id="cognome-icon">
			        <i class="bi bi-text-paragraph"></i>
			      </span>
			      <textarea name="spiegazione" id="spiegazione" 
			      class="form-control"  required></textarea>
			    </div>
	
			  </div>
			</div>
				
				</div>
				<div class="modal-footer">
				
						<button type="button" class="btn btn-default modal-default" style="witdh: 150px"
						data-bs-dismiss="modal">
							Annulla
						</button>
						<button type="submit" class="btn btn-primary" id="submitBtn" style=" witdh: 150px">
							<i class="bi bi-send-plus-fill"></i>&nbsp;&nbsp;Chiudi
						</button>
				
				</div>
			</div>
		</div>
		</form>
		

	</div>
	<%} %>
			
	
