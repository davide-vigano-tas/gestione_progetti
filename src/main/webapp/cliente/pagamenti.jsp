<%@page import="java.util.Arrays"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.User"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.Payment"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.model.Project"%>
<%@page import="eu.tasgroup.gestione.businesscomponent.facade.ClienteFacade"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
if (session.getAttribute("username") != null) {
    String username = (String) session.getAttribute("username");
    User user = ClienteFacade.getInstance().getByUsername(username);
    
    String projectIdParam = request.getParameter("id");
    if (projectIdParam != null) {
        long projectId = Long.parseLong(projectIdParam);
        Project project = ClienteFacade.getInstance().getProjectById(projectId);
        List<Payment> payments = Arrays.asList(ClienteFacade.getInstance().getPaymentByProject(project));
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        double totalPaid = payments.stream().mapToDouble(Payment::getCifra).sum();
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <title>Pagamenti Progetto</title>
    <%@ include file="../cdn.html"%>
    <link rel="stylesheet" href="/<%=application.getServletContextName()%>/css/style.css">
</head>
<body>
    <jsp:include page="../nav.jsp" />
    <div class="container mt-5">
        <div class="card shadow-sm">
            <div class="card-header">
                <h4>Pagamenti per il progetto: <%= project.getNomeProgetto() %></h4>
            </div>
            <div class="card-body">
                <p><strong>ID Progetto:</strong> <%= project.getId() %></p>
                <p><strong>Nome:</strong> <%= project.getNomeProgetto() %></p>
                <p><strong>Costo Totale:</strong> € <%= project.getCostoProgetto() %></p>
                <p><strong>Totale Pagato:</strong> € <%= totalPaid %></p>
                
                <table class="table table-hover mt-4">
                    <thead>
                        <tr>
                            <th>ID Pagamento</th>
                            <th>ID Progetto</th>
                            <th>Importo (€)</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                        if (!payments.isEmpty()) {
                            for (Payment payment : payments) {
                        %>
                        <tr>
                            <td><%= payment.getId() %></td>
                            <td><%= payment.getIdProgetto() %></td>
                            <td><%= payment.getCifra() %></td>
                        </tr>
                        <%
                            }
                        } else {
                        %>
                        <tr>
                            <td colspan="3" class="text-center">Nessun pagamento registrato per questo progetto.</td>
                        </tr>
                        <%
                        }
                        %>
                    </tbody>
                </table>
            </div>
            <a href="<%= application.getContextPath() %>/cliente/cliente-home.jsp" class="btn btn-secondary">Torna alla Home</a>
        </div>
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
