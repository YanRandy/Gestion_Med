<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<% pageContext.setAttribute("hideLoginLink", true); %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <title>Connexion · Gestion Med</title>
    <%@ include file="fragments/head.jspf" %>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/login.css">
</head>
<body data-context="${pageContext.request.contextPath}">
<%@ include file="fragments/header.jspf" %>

<main class="login-page">
    <div class="container">
        <h1>Connexion</h1>
        <p class="lead">Choisissez votre espace, puis connectez-vous avec votre adresse email.</p>
        <%@ include file="fragments/auth-slider.jspf" %>

        <%-- Comptes de test (voir sql/donnees_test.sql) : à retirer en production --%>
        <section class="test-accounts" aria-labelledby="test-title">
            <h2 id="test-title">Comptes de test</h2>
            <p class="lead">Cliquez sur un compte pour remplir le formulaire.</p>
            <ul class="test-list">
                <li>
                    <button type="button" class="test-account" data-fill="staff"
                            data-email="medecin1@hopital.mg" data-password="medecin123">
                        <strong>Médecin 1</strong>
                        <span>medecin1@hopital.mg</span>
                        <span>medecin123</span>
                    </button>
                </li>
                <li>
                    <button type="button" class="test-account" data-fill="staff"
                            data-email="medecin2@hopital.mg" data-password="medecin123">
                        <strong>Médecin 2</strong>
                        <span>medecin2@hopital.mg</span>
                        <span>medecin123</span>
                    </button>
                </li>
                <li>
                    <button type="button" class="test-account" data-fill="patient"
                            data-email="patient@hopital.mg" data-password="patient123">
                        <strong>Patient (Jean Rakoto)</strong>
                        <span>patient@hopital.mg</span>
                        <span>patient123</span>
                    </button>
                </li>
            </ul>
        </section>
    </div>
</main>

<%@ include file="fragments/footer.jspf" %>
<script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/auth.js"></script>
</body>
</html>
