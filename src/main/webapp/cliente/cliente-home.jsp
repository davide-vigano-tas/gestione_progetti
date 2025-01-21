<%@page import="eu.tasgroup.gestione.businesscomponent.model.Payment"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.Project"%>
<%@page import="java.util.List"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli"%>
<%@page import="java.util.Arrays"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.Role"%>
<%@page
	import="eu.tasgroup.gestione.businesscomponent.facade.ClienteFacade"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.User"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%
if (session.getAttribute("username") != null) {
	String username = (String) session.getAttribute("username");
	User user = ClienteFacade.getInstance().getByUsername(username);
	Role[] roles = ClienteFacade.getInstance().getRolesById(user.getId());
	if (Arrays.asList(roles).stream().anyMatch(r -> r.getRole().equals(Ruoli.CLIENTE))) {
		List<Project> projects = ClienteFacade.getInstance().getProjectsByCliente(user);
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
%>
<!DOCTYPE html>
<html lang="it">
<head>
<title>Tas Home</title>
<%@ include file="../cdn.html"%>
<link rel="stylesheet"
	href="/<%=application.getServletContextName()%>/css/style.css">
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="../js/data_table.js"></script>
<style>
.hidden {
	display: none;
}
</style>
</head>
<body>
	<jsp:include page="../nav.jsp" />
	<div class="container">
		<div class="card shadow-sm mt-5">
			<div class="card-header">
				<h4>
					Benvenuto,
					<%=username%></h4>
			</div>
			<div class="card-body">
				<div class="row mb-3">
					<div class="col-md-6">
						<p>
							<strong>Nome:</strong>
							<%=user.getNome()%></p>
						<p>
							<strong>Cognome:</strong>
							<%=user.getCognome()%></p>
						<p>
							<strong>Username:</strong>
							<%=username%></p>
						<p>
							<strong>Email:</strong>
							<%=user.getEmail()%></p>
					</div>
					<div class="col-md-6">
						<p>
							<strong>Tentativi Errati:</strong>
							<%=user.getTentativiFalliti()%></p>
						<p>
							<strong>Account Bloccato:</strong>
							<%=user.isLocked()%></p>
						<p>
							<strong>Data Creazione:</strong>
							<%=user.getDataCreazione().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)%></p>
					</div>
				</div>
			</div>
		</div>

		<!-- Pulsante per mostrare la tabella -->
		<div class="card shadow-sm mt-5">
			<div
				class="card-header d-flex justify-content-between align-items-center"
				onclick="toggleProjectsTable()">
				<h4>Progetti Associati</h4>
				<span> <i class="bi bi-chevron-down"></i>
				</span>
			</div>
			<div class="card-body hidden" id="projectsTableContainer">
				<table id="itemTable" class="table table-hover">
					<thead>
						<tr>
							<th>ID</th>
							<th>Nome</th>
							<th>Stato</th>
							<th>Progressi</th>
							<th>Fine Prevista</th>
							<th>Da versare</th>
							<th>Paga Ora</th>
							<th>Storia Pagamenti</th>
						</tr>
					</thead>
					<tbody>
						<%
						for (Project project : projects) {
						%>
						<tr>
							<td><%=project.getId()%></td>
							<td><%=project.getNomeProgetto()%></td>
							<td><%=project.getStato()%></td>
							<td>
								<div class="progress" style="height: 15px; margin-top: 6px;">
									<div class="progress-bar" role="progressbar"
										style="width: <%=project.getPercentualeCompletamento()%>%;"
										aria-valuenow="<%=project.getPercentualeCompletamento()%>"
										aria-valuemin="0" aria-valuemax="100"><%=project.getPercentualeCompletamento()%>%
									</div>
								</div>
							</td>
							<td><%=formato.format(project.getDataFine())%></td>
							<td>
								<%= project.getCostoProgetto() - ClienteFacade.getInstance().getTotalPaymentsByProjectId(project.getId()) %>
							</td>

							<%
							Payment[] pProject = ClienteFacade.getInstance().getPaymentByCliente(user);

							double totale = 0;
							for (Payment payment : pProject) {
								totale += payment.getCifra();
							}

							if (totale == 0) {
							%>
							<td>
								<button class="btn btn-success" data-bs-toggle="modal"
									data-bs-target="#paymentModal"
									onclick="preparePaymentModal('<%=project.getId()%>', '<%=project.getNomeProgetto()%>', '<%=project.getCostoProgetto() - totale%>')">
									<i class="bi bi-currency-euro"></i>
								</button>
							</td>

							<td>
								<button type="button" class="btn btn-warning position-relative"
									disabled>
									<i class="bi bi-clock-history"></i> <span
										class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-light text-dark">
										<%=pProject.length%> <span class="visually-hidden">history payment</span>
									</span>
								</button>
							</td>
							<%
							} else if (totale - project.getCostoProgetto() < 0) {
							%>
							<td>
								<button class="btn btn-success" data-bs-toggle="modal"
									data-bs-target="#paymentModal"
									onclick="preparePaymentModal('<%=project.getId()%>', '<%=project.getNomeProgetto()%>', '<%=project.getCostoProgetto() - totale%>')">
									<i class="bi bi-currency-euro"></i>
								</button>
							</td>
							<td><a type="submit"
								class="btn btn-warning position-relative"
								href="/<%=application.getServletContextName()%>/cliente/pagamenti.jsp?id=<%=project.getId()%>">
									<i class="bi bi-clock-history"></i> <span
									class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-light text-dark">
										<%=pProject.length%> <span class="visually-hidden">history payment</span>
								</span>
							</a></td>
							<%
							} else {
							%>
							<td>
								<button class="btn btn-success" disabled>
									<i class="bi bi-currency-euro"></i>
								</button>
							</td>
							<td><a type="submit"
								class="btn btn-warning position-relative"
								href="/<%=application.getServletContextName()%>/cliente/pagamenti.jsp?id=<%=project.getId()%>">
									<i class="bi bi-clock-history"></i> <span
									class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-light text-dark">
										<%=pProject.length%> <span class="visually-hidden">history payment</span>
								</span>
							</a></td>
							<%
							}
							%>
						</tr>
						<%
						}
						%>
					</tbody>
				</table>
			</div>
		</div>
		<div class="modal fade" id="paymentModal" tabindex="-1"
			aria-labelledby="paymentModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title" id="paymentModalLabel">Effettua un
							pagamento</h5>
						<button type="button" class="btn-close" data-bs-dismiss="modal"
							aria-label="Close"></button>
					</div>
					<form id="paymentForm" method="POST"
						action="/<%=application.getServletContextName()%>/cliente/processaPayment">
						<div class="modal-body">
							<input type="hidden" name="projectId" id="modalProjectId">
							<div class="mb-3">
								<label for="modalProjectName" class="form-label">Progetto</label>
								<input type="text" class="form-control" id="modalProjectName"
									readonly>
							</div>
							<div class="mb-3">
								<label for="modalAmount" class="form-label">Importo (€)</label>
								<input type="number" class="form-control" id="modalAmount"
									name="amount" min="1" required>
								<div class="form-text">
									Puoi pagare fino a <span id="modalMaxAmount"></span> €
								</div>
							</div>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-secondary"
								data-bs-dismiss="modal">Annulla</button>
							<button type="submit" class="btn btn-success">Conferma
								Pagamento</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>


	<footer><%@ include file="../footer.html"%></footer>

	<script>
	document.getElementById("paymentForm").addEventListener("submit", function (event) {
	    event.preventDefault(); // Impedisce l'invio immediato del form

	    const projectId = document.getElementById("modalProjectId").value;
	    const projectName = document.getElementById("modalProjectName").value;
	    const amount = document.getElementById("modalAmount").value;

	    Swal.fire({
	        title: "Confermi il pagamento di €"+amount+"?",
	        text: "Stai per effettuare un pagamento per il progetto " + projectName,
	        icon: "warning",
	        showCancelButton: true,
	        confirmButtonText: "Sì, conferma!",
	        cancelButtonText: "Annulla"
	    }).then((result) => {
	        if (result.isConfirmed) {
	            // Se l'utente conferma, invia il form
	            event.target.submit();
	        } else {
	            Swal.fire("Pagamento annullato", "Nessuna modifica è stata apportata.", "info");
	        }
	    });
	});

        function toggleProjectsTable() {
            const container = document.getElementById('projectsTableContainer');
            container.classList.toggle('hidden');
        }

        function preparePaymentModal(projectId, projectName, maxAmount) {
            // Popola i campi della modale con i dati del progetto
            document.getElementById("modalProjectId").value = projectId;
            document.getElementById("modalProjectName").value = projectName;
            document.getElementById("modalAmount").setAttribute("max", maxAmount);
            document.getElementById("modalMaxAmount").textContent = maxAmount;
        }

    </script>
</body>
</html>

<%
} else {
response.sendRedirect("/gestionale-progetti/login.jsp");
}
} else {
response.sendRedirect("/gestionale-progetti/login.jsp");
}
%>
