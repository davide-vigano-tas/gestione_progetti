<%@page import="eu.tasgroup.gestione.businesscomponent.PaymentBC"%>
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
	<div class="container mt-5">
		<h1 class="mb-4">Elenco Pagamenti</h1>

		<%
		// Recupera la lista dei pagamenti dall'attributo della richiesta
		List<Payment> payments = Arrays.asList(ClienteFacade.getInstance().getPaymentByCliente(user));
		if (payments == null || payments.isEmpty()) {
		%>
		<div class="alert alert-warning">Non sono stati trovati
			pagamenti per questo cliente.</div>
		<%
		} else {
		%>
		<table class="table table-bordered">
			<thead class="table-dark">
				<tr>
					<th>ID Pagamento</th>
					<th>ID Progetto</th>
					<th>Importo (€)</th>
					<th>Genera Fattura</th>
				</tr>
			</thead>
			<tbody>
				<%
				for (Payment payment : payments) {
				%>
				<tr style="vertical-align: middle;">
					<td><%=payment.getId()%></td>
					
					<td>
						<a type="submit" class="btn btn-default btn-md" 
						href="/<%=application.getServletContextName()%>/cliente/pagamenti.jsp?id=<%=payment.getIdProgetto()%>">
							<%=payment.getIdProgetto()%>
						</a>
					</td>
					
					<td><%=payment.getCifra()%></td>
					<td>
						<form action="<%=application.getContextPath()%>/cliente/generaFattura"
							method="POST" target="_blank">
							<input type="hidden" name="idPagamento"
								value="<%=payment.getId()%>">
							<button type="submit" class="btn btn-primary">
								<i class="bi bi-download"></i>&nbsp;Scarica PDF
							</button>
						</form>
					</td>
				</tr>
				<%
				}
				%>
			</tbody>
		</table>

		<%
		}
		%>
		<a href="<%=application.getContextPath()%>/cliente/cliente-home.jsp"
			class="btn btn-secondary">Torna alla Home</a>
	</div>
	<footer><%@ include file="../footer.html"%></footer>
</body>
</html>
<%
} else {
response.sendRedirect("/gestionale-progetti/cliente/dashboard.jsp");
}
} else {
response.sendRedirect("/gestionale-progetti/login.jsp");
}
%>
