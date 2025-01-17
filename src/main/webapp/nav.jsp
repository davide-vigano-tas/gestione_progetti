
<jsp:useBean id="carrello" class="eu.tasgroup.ordini.businesscomponent.utility.Carrello" scope="session"/>
<nav class="navbar navbar-inverse">
<!-- Riempie fin al bordo -->
	<div class="container-fluid">
		<div class="navbar-header">
		<!-- Per apreire e chiudere mnu quando ristretto. Deve interagire con l'evento toggle -->
			<button type="button" class="navbar-toggle" data-toggle="collapse"
			  data-target="#menu"><!-- Nome dle menu -->
			  <span class="icon-bar"></span>
			  <span class="icon-bar"></span>
			  <span class="icon-bar"></span>
		    </button> 
		    <a class="navbar-brand" href="<%= application.getContextPath() %>/home.jsp"
		    style="display: flex; align-items: center;">
		    	<img src="img/new-logo.png" alt="logo" style="height: 30px; margin-right: 20px; margin-left: 10px;">
		    	Morpheus' Store
		    </a>
		    
		</div>
		<div class="collapse navbar-collapse" id = "menu">
			<%
				String username = (String) session.getAttribute("username");
				String img = (String) session.getAttribute("img_user");
				if(username == null) {
			%>
				<!-- voci di menu -->
				<ul class="nav navbar-nav navbar-right">
					<li>
					 <a  href="<%= application.getContextPath() %>/registrazione.jsp">
					 	<span class="glyphicon glyphicon-user"></span>
					 	Sign up
					 </a>
					</li>
					<li>
					 <a  href="<%= application.getContextPath() %>/login.jsp">
					 	<span class="glyphicon glyphicon-log-in"></span>
					 	Login
					 </a>
					</li>
				</ul>
			<%
				} else {
			%>
				<ul class="nav navbar-nav">
					<li>
					 <a  href="<%= application.getContextPath() %>/acquisti.jsp">
					 	
					 	Scelta articoli
					 </a>
					</li>
					<li>
					 <a  href="<%= application.getContextPath() %>/carrello.jsp">
					 	Riepilogo carrello
					 </a>
					</li>
				</ul>
		       <ul class="nav navbar-nav navbar-right">
					<li>
					 <a  href="<%= application.getContextPath()%>/carrello.jsp">
					 	<span class="glyphicon glyphicon-shopping-cart"></span>
					 	<span class="badge"><%= carrello.getArticoli() %></span>
					 </a>
					</li>
					<li>
					 <a  href="<%= application.getContextPath() %>/account.jsp">
					 
					 <%
					 	if(img == null) {
					 		
					 %>
					 <span class="glyphicon glyphicon-user"></span>
					 <% } else { %>
					 	<img alt="Profile Image" src="<%= img %>" style="width: 25px; height: 25px; border-radius: 50%;">
					 	<% } %>
					 	<%=  username %>
					 </a>
					</li>
					<li>
					 <a  href="<%= application.getContextPath() %>/logout.jsp">
					 	<span class="glyphicon glyphicon-off"></span>
					 	Logout
					 </a>
					</li>
				</ul>
			<%
				}
			%>
		</div>
	</div>
</nav>