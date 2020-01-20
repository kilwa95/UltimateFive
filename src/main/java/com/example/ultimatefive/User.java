package com.example.ultimatefive;

/**
 * created by Abdulhalim Khaled on 2020-01-03.
 */
class User {

    private String id;
    private String nom;
    private String prenom;
    private String ville;
    private String age;
    private String email;
    private String password;
    private String image;

    public User(String id, String nom, String prenom, String ville, String age, String email, String password, String image) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.ville = ville;
        this.age = age;
        this.email = email;
        this.password = password;
        this.image = image;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImage(String imageURL) {
        this.image = imageURL;
    }

    public String getImage() {
        return image;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
