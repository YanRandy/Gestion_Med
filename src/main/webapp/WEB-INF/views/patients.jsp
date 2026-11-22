<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <title>Patients · Gestion Med</title>
    <%@ include file="fragments/head.jspf" %>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/patients.css">
</head>
<% pageContext.setAttribute("activeNav", "patients"); %>
<body class="app" data-context="${pageContext.request.contextPath}">
<%@ include file="fragments/header.jspf" %>

<main class="app-page">
    <div class="container">
        <div class="page-head">
            <div>
                <h1>Patients</h1>
                <p class="muted" id="count" aria-live="polite">Chargement…</p>
            </div>
            <button type="button" class="btn btn-primary" id="open-create">Nouveau patient</button>
        </div>

        <div class="alert alert-success" id="page-success" role="status"></div>
        <div class="surface">
        <div class="toolbar">
            <label class="visually-hidden" for="search">Rechercher un patient</label>
            <input class="input search" type="search" id="search" placeholder="Rechercher par nom, prénoms ou nom d'usage" autocomplete="off">
        </div>

        <div class="table-wrap">
            <table class="table">
                <thead>
                <tr>
                    <th scope="col">Nom</th>
                    <th scope="col" class="hide-sm">Prénoms</th>
                    <th scope="col">Né(e) le</th>
                    <th scope="col" class="hide-sm">Sexe</th>
                    <th scope="col" class="hide-sm">N° CIN</th>
                    <th scope="col">Accès patient</th>
                </tr>
                </thead>
                <tbody id="rows"></tbody>
            </table>
        </div>
        <p class="empty" id="empty" hidden></p>
        </div>
    </div>
</main>

<%-- Fiche d'un patient + création de son accès --%>
<dialog class="dialog" id="detail-dialog" aria-labelledby="detail-title">
    <div class="dialog-header">
        <h2 id="detail-title">Fiche patient</h2>
        <button type="button" class="dialog-close" data-close aria-label="Fermer">&times;</button>
    </div>
    <div class="dialog-body">
        <div class="alert alert-success" id="detail-success" role="status"></div>
        <div id="detail-record"></div>

        <section class="access" id="access-none" aria-labelledby="access-title">
            <h3 id="access-title">Créer l'accès patient</h3>
            <p class="muted">Le patient pourra consulter sa fiche avec cet email et ce mot de passe.
                Remettez-les-lui pendant la consultation.</p>
            <form id="access-form" novalidate>
                <div class="alert alert-error" role="alert"></div>
                <div class="form-grid">
                    <div class="field">
                        <label for="access-email">Email du patient</label>
                        <input class="input" type="email" id="access-email" name="email" autocomplete="off" required>
                    </div>
                    <div class="field">
                        <label for="access-password">Mot de passe</label>
                        <input class="input" type="text" id="access-password" name="password" autocomplete="off" required>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary">Créer l'accès</button>
            </form>
        </section>
        <p class="access access-done" id="access-done"><span class="badge badge-ok">Accès actif</span>
            Ce patient peut déjà consulter sa fiche.</p>
    </div>
    <div class="dialog-footer">
        <button type="button" class="btn btn-ghost-danger" id="open-delete">Supprimer le patient</button>
        <button type="button" class="btn btn-primary" id="open-edit">Modifier la fiche</button>
    </div>
</dialog>

<%-- Confirmation de suppression --%>
<dialog class="dialog dialog-sm" id="delete-dialog" aria-labelledby="delete-title">
    <div class="dialog-header">
        <h2 id="delete-title">Supprimer ce patient&nbsp;?</h2>
    </div>
    <div class="dialog-body">
        <div class="alert alert-error" id="delete-error" role="alert"></div>
        <p id="delete-text"></p>
    </div>
    <div class="dialog-footer">
        <button type="button" class="btn btn-secondary" data-close>Annuler</button>
        <button type="button" class="btn btn-danger" id="confirm-delete">Supprimer définitivement</button>
    </div>
</dialog>

