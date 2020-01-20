package com.example.ultimatefive;

/**
 * created by Abdulhalim Khaled on 2019-12-25.
 */
public class Model {

    private String nomOrganisateur;
    private String lieu;
    private String adresse;
    private String date;
    private String horaire;
    private String typeTerraine;
    private int image;



    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }



    public String getNomOrganisateur() {
        return nomOrganisateur;
    }

    public void setNomOrganisateur(String nomOrganisateur) {
        this.nomOrganisateur = nomOrganisateur;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHoraire() {
        return horaire;
    }

    public void setHoraire(String horaire) {
        this.horaire = horaire;
    }

    public String getTypeTerraine() {
        return typeTerraine;
    }

    public void setTypeTerraine(String typeTerraine) {
        this.typeTerraine = typeTerraine;
    }
}
