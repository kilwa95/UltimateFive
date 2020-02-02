package com.example.ultimatefive;

/**
 * created by Abdulhalim Khaled on 2020-01-21.
 */
public class Matche
{

    private String id;
    private String date;
    private String duree;
    private String heure;
    private String prix;
    private String type;
    private String ville;
    private String description;
    private String image;

    public Matche(String id, String date, String duree, String heure, String prix, String type, String ville, String description, String image) {
        this.id = id;
        this.date = date;
        this.duree = duree;
        this.heure = heure;
        this.prix = prix;
        this.type = type;
        this.ville = ville;
        this.description = description;
        this.image = image;
    }

    public Matche()
    {

    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
