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
							<th>Inizio</th>
							<th>Fine</th>
							<th>Completamento</th>
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
							<td><%=formato.format(project.getDataInizio())%></td>
							<td><%=formato.format(project.getDataFine())%></td>
							<td><%=project.getPercentualeCompletamento()%> %</td>

							<%
							Payment[] pProject = ClienteFacade.getInstance().getPaymentByCliente(user);

							double totale = 0;
							for (Payment payment : pProject) {
								totale += payment.getCifra();
							}

							if (totale == 0) {
							%>
							<td>
								<button class="btn btn-success"
									onclick="confirmPayment('<%=project.getId()%>', '<%=project.getNomeProgetto()%>')">
									<i class="bi bi-currency-euro"></i>
								</button>
							</td>
							<td>
								<button type="button" class="btn btn-warning position-relative" disabled>
									<i class="bi bi-clock-history"></i> <span
										class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-light text-dark">
										<%= pProject.length %> <span class="visually-hidden">unread messages</span>
									</span>
								</button>
							</td>
							<%
							} else if (totale - project.getCostoProgetto() < 0) {
							%>
							<td>
								<button class="btn btn-success"
									onclick="confirmPayment('<%=project.getId()%>', '<%=project.getNomeProgetto()%>')">
									<i class="bi bi-currency-euro"> Nessuno</i>
								</button>
							</td>
							<td>
								<a type="submit" class="btn btn-warning position-relative" 
									href="/<%= application.getServletContextName()%>/cliente/pagamenti.jsp?id=<%=project.getId()%>">
									<i class="bi bi-clock-history"></i> <span
										class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-light text-dark">
										<%= pProject.length %> <span class="visually-hidden">unread messages</span>
									</span>
								</a>
							</td>
							<%
							} else {
							%>
							<td>
								<button class="btn btn-success" disabled>
									<i class="bi bi-currency-euro"></i>
								</button>
							</td>
							<td>
								<a type="submit" class="btn btn-warning position-relative" 
									href="/<%= application.getServletContextName()%>/cliente/pagamenti.jsp?id=<%=project.getId()%>">
									<i class="bi bi-clock-history"></i> <span
										class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-light text-dark">
										<%= pProject.length %> <span class="visually-hidden">unread messages</span>
									</span>
								</a>
							</td>
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
	</div>

	<footer><%@ include file="../footer.html"%></footer>

	<script>
        function toggleProjectsTable() {
            const container = document.getElementById('projectsTableContainer');
            container.classList.toggle('hidden');
        }

        function confirmPayment(projectId, projectName) {

        	console.log(projectId, projectName)
            Swal.fire({
                title: `Confermi il pagamento per il progetto ${projectName}?`,
                text: "Questa operazione una volta conclusa non è più reversibile.",
                icon: "warning",
                showCancelButton: true,
                confirmButtonText: "Sì, paga ora!",
                cancelButtonText: "Annulla"
            }).then((result) => {
                if (result.isConfirmed) {
                    fetch(`/gestionale-progetti/cliente/payment`, {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json"
                        },
                        body: JSON.stringify({ projectId: projectId })
                    })
                    .then(response => response.json())
                    .then(data => {
                        Swal.fire(data.success ? "Successo!" : "Errore!", data.message, data.success ? "success" : "error");
                    })
                    .catch(() => Swal.fire("Errore!", "Errore di connessione al server.", "error"));
                }
            });
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
