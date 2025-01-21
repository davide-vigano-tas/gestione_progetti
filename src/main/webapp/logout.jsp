<%@ page import="java.util.UUID"%>
<%
if (session.getAttribute("username") != null) {
	session.invalidate();
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" errorPage="error.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="cdn.html"%>
<title>Logout utente</title>
<link rel="stylesheet"
	href="/<%=application.getServletContextName()%>/css/style.css">
<style type="text/css">
.rounded-panel {
	border: 0px; /* Bordo del pannello */
	border-radius: 0; /* Imposta il border-radius a zero per default */
	overflow: hidden;
	/* Assicura che il contenuto non fuoriesca dai bordi arrotondati */
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
	width: 80%;
	/* Imposta la larghezza del pannello (puoi modificare questa percentuale) */
	max-width: 600px; /* Imposta una larghezza massima per il pannello */
	margin: 0 auto; /* Centra il pannello orizzontalmente */
}

.rounded-panel .panel-heading {
	border-top-left-radius: 5px; /* Angolo superiore sinistro */
	border-top-right-radius: 5px; /* Angolo superiore destro */
}

.rounded-panel .panel-footer {
	border-bottom-left-radius: 5px; /* Angolo inferiore sinistro */
	border-bottom-right-radius: 5px; /* Angolo inferiore destro */
}
</style>
</head>
<body>
	<jsp:include page="nav.jsp"></jsp:include>
	<div class="container">
		<div class="row">
			<div style="margin-top: 30px;"></div>
			<div class="panel panel-primary rounded-panel">
				<div class="panel-heading text-center">
					<h3 class="panel-title">Logout</h3>
				</div>
				<div class="panel-body text-center">
					<i class="bi bi-box-arrow-left"
						style="font-size: 30px; color: blue;"> </i>
					<h4 class="mt-3">Logout effettuato correttamente</h4>
					<p>
						<a href="login.jsp" class="btn modal-default"
							style="margin-top: 30px">
							 <i class="bi bi-box-arrow-in-right" style=" color: blue;"></i> 
							 &nbsp;Effettua il login
							per accedere ai nostri servizi
						</a>
				</div>
				<div class="panel-footer text-center">
					<small>Grazie per aver visitato il nostro sito</small>
				</div>
			</div>
		</div>
	</div>
	<footer><%@include file="footer.html"%></footer>
<body>

</body>
</html>
<%
} else {
response.sendRedirect("login.jsp");
}
%>