package com.hopital.entity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Patient {

    private Integer id;

    private String nom;
    private String prenoms;
    private String nomUsage;

    private LocalDate dateNaissance;
    private Boolean estDatePresumee = false;
    private String lieuNaissance;

    private String sexe;

    private String nomPere;
    private String prenomsPere;
    private String nomMere;
    private String prenomsMere;

    private Integer idSituationMatrimoniale;

    private String adresse;
    private String telephone;
    private String email;
    private String contactUrgenceNom;
    private String contactUrgenceTel;

    private Integer idNationalite;

    private String numCin;
    private LocalDate dateDelivranceCin;
    private String lieuDelivranceCin;

    private Integer idUser;

    private LocalDateTime createdAt;

    public Patient() {
    }

    private static String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }

    private static LocalDate parseDate(String value) {
        return (value == null || value.isBlank()) ? null : LocalDate.parse(value.trim());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = blankToNull(nom);
    }

    public String getPrenoms() {
        return prenoms;
    }

    public void setPrenoms(String prenoms) {
        this.prenoms = blankToNull(prenoms);
    }

    public String getNomUsage() {
        return nomUsage;
    }

    public void setNomUsage(String nomUsage) {
        this.nomUsage = blankToNull(nomUsage);
    }

    public String getDateNaissance() {
        return dateNaissance == null ? null : dateNaissance.toString();
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = parseDate(dateNaissance);
    }

    public Boolean getEstDatePresumee() {
        return estDatePresumee;
    }

    public void setEstDatePresumee(Boolean estDatePresumee) {
        this.estDatePresumee = estDatePresumee != null && estDatePresumee;
    }

    public String getLieuNaissance() {
        return lieuNaissance;
    }

    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance = blankToNull(lieuNaissance);
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = blankToNull(sexe);
    }

    public String getNomPere() {
        return nomPere;
    }

    public void setNomPere(String nomPere) {
        this.nomPere = blankToNull(nomPere);
    }

    public String getPrenomsPere() {
        return prenomsPere;
    }

    public void setPrenomsPere(String prenomsPere) {
        this.prenomsPere = blankToNull(prenomsPere);
    }

    public String getNomMere() {
        return nomMere;
    }

    public void setNomMere(String nomMere) {
        this.nomMere = blankToNull(nomMere);
    }

    public String getPrenomsMere() {
        return prenomsMere;
    }

    public void setPrenomsMere(String prenomsMere) {
        this.prenomsMere = blankToNull(prenomsMere);
    }

    public Integer getIdSituationMatrimoniale() {
        return idSituationMatrimoniale;
    }

    public void setIdSituationMatrimoniale(Integer idSituationMatrimoniale) {
        this.idSituationMatrimoniale = idSituationMatrimoniale;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = blankToNull(adresse);
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = blankToNull(telephone);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = blankToNull(email);
    }

    public String getContactUrgenceNom() {
        return contactUrgenceNom;
    }

    public void setContactUrgenceNom(String contactUrgenceNom) {
        this.contactUrgenceNom = blankToNull(contactUrgenceNom);
    }

    public String getContactUrgenceTel() {
        return contactUrgenceTel;
    }

    public void setContactUrgenceTel(String contactUrgenceTel) {
        this.contactUrgenceTel = blankToNull(contactUrgenceTel);
    }

    public Integer getIdNationalite() {
        return idNationalite;
    }

    public void setIdNationalite(Integer idNationalite) {
        this.idNationalite = idNationalite;
    }

    public String getNumCin() {
        return numCin;
    }

    public void setNumCin(String numCin) {
        this.numCin = blankToNull(numCin);
    }

    public String getDateDelivranceCin() {
        return dateDelivranceCin == null ? null : dateDelivranceCin.toString();
    }

    public void setDateDelivranceCin(String dateDelivranceCin) {
        this.dateDelivranceCin = parseDate(dateDelivranceCin);
    }

    public String getLieuDelivranceCin() {
        return lieuDelivranceCin;
    }

    public void setLieuDelivranceCin(String lieuDelivranceCin) {
        this.lieuDelivranceCin = blankToNull(lieuDelivranceCin);
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getCreatedAt() {
        return createdAt == null ? null : createdAt.toString();
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = (createdAt == null || createdAt.isBlank()) ? null : Timestamp.valueOf(createdAt).toLocalDateTime();
    }
}
