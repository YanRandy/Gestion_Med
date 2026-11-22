package com.hopital.validation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Accumule les erreurs d'un formulaire, puis les renvoie toutes d'un coup.
 *
 * <pre>
 * new Verification()
 *     .obligatoire(p.getNom(), "Le nom")
 *     .longueurMax(p.getNom(), 100, "Le nom")
 *     .lancer();   // IllegalArgumentException avec tous les messages s'il y en a
 * </pre>
 */
public class Verification {

    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern TELEPHONE = Pattern.compile("^\\+?[0-9 .-]{7,20}$");

    private final List<String> erreurs = new ArrayList<>();

    /** Ajoute le message si la condition est vraie. */
    public Verification si(boolean condition, String message) {
        if (condition) {
            erreurs.add(message);
        }
        return this;
    }

    public Verification obligatoire(Object valeur, String champ) {
        boolean vide = valeur == null || (valeur instanceof String s && s.isBlank());
        return si(vide, champ + " est obligatoire.");
    }

    public Verification longueurMax(String valeur, int max, String champ) {
        return si(valeur != null && valeur.length() > max,
                champ + " ne doit pas dépasser " + max + " caractères.");
    }

    public Verification longueurMin(String valeur, int min, String champ) {
        return si(valeur != null && valeur.length() < min,
                champ + " doit contenir au moins " + min + " caractères.");
    }

    public Verification email(String valeur, String champ) {
        return si(valeur != null && !EMAIL.matcher(valeur.trim()).matches(),
                champ + " n'est pas une adresse email valide.");
    }

    public Verification telephone(String valeur, String champ) {
        return si(valeur != null && !TELEPHONE.matcher(valeur.trim()).matches(),
                champ + " n'est pas un numéro valide (chiffres, espaces, + au début).");
    }

    /** La date ne doit pas être dans le futur. */
    public Verification pasDansLeFutur(String date, String champ) {
        LocalDate d = date(date);
        return si(d != null && d.isAfter(LocalDate.now()), champ + " ne peut pas être dans le futur.");
    }

    /** date1 doit être le même jour ou après date2 (ignoré si l'une manque). */
    public Verification apres(String date1, String date2, String message) {
        LocalDate d1 = date(date1);
        LocalDate d2 = date(date2);
        return si(d1 != null && d2 != null && d1.isBefore(d2), message);
    }

    public boolean aDesErreurs() {
        return !erreurs.isEmpty();
    }

    /** Lève une IllegalArgumentException contenant toutes les erreurs trouvées. */
    public void lancer() {
        if (aDesErreurs()) {
            throw new IllegalArgumentException(String.join(" ", erreurs));
        }
    }

    private static LocalDate date(String valeur) {
        if (valeur == null || valeur.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(valeur.trim());
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