<%-- Création d'un patient --%>
<dialog class="dialog" id="create-dialog" aria-labelledby="create-title">
    <form id="create-form" novalidate>
        <div class="dialog-header">
            <h2 id="create-title">Nouveau patient</h2>
            <button type="button" class="dialog-close" data-close aria-label="Fermer">&times;</button>
        </div>
        <div class="dialog-body">
            <div class="alert alert-error" role="alert"></div>

            <fieldset class="fieldset">
                <legend>Identité</legend>
                <div class="form-grid">
                    <div class="field">
                        <label for="nom">Nom</label>
                        <input class="input" id="nom" name="nom" required>
                    </div>
                    <div class="field">
                        <label for="prenoms">Prénoms <span class="optional">(facultatif)</span></label>
                        <input class="input" id="prenoms" name="prenoms">
                    </div>
                    <div class="field">
                        <label for="nomUsage">Nom d'usage <span class="optional">(facultatif)</span></label>
                        <input class="input" id="nomUsage" name="nomUsage">
                    </div>
                    <div class="field">
                        <span class="label" id="sexe-label">Sexe</span>
                        <div class="choices" role="radiogroup" aria-labelledby="sexe-label">
                            <label class="choice"><input type="radio" name="sexe" value="M" required> Masculin</label>
                            <label class="choice"><input type="radio" name="sexe" value="F"> Féminin</label>
                        </div>
                    </div>
                </div>
            </fieldset>

            <fieldset class="fieldset">
                <legend>Naissance</legend>
                <div class="form-grid">
                    <div class="field">
                        <label for="dateNaissance">Date de naissance</label>
                        <input class="input" type="date" id="dateNaissance" name="dateNaissance" required>
                    </div>
                    <div class="field">
                        <label for="lieuNaissance">Lieu de naissance <span class="optional">(facultatif)</span></label>
                        <input class="input" id="lieuNaissance" name="lieuNaissance">
                    </div>
                    <div class="field full">
                        <label class="choice"><input type="checkbox" name="estDatePresumee" value="true">
                            La date exacte n'est pas connue (date présumée)</label>
                    </div>
                </div>
            </fieldset>

            <fieldset class="fieldset">
                <legend>Filiation <span class="optional">(facultatif)</span></legend>
                <div class="form-grid">
                    <div class="field">
                        <label for="nomPere">Nom du père</label>
                        <input class="input" id="nomPere" name="nomPere">
                    </div>
                    <div class="field">
                        <label for="prenomsPere">Prénoms du père</label>
                        <input class="input" id="prenomsPere" name="prenomsPere">
                    </div>
                    <div class="field">
                        <label for="nomMere">Nom de la mère</label>
                        <input class="input" id="nomMere" name="nomMere">
                    </div>
                    <div class="field">
                        <label for="prenomsMere">Prénoms de la mère</label>
                        <input class="input" id="prenomsMere" name="prenomsMere">
                    </div>
                </div>
            </fieldset>

            <fieldset class="fieldset">
                <legend>Situation <span class="optional">(facultatif)</span></legend>
                <div class="form-grid">
                    <div class="field">
                        <label for="idSituationMatrimoniale">Situation matrimoniale</label>
                        <select class="input" id="idSituationMatrimoniale" name="idSituationMatrimoniale"></select>
                    </div>
                    <div class="field">
                        <label for="idNationalite">Nationalité</label>
                        <select class="input" id="idNationalite" name="idNationalite"></select>
                    </div>
                </div>
            </fieldset>

            <fieldset class="fieldset">
                <legend>Coordonnées <span class="optional">(facultatif)</span></legend>
                <div class="form-grid">
                    <div class="field full">
                        <label for="adresse">Adresse</label>
                        <input class="input" id="adresse" name="adresse">
                    </div>
                    <div class="field">
                        <label for="telephone">Téléphone</label>
                        <input class="input" type="tel" id="telephone" name="telephone">
                    </div>
                    <div class="field">
                        <label for="email">Email</label>
                        <input class="input" type="email" id="email" name="email">
                    </div>
                    <div class="field">
                        <label for="contactUrgenceNom">Contact d'urgence</label>
                        <input class="input" id="contactUrgenceNom" name="contactUrgenceNom">
                    </div>
                    <div class="field">
                        <label for="contactUrgenceTel">Téléphone du contact</label>
                        <input class="input" type="tel" id="contactUrgenceTel" name="contactUrgenceTel">
                    </div>
                </div>
            </fieldset>

            <fieldset class="fieldset">
                <legend>Pièce d'identité <span class="optional">(facultatif)</span></legend>
                <div class="form-grid">
                    <div class="field">
                        <label for="numCin">Numéro de CIN</label>
                        <input class="input" id="numCin" name="numCin">
                    </div>
                    <div class="field">
                        <label for="dateDelivranceCin">Délivrée le</label>
                        <input class="input" type="date" id="dateDelivranceCin" name="dateDelivranceCin">
                    </div>
                    <div class="field full">
                        <label for="lieuDelivranceCin">Délivrée à</label>
                        <input class="input" id="lieuDelivranceCin" name="lieuDelivranceCin">
                    </div>
                </div>
            </fieldset>
        </div>
        <div class="dialog-footer">
            <button type="button" class="btn btn-secondary" data-close>Annuler</button>
            <button type="submit" class="btn btn-primary">Enregistrer le patient</button>
        </div>
    </form>
</dialog>

<%@ include file="fragments/footer.jspf" %>
<script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/patients.js"></script>
</body>
</html>
