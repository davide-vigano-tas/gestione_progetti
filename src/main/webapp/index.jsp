<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="error.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="cdn.html" %>
<title>Tas home</title>
<link rel="stylesheet" href="/<%= application.getServletContextName() %>/css/style.css"> 

<style>
	
	.content-section {
		margin-top: 20px;
		padding: 20px;
		background-color: #F8F9FA;
		border: 1px solid #ddd;
		border-radius: 10px;
		box-shadow: 0 2px 4px rba(0,0,0,0.1);
	}
	.content-section h2 {
		margin-top: 0;
		font-size: 24px;
	}
	
	.content-section p {
		font-size: 14px;
		line-height: 1.6;
	}
	
	
	.content-section img {
		max-width: 130px;
		height: auto;
		margin-right: 25px;
		border-radius: 10px;
	}
	.login{
		border-radius: 30px;
		
	}
	
	.login:hover {
		border-radius: 20px;
		 filter: brightness(110%);
	}
	
</style>
</head>
<jsp:include page="nav.jsp"/>
<body>
<div class="container-fluid">
	<div class="container-sm my-4 login" style="max-width: 70%;"> 
	<button class="btn btn-primary login" style="background-color: #105491; width: 100%;">
		<h4 class="h4 py-4" style="color: white; text-align: center;"> 
		Entra come Cliente&nbsp;<i class="bi bi-box-arrow-in-right"></i></h4>
	</button>
	</div>
	<div class="container-sm my-4 login" style=" max-width: 70%;">
	<button class="btn btn-primary login" style="background-color: #11a27a; width: 100%;" >
		<h4 class="h4 py-4" style="color: white; text-align: center;"> 
		Entra come Dipendente&nbsp;<i class="bi bi-box-arrow-in-right"></i></h4>
	</button>
	</div>
	
	<div class="container-sm my-4 login" style=" max-width: 70%;">
	<button class="btn btn-primary login" style="background-color: #e4204e; width: 100%;">
				<h4 class="h4 py-4" style="color: white; text-align: center;"> 
		Entra come Admin&nbsp;<i class="bi bi-box-arrow-in-right"></i></h4>
	</button>
	</div>

</div>


<footer><%@ include file="footer.html" %></footer>
</body>
</html>