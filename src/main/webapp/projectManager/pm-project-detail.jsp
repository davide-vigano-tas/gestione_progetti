<%@page import="eu.tasgroup.gestione.businesscomponent.model.ProjectTask"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.enumerated.Fase"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.Project"%>
<%@page import="java.util.List"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.Role"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.facade.ProjectManagerFacade"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Arrays"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%
if (session.getAttribute("username") != null) {
    String username = (String) session.getAttribute("username");
    User user = ProjectManagerFacade.getInstance().getProjectManagerByUsername(username);
    Role[] roles = ProjectManagerFacade.getInstance().getRolesById(user.getId());
    if (Arrays.asList(roles).stream().anyMatch(r -> r.getRole().equals(Ruoli.PROJECT_MANAGER))) {
        String id = request.getParameter("id");
        long project_id = Long.parseLong(id);
        Project retrieved = ProjectManagerFacade.getInstance().getProjectById(project_id);
        SimpleDateFormat formato = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss");
%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="../cdn.html" %>
    <title>Tas home</title>
    <link rel="stylesheet" href="/<%=application.getServletContextName()%>/css/style.css">
    <style>
        .content-section {
            margin-top: 20px;
            padding: 20px;
            background-color: #F8F9FA;
            border: 1px solid #ddd;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
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
        .login {
            border-radius: 30px;
        }
        .login:hover {
            border-radius: 20px;
            filter: brightness(110%);
        }
        .col-md-3>a {
            color: #03122F;
            border-radius: 25px;
            min-width: 7.5em;
            border: 0.5rem solid rgba(251, 123, 6, 1);
            transition: 0.7s;
        }
        .col-md-3>a:hover {
            color: #03122F;
            border-radius: 25px;
            border: 0.5rem solid #a70fff;
        }
    </style>
</head>
<jsp:include page="../nav.jsp" />
<body>
    <div class="container">
        <div class="card shadow-sm mt-5">
            <div class="card-header">
                <h4>Benvenuto <%=username%></h4>
            </div>
            <div class="card-body">
                <div class="row mb-3">
                    <div class="col-md-6">
                        <p><strong>Nome: </strong><%=user.getNome()%></p>
                        <p><strong>Cognome: </strong><%=user.getCognome()%></p>
                        <p><strong>Username: </strong><%=username%></p>
                        <p><strong>Email: </strong><%=user.getEmail()%></p>
                    </div>
                    <div class="col-md-6">
                        <p><strong>Tentativi Errati: </strong><%=user.getTentativiFalliti()%></p>
                        <p><strong>Account Bloccato: </strong><%=user.isLocked()%></p>
                        <p><strong>Data Creazione: </strong><%=user.getDataCreazione().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)%></p>
                    </div>
                </div>

                <hr>

                <div class="row">
                    <% for (Fase fase : Fase.values()) { %>
                    <div class="col-12 col-md-6 col-lg-4 mb-3">
                        <div class="card h-100">
                            <div class="card-header">
                                <h5 class="card-title"><%=fase%></h5>
                            </div>
                            <div class="card-body">
                                <% for(ProjectTask task : ProjectManagerFacade.getInstance().getTaskByFaseAndProject(fase, retrieved.getId())) { %>
                                <div>
                                    <p><%=task.getNomeTask() %> &nbsp; <%=task.getStato() %></p>
                                </div>
                                <% } %>
                            </div>
                        </div>
                    </div>
                    <% } %>
                </div>
            </div>
        </div>
    </div>

    <footer><%@ include file="../footer.html" %></footer>
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
