<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <title>Mon dossier · Gestion Med</title>
    <%@ include file="fragments/head.jspf" %>
</head>
<% pageContext.setAttribute("activeNav", "dossier"); %>
<body class="app" data-context="${pageContext.request.contextPath}">
<%@ include file="fragments/header.jspf" %>

<main class="app-page">
    <div class="container">
        <div class="page-head">
            <div>
                <h1 id="title">Mon dossier</h1>
                <p class="lead">Voici la fiche que l'hôpital a enregistrée pour vous.
                    Si une information est fausse, signalez-la à votre médecin lors de votre prochaine visite.</p>
            </div>
        </div>

        <div class="alert alert-error" id="error" role="alert"></div>
        <div class="surface surface-pad" id="record" aria-live="polite"><p class="muted">Chargement de votre fiche…</p></div>
    </div>
</main>

<%@ include file="fragments/footer.jspf" %>
<script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
<script>
    (function () {
        var record = document.getElementById("record");
        var error = document.getElementById("error");

        Promise.all([App.api("/api/me"), App.loadReferences()]).then(function (results) {
            var me = results[0];
            var patient = me.patient;
            if (!me.success || !patient) {
                record.innerHTML = "";
                error.textContent = me.message || "Aucune fiche n'est reliée à votre compte. Adressez-vous à votre médecin.";
                error.classList.add("visible");
                return;
            }
            document.getElementById("title").textContent =
                "Dossier de " + [patient.prenoms, patient.nom].filter(Boolean).join(" ");
            record.innerHTML = App.renderRecord(patient, results[1]);
        });
    })();
</script>
</body>
</html>
