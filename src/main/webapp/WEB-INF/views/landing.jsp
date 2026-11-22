<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <title>Gestion Med</title>
    <%@ include file="fragments/head.jspf" %>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/landing.css">
</head>
<body data-context="${pageContext.request.contextPath}">
<%@ include file="fragments/header.jspf" %>

<main>
    <section class="hero">
        <div class="container">
            <h1>Les fiches patients de l'hôpital, tenues par les médecins, lisibles par les patients.</h1>
            <p class="lead">Le médecin enregistre l'état civil du patient une seule fois, le retrouve en tapant
                son nom, puis lui ouvre un accès pour qu'il relise sa fiche quand il le souhaite.</p>
            <div class="hero-actions">
                <a class="btn btn-primary btn-lg" href="${pageContext.request.contextPath}/login">Espace personnel</a>
                <a class="btn btn-secondary btn-lg" href="${pageContext.request.contextPath}/login#patient">Espace patient</a>
            </div>
        </div>
    </section>

    <c:set var="icons" value="${pageContext.request.contextPath}/assets/icons" />
    <section class="spaces" aria-labelledby="spaces-title">
        <div class="container">
            <h2 id="spaces-title">Ce que chacun peut faire</h2>
            <div class="spaces-grid">
                <div class="space">
                    <div class="space-head">
                        <span class="icon icon-lg" style="--icon: url('${icons}/doctor-outline.svg')" aria-hidden="true"></span>
                        <h3>Médecin</h3>
                    </div>
                    <ul class="features">
                        <li><span class="icon" style="--icon: url('${icons}/medical-search-outline.svg')" aria-hidden="true"></span>
                            Retrouver un patient en tapant une partie de son nom.</li>
                        <li><span class="icon" style="--icon: url('${icons}/medical-records-outline.svg')" aria-hidden="true"></span>
                            Enregistrer, modifier ou supprimer la fiche d'un patient.</li>
                        <li><span class="icon" style="--icon: url('${icons}/ui-user-profile-outline.svg')" aria-hidden="true"></span>
                            Créer l'accès du patient avec son email et un mot de passe à lui remettre.</li>
                    </ul>
                </div>
                <div class="space">
                    <div class="space-head">
                        <span class="icon icon-lg" style="--icon: url('${icons}/regular-patient-outline.svg')" aria-hidden="true"></span>
                        <h3>Patient</h3>
                    </div>
                    <ul class="features">
                        <li><span class="icon" style="--icon: url('${icons}/ui-user-profile-outline.svg')" aria-hidden="true"></span>
                            Se connecter avec l'email et le mot de passe donnés par son médecin.</li>
                        <li><span class="icon" style="--icon: url('${icons}/ui-folder-family-outline.svg')" aria-hidden="true"></span>
                            Relire sa fiche et signaler une erreur au médecin lors de la prochaine visite.</li>
                    </ul>
                </div>
            </div>
        </div>
    </section>
</main>

<%@ include file="fragments/footer.jspf" %>
<script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
</body>
</html>
