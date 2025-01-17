<%@page import="java.io.PrintWriter"%>
<%@page import="java.io.PrintStream"%>
<%@page import="eu.tasgroup.gestione.architetture.dao.DAOException"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="cdn.html" %>
<title>Error page</title>
<link rel="stylesheet" href="/<%= application.getServletContextName() %>/css/style.css"> 
</head>
<body>

<jsp:include page="nav.jsp"/>
<div class="container">
	<header class ="page-header">
		<h3>Pagina di errore</h3>
	</header>


<%
		if(exception instanceof ClassNotFoundException) {
		
%>

	<div class="panel panel-danger">
	
		<div class = "panel-heading">
			<h5>Tipo eccezione: <%= exception.getClass().getName() %></h5>
			<p> Classe da caricare non trovata</p>
		</div>
		<div class="panel-body" style="word-wrap: break-word;">
			<p>Message: <%= exception.getMessage() %></p>
			<div style="border-top: 30px;">
				<button onclick="windows.history.back()" class="btn btn-default">Indietro</button>
			</div>
		</div>
	</div>

<%
		} else if(exception instanceof DAOException) {

%>

	<div class="panel panel-danger">
	
		<div class = "panel-heading">
			<h5>Tipo eccezione: <%= exception.getClass().getName() %></h5>
			<p> Classe da caricare non trovata</p>
		</div>
		<div class="panel-body" style="word-wrap: break-word;">
			<p>Message: <%= exception.getMessage() %></p>
			<div style="border-top: 30px;">
				<button onclick="windows.history.back()" class="btn btn-default">Indietro</button>
			</div>
		</div>
	</div>

<%
		} else {

%>

	<div class="panel panel-danger">
	
		<div class = "panel-heading">
			<h5>Eccezione non prevista</h5>
			<p>Tipo eccezione: <%= exception.getClass().getName() %></p>
			<p> Classe da caricare non trovata</p>
		</div>
		<div class="panel-body" style="word-wrap: break-word;">
			<p>Message: <%= exception.getMessage() %></p>
			<p>StackTrace: </p>
			<% exception.printStackTrace(new PrintWriter(out)); %>
			<div style="border-top: 30px;">
				<button onclick="window.history.back()" class="btn btn-default">Indietro</button>
			</div>
		</div>
	</div>

<%
		} 
			
%>
	</div>
	
<footer><%@ include file="footer.html" %></footer>


</body>
</html>