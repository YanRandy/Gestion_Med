/* Outils communs aux pages connectées : appels API, déconnexion, affichage d'une fiche patient. */
window.App = (function () {
    var contextPath = document.body.getAttribute("data-context") || "";

    // Appel API → toujours un objet JSON { success, ... } (même si le serveur renvoie une page d'erreur)
    function api(path, params) {
        var options = { credentials: "same-origin" };
        if (params) {
            var body = new URLSearchParams();
            Object.keys(params).forEach(function (key) {
                var value = params[key];
                if (value !== null && value !== undefined && String(value).trim() !== "") {
                    body.append(key, String(value).trim());
                }
            });
            options.method = "POST";
            options.headers = { "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8" };
            options.body = body.toString();
        }
        return fetch(contextPath + path, options).then(function (response) {
            if (response.status === 401) {
                window.location.href = contextPath + "/login";
            }
            return response.json().catch(function () {
                return { success: false, message: "Erreur du serveur (" + response.status + ")." };
            });
        }, function () {
            return { success: false, message: "Le serveur ne répond pas. Vérifiez votre connexion puis réessayez." };
        });
    }

    function escape(value) {
        return String(value)
            .replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;").replace(/'/g, "&#39;");
    }

    // "1990-05-14" → "14/05/1990"
    function formatDate(value) {
        if (!value) return "";
        var parts = String(value).split("-");
        return parts.length === 3 ? parts[2] + "/" + parts[1] + "/" + parts[0] : value;
    }

    function join() {
        return Array.prototype.filter.call(arguments, Boolean).join(" ");
    }

    // Listes de référence (nationalités, situations) → { id: libelle }
    function loadReferences() {
        return Promise.all([api("/api/nationalites"), api("/api/situations-matrimoniales")])
            .then(function (results) {
                function toMap(list) {
                    var map = {};
                    (list || []).forEach(function (item) { map[item.id] = item.libelle; });
                    return map;
                }
                return {
                    nationalites: results[0].nationalites || [],
                    situations: results[1].situations || [],
                    nationaliteById: toMap(results[0].nationalites),
                    situationById: toMap(results[1].situations)
                };
            });
    }

    // Fiche patient en lecture (HTML), utilisée par /patients et /mon-dossier
    function renderRecord(p, refs) {
        function row(label, value) {
            return "<dt>" + label + "</dt>" + (value
                ? "<dd>" + escape(value) + "</dd>"
                : '<dd class="missing">Non renseigné</dd>');
        }
        function section(title, rows) {
            return '<section class="record-section"><h3>' + title + "</h3><dl>" + rows.join("") + "</dl></section>";
        }
        var naissance = formatDate(p.dateNaissance) + (p.estDatePresumee ? " (date présumée)" : "");
        var urgence = join(p.contactUrgenceNom, p.contactUrgenceTel && "(" + p.contactUrgenceTel + ")");

        return '<div class="record">' +
            section("Identité", [
                row("Nom", p.nom),
                row("Prénoms", p.prenoms),
                row("Nom d'usage", p.nomUsage),
                row("Sexe", p.sexe === "F" ? "Féminin" : p.sexe === "M" ? "Masculin" : "")
            ]) +
            section("Naissance", [
                row("Date", naissance),
                row("Lieu", p.lieuNaissance)
            ]) +
            section("Filiation", [
                row("Père", join(p.nomPere, p.prenomsPere)),
                row("Mère", join(p.nomMere, p.prenomsMere))
            ]) +
            section("Situation", [
                row("Situation matrimoniale", refs.situationById[p.idSituationMatrimoniale]),
                row("Nationalité", refs.nationaliteById[p.idNationalite])
            ]) +
            section("Coordonnées", [
                row("Adresse", p.adresse),
                row("Téléphone", p.telephone),
                row("Email", p.email),
                row("Contact d'urgence", urgence)
            ]) +
            section("Pièce d'identité", [
                row("Numéro de CIN", p.numCin),
                row("Délivrée le", formatDate(p.dateDelivranceCin)),
                row("Délivrée à", p.lieuDelivranceCin)
            ]) +
            "</div>";
    }

    // Bouton "Se déconnecter" de l'en-tête
    document.querySelectorAll("[data-logout]").forEach(function (button) {
        button.addEventListener("click", function () {
            button.disabled = true;
            api("/api/logout", {}).then(function () {
                window.location.href = contextPath + "/";
            });
        });
    });

    return {
        api: api,
        escape: escape,
        formatDate: formatDate,
        loadReferences: loadReferences,
        renderRecord: renderRecord
    };
})();
