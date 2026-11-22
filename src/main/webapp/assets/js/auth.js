/* Formulaire de connexion coulissant (personnel / patient) + envoi vers /api/login. */
(function () {
    var contextPath = document.body.getAttribute("data-context") || "";
    var slider = document.querySelector("[data-auth-slider]");

    // --- Bascule entre les deux panneaux ---
    function show(side) {
        if (!slider) return;
        var patient = side === "patient";
        slider.classList.toggle("is-patient", patient);
        // Le panneau caché n'est plus atteignable au clavier
        slider.querySelector('[data-pane="staff"]').inert = patient;
        slider.querySelector('[data-pane="patient"]').inert = !patient;
        slider.querySelector('[data-overlay="to-patient"]').inert = patient;
        slider.querySelector('[data-overlay="to-staff"]').inert = !patient;
    }

    document.querySelectorAll("[data-show]").forEach(function (button) {
        button.addEventListener("click", function () {
            var side = button.getAttribute("data-show");
            show(side);
            var input = slider && slider.querySelector('[data-pane="' + side + '"] input');
            if (input) input.focus({ preventScroll: button.tagName === "BUTTON" });
        });
    });

    show(location.hash === "#patient" ? "patient" : "staff");

    // --- Comptes de test : remplit le bon formulaire et bascule dessus ---
    document.querySelectorAll("[data-fill]").forEach(function (button) {
        button.addEventListener("click", function () {
            var side = button.getAttribute("data-fill");
            var pane = slider.querySelector('[data-pane="' + side + '"]');
            show(side);
            pane.querySelector('input[type="email"]').value = button.getAttribute("data-email");
            pane.querySelector('input[name="password"]').value = button.getAttribute("data-password");
            pane.querySelector('button[type="submit"]').focus({ preventScroll: true });
        });
    });

    // --- Afficher / masquer le mot de passe ---
    document.querySelectorAll("[data-password-toggle]").forEach(function (toggle) {
        var input = document.getElementById(toggle.getAttribute("aria-controls"));
        toggle.addEventListener("click", function () {
            var visible = input.type === "text";
            input.type = visible ? "password" : "text";
            toggle.textContent = visible ? "Afficher" : "Masquer";
            toggle.setAttribute("aria-pressed", String(!visible));
        });
    });

    // --- Envoi des formulaires de connexion ---
    document.querySelectorAll("form[data-login]").forEach(function (form) {
        var email = form.querySelector('input[type="email"]');
        var password = form.querySelector('input[name="password"]');
        var submit = form.querySelector('button[type="submit"]');
        var errorBox = form.querySelector(".alert");
        var label = submit.textContent;

        function showError(message) {
            errorBox.textContent = message;
            errorBox.classList.add("visible");
        }

        function setLoading(loading) {
            submit.disabled = loading;
            submit.textContent = loading ? "Connexion en cours…" : label;
        }

        form.addEventListener("submit", function (event) {
            event.preventDefault();
            errorBox.classList.remove("visible");

            if (!email.value.trim() || !password.value) {
                showError("Renseignez votre adresse email et votre mot de passe.");
                (email.value.trim() ? password : email).focus();
                return;
            }

            setLoading(true);
            var body = new URLSearchParams();
            body.append("email", email.value.trim());
            body.append("password", password.value);

            fetch(contextPath + "/api/login", {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8" },
                body: body.toString(),
                credentials: "same-origin"
            })
                .then(function (response) { return response.json(); })
                .then(function (data) {
                    if (data.success) {
                        window.location.href = contextPath + data.redirect;
                        return;
                    }
                    setLoading(false);
                    showError(data.message || "Connexion refusée.");
                    password.value = "";
                    password.focus();
                })
                .catch(function () {
                    setLoading(false);
                    showError("Le serveur ne répond pas. Vérifiez votre connexion puis réessayez.");
                });
        });
    });
})();
