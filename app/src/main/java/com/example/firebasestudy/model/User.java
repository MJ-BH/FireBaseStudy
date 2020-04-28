package com.example.firebasestudy.model;

public class User {
    private String uid;
    private String nom;
    private String prenom;
    private String url;

    public User() {
    }

    public User(String uid, String nom, String prenom, String url) {
        this.uid = uid;
        this.nom = nom;
        this.prenom = prenom;
        this.url = url;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
