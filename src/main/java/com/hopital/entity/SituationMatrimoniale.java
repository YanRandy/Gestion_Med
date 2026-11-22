package com.hopital.entity;

public class SituationMatrimoniale {

    private Integer id;
    private String libelle;

    public SituationMatrimoniale() {
    }

    public SituationMatrimoniale(Integer id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
