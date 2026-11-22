/* Page /patients (médecin) : liste + recherche, fiche, création d'un patient et de son accès. */
(function () {
    var refs = { nationaliteById: {}, situationById: {} };
    var current = null;   // patient affiché dans la fiche
    var editing = false;  // le formulaire sert à créer (false) ou à modifier (true)
    var searchTimer = null;

    var rows = document.getElementById("rows");
    var empty = document.getElementById("empty");
    var count = document.getElementById("count");
    var search = document.getElementById("search");

    var detailDialog = document.getElementById("detail-dialog");
    var createDialog = document.getElementById("create-dialog");
    var createForm = document.getElementById("create-form");
    var accessForm = document.getElementById("access-form");
    var deleteDialog = document.getElementById("delete-dialog");
    var pageSuccess = document.getElementById("page-success");

    function showAlert(box, message) {
        box.textContent = message;
        box.classList.add("visible");
    }

    function hideAlert(box) {
        box.textContent = "";
        box.classList.remove("visible");
    }

    function setBusy(button, busy, busyLabel) {
        if (!button.dataset.label) button.dataset.label = button.textContent;
        button.disabled = busy;
        button.textContent = busy ? busyLabel : button.dataset.label;
    }

    // --- Liste ---

    function load() {
        var query = search.value.trim();
        App.api("/api/patients?nom=" + encodeURIComponent(query)).then(function (data) {
            var patients = data.patients || [];
            rows.innerHTML = patients.map(function (p) {
                return '<tr data-id="' + p.id + '">' +
                    '<td><button type="button" class="link-button" data-open="' + p.id + '">' + App.escape(p.nom) + "</button></td>" +
                    "<td class=\"hide-sm\">" + App.escape(p.prenoms || "") + "</td>" +
                    "<td>" + App.formatDate(p.dateNaissance) + "</td>" +
                    '<td class="hide-sm">' + (p.sexe === "F" ? "F" : "M") + "</td>" +
                    '<td class="hide-sm">' + App.escape(p.numCin || "") + "</td>" +
                    "<td>" + (p.idUser
                        ? '<span class="badge badge-ok">Actif</span>'
                        : '<span class="badge">Aucun</span>') + "</td>" +
                    "</tr>";
            }).join("");

            if (!data.success) {
                count.textContent = data.message || "Impossible de charger les patients.";
            } else if (query) {
                count.textContent = patients.length + " résultat" + (patients.length > 1 ? "s" : "") + " pour « " + query + " »";
            } else {
                count.textContent = patients.length + " patient" + (patients.length > 1 ? "s" : "") + " enregistré" + (patients.length > 1 ? "s" : "");
            }

            empty.hidden = patients.length > 0 || !data.success;
            empty.textContent = query
                ? "Aucun patient ne correspond à « " + query + " ». Vérifiez l'orthographe ou enregistrez un nouveau patient."
                : "Aucun patient enregistré. Commencez par « Nouveau patient ».";
        });
    }

    search.addEventListener("input", function () {
        clearTimeout(searchTimer);
        searchTimer = setTimeout(load, 250);
    });

    // Clic sur une ligne (ou sur le nom) → fiche
    rows.addEventListener("click", function (event) {
        var tr = event.target.closest("tr[data-id]");
        if (tr) openDetail(Number(tr.getAttribute("data-id")));
    });

    // --- Fiche patient ---

    function openDetail(id, successMessage) {
        App.api("/api/patients/detail?id=" + id).then(function (data) {
            if (!data.success) {
                count.textContent = data.message;
                return;
            }
            current = data.patient;
            document.getElementById("detail-title").textContent =
                [current.nom, current.prenoms].filter(Boolean).join(" ");
            document.getElementById("detail-record").innerHTML = App.renderRecord(current, refs);

            var success = document.getElementById("detail-success");
            if (successMessage) showAlert(success, successMessage); else hideAlert(success);

            var hasAccess = Boolean(current.idUser);
            document.getElementById("access-none").hidden = hasAccess;
            document.getElementById("access-done").hidden = !hasAccess;
            accessForm.reset();
            accessForm.email.value = current.email || "";
            hideAlert(accessForm.querySelector(".alert"));

            if (!detailDialog.open) detailDialog.showModal();
        });
    }

    accessForm.addEventListener("submit", function (event) {
        event.preventDefault();
        var error = accessForm.querySelector(".alert");
        var submit = accessForm.querySelector('button[type="submit"]');
        hideAlert(error);

        if (!accessForm.email.value.trim() || !accessForm.password.value) {
            showAlert(error, "Renseignez l'email et le mot de passe du patient.");
            return;
        }
        setBusy(submit, true, "Création…");
        App.api("/api/patients/acces", {
            idPatient: current.id,
            email: accessForm.email.value,
            password: accessForm.password.value
        }).then(function (data) {
            setBusy(submit, false);
            if (!data.success) {
                showAlert(error, data.message || "L'accès n'a pas pu être créé.");
                return;
            }
            openDetail(current.id, "Accès créé : " + data.user.email + ". Remettez le mot de passe au patient.");
            load();
        });
    });

    // --- Nouveau patient ---

    function fillSelect(select, items) {
        select.innerHTML = '<option value="">Non renseigné</option>' + items.map(function (item) {
            return '<option value="' + item.id + '">' + App.escape(item.libelle) + "</option>";
        }).join("");
    }

    // Même formulaire pour créer et modifier : on change titre, bouton et contenu
    function openForm(patient) {
        editing = Boolean(patient);
        hideAlert(pageSuccess);
        createForm.reset();
        hideAlert(createForm.querySelector(".alert"));
        document.getElementById("create-title").textContent = editing ? "Modifier la fiche" : "Nouveau patient";
        var submit = createForm.querySelector('button[type="submit"]');
        submit.dataset.label = editing ? "Enregistrer les modifications" : "Enregistrer le patient";
        submit.textContent = submit.dataset.label;

        if (editing) {
            Array.prototype.forEach.call(createForm.elements, function (field) {
                if (!field.name) return;
                var value = patient[field.name];
                if (field.type === "radio") field.checked = field.value === value;
                else if (field.type === "checkbox") field.checked = Boolean(value);
                else field.value = value === null || value === undefined ? "" : value;
            });
        }
        createDialog.showModal();
        createForm.querySelector(".dialog-body").scrollTop = 0;
        createForm.nom.focus();
    }

    document.getElementById("open-create").addEventListener("click", function () {
        openForm(null);
    });

    document.getElementById("open-edit").addEventListener("click", function () {
        detailDialog.close();
        openForm(current);
    });

    createForm.addEventListener("submit", function (event) {
        event.preventDefault();
        var error = createForm.querySelector(".alert");
        var submit = createForm.querySelector('button[type="submit"]');
        hideAlert(error);

        var missing = [];
        if (!createForm.nom.value.trim()) missing.push("le nom");
        if (!createForm.sexe.value) missing.push("le sexe");
        if (!createForm.dateNaissance.value) missing.push("la date de naissance");
        if (missing.length) {
            showAlert(error, "Renseignez " + missing.join(", ") + ".");
            createForm.querySelector(".dialog-body").scrollTop = 0;
            return;
        }

        // create(Patient patient, ApplicationContext ctx) a 2 paramètres :
        // le framework attend donc les champs préfixés ("patient.nom", "patient.sexe", ...)
        var params = {};
        new FormData(createForm).forEach(function (value, key) { params["patient." + key] = value; });
        if (editing) params["patient.id"] = current.id;

        setBusy(submit, true, "Enregistrement…");
        App.api(editing ? "/api/patients/modifier" : "/api/patients", params).then(function (data) {
            setBusy(submit, false);
            if (!data.success) {
                showAlert(error, data.message || "La fiche n'a pas pu être enregistrée.");
                createForm.querySelector(".dialog-body").scrollTop = 0;
                return;
            }
            createDialog.close();
            load();
            openDetail(data.patient.id, editing
                ? "Fiche modifiée."
                : "Patient enregistré. Vous pouvez maintenant créer son accès.");
        });
    });

    // --- Suppression ---

    document.getElementById("open-delete").addEventListener("click", function () {
        var name = [current.nom, current.prenoms].filter(Boolean).join(" ");
        document.getElementById("delete-text").textContent =
            "La fiche de " + name + " sera supprimée" +
            (current.idUser ? ", ainsi que son accès patient" : "") +
            ". Cette action est définitive.";
        hideAlert(document.getElementById("delete-error"));
        deleteDialog.showModal();
    });

    document.getElementById("confirm-delete").addEventListener("click", function () {
        var button = this;
        var name = [current.nom, current.prenoms].filter(Boolean).join(" ");
        setBusy(button, true, "Suppression…");
        App.api("/api/patients/supprimer", { id: current.id }).then(function (data) {
            setBusy(button, false);
            if (!data.success) {
                showAlert(document.getElementById("delete-error"), data.message || "La suppression a échoué.");
                return;
            }
            deleteDialog.close();
            detailDialog.close();
            showAlert(pageSuccess, "Fiche de " + name + " supprimée.");
            load();
        });
    });

    // --- Fermeture des fenêtres ---

    document.querySelectorAll("[data-close]").forEach(function (button) {
        button.addEventListener("click", function () {
            button.closest("dialog").close();
        });
    });

    // Clic sur le fond sombre → ferme
    [detailDialog, createDialog, deleteDialog].forEach(function (dialog) {
        dialog.addEventListener("click", function (event) {
            if (event.target === dialog) dialog.close();
        });
    });

    // --- Démarrage ---

    App.loadReferences().then(function (loaded) {
        refs = loaded;
        fillSelect(createForm.idSituationMatrimoniale, loaded.situations);
        fillSelect(createForm.idNationalite, loaded.nationalites);
    });
    load();
})();
