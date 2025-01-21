<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" errorPage="../error.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="cdn.html" %>
<title>Verifica OTP</title>
<link rel="stylesheet" href="/<%=application.getServletContextName()%>/css/style.css">
<style>
.rounded-panel {
    border: 0px;
    border-radius: 5px;
    overflow: hidden;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
    width: 80%;
    max-width: 600px;
    margin: 30px auto;
}

.panel-heading {
    border-top-left-radius: 5px;
    border-top-right-radius: 5px;
}

.panel-body {
    border-radius: 0;
    padding: 30px;
}
</style>
</head>
<body>
    <jsp:include page="nav.jsp" />
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <div class="panel panel-primary rounded-panel">
                    <div class="panel-heading text-center">
                        <h3 class="panel-title mt-4">Verifica OTP</h3>
                    </div>
                    <div class="panel-body">
                        <!-- Messaggi di errore -->
                        <%
                            String error = request.getParameter("error");
                            if (error != null) {
                        %>
                        <div id="error" class="alert alert-danger">
                           Il codice OTP è errato, riprova.
                        </div>
                        <% } %>

                        <!-- Form per l'inserimento dell'OTP -->
                        <form action="/<%=application.getServletContextName()%>/verify-otp" method="post" class="form-horizontal">
                            <div class="form-group mb-3">
                                <label for="otp" class="form-label">Inserisci il codice OTP</label>
                                <div class="input-group">
                                    <span class="input-group-text">
                                        <i class="bi bi-key"></i>
                                    </span>
                                    <input type="text" id="otp" name="otp" class="form-control" placeholder="Codice OTP" maxlength="6" required>
                                </div>
                            </div>
                            <div class="form-group">
                                <button type="submit" class="btn btn-primary w-100">
                                    Verifica&nbsp;&nbsp;<i class="bi bi-check-circle"></i>
                                </button>
                            </div>
                        </form>

                        <!-- Messaggio di aiuto -->
                        <div class="mt-3 text-muted text-center">
                            <p>Non hai ricevuto il codice? <a href="#">Reinvia OTP</a></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
