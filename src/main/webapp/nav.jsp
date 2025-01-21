
<%@page import="eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli"%>
<%@page import="java.util.Arrays"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.facade.AdminFacade"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.Role"%>
<%@page import="java.util.List"%>
<nav class="navbar navbar-expand-lg navbar-light bg-secondary">
    <div class="container-fluid">
        <a class="navbar-brand navbar-text" href="<%= application.getContextPath() %>/index.jsp" style="display: flex; align-items: center;">
       <%
       String username = (String) session.getAttribute("username");
       if (username == null) { %>
            <img src="img/sito-logo.png" alt="logo" style="height: 30px; margin-right: 20px; margin-left: 10px;">
    <% }  else {%>
    <img src="../img/sito-logo.png" alt="logo" style="height: 30px; margin-right: 20px; margin-left: 10px;">
    <% } %>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#menu" aria-controls="menu" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="menu">
            <%
				AdminFacade af = AdminFacade.getInstance();            
               
            	List<Role> roles = Arrays.asList(af.getRolesByUsername(username));
                if (username == null) {
            %>
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <a class="nav-link  navbar-text" href="<%= application.getContextPath() %>/registrazione.jsp">
                        <i class="bi bi-person-plus"></i> Sign up
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link navbar-text" href="<%= application.getContextPath() %>/login.jsp">
                        <i class="bi bi-box-arrow-in-right"></i> Login
                    </a>
                </li>
            </ul>
            <% } else {
                 if (roles.stream().anyMatch(r -> r.getRole().equals(Ruoli.CLIENTE))){
            %>
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link navbar-text" href="<%= application.getContextPath() %>/cliente/pagamenti.jsp">
                        I tuoi pagamenti
                    </a>
                </li>
            </ul>
                      
               <%
                } else if (roles.stream().anyMatch(r -> r.getRole().equals(Ruoli.DIPENDENTE))){
            %>
                        <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link navbar-text" href="<%= application.getContextPath() %>/dip/developer/tasks.jsp">
                        Tasks
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link navbar-text" href="<%= application.getContextPath() %>/dip/developer/timsheets.jsp">
                        Timesheets
                    </a>
                </li>
                
                <li class="nav-item">
                    <a class="nav-link navbar-text" href="<%= application.getContextPath() %>/dip/developer/skills.jsp">
                       Le tue skill
                    </a>
                </li>
            </ul>
                       
               <%
                } else if (roles.stream().anyMatch(r -> r.getRole().equals(Ruoli.PROJECT_MANAGER))){
            %>
                <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link navbar-text" href="<%= application.getContextPath() %>/dip/projman/dipendenti.jsp">
                        Dipendenti
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link navbar-text" href="<%= application.getContextPath() %>/dip/projman/progetti.jsp">
                        Progetti
                    </a>
                </li>
                
                <li class="nav-item">
                    <a class="nav-link navbar-text" href="<%= application.getContextPath() %>/dip/projman/timesheets.jsp">
                       Timesheets
                    </a>
                </li>
            </ul>
            
               <%
                } else if (roles.stream().anyMatch(r -> r.getRole().equals(Ruoli.ADMIN))){
            %>
                            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link navbar-text" href="<%= application.getContextPath() %>/admin/skills.jsp">
                        Skills
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link navbar-text" href="<%= application.getContextPath() %>/admin/users.jsp">
                        Utenti
                    </a>
                </li>
                
                <li class="nav-item">
                    <a class="nav-link navbar-text" href="<%= application.getContextPath() %>/admin/timesheets.jsp">
                       Timesheets
                    </a>
                </li>
                             
                <li class="nav-item">
                    <a class="nav-link navbar-text" href="<%= application.getContextPath() %>/admin/progetti.jsp">
                       Progetti
                    </a>
                </li>
                
                  <li class="nav-item">
                    <a class="nav-link navbar-text" href="<%= application.getContextPath() %>/admin/pagamenti.jsp">
                       Pagamenti
                    </a>
                </li>
                    <li class="nav-item">
                    <a class="nav-link navbar-text" href="<%= application.getContextPath() %>/admin/auditlogs.jsp">
                       AuditLog
                    </a>
                </li>
            </ul>

            <%
                }
               
            %>
            <ul class="navbar-nav ms-auto">

					<li>
					 <a  href="<%= application.getContextPath() %>/admin/admin-home.jsp" class="nav-link navbar-text">
					 
				
					 	<%=  username %>
					 </a>
					</li>
        
                <li class="nav-item">
                    <a class="nav-link navbar-text" href="<%= application.getContextPath() %>/logout.jsp">
                        <i class="bi bi-power"></i> Logout
                    </a>
                </li>
            </ul>
            <%
                }
            %>
        </div>
    </div>
</nav>
