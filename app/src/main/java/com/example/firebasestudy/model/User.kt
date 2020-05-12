package com.example.firebasestudy.model


class User {
    private var uid: String? = null
    private var mail: String? = null
    private var nom: String? = null
    private var prenom: String? = null
    private var url: String? = null
    private var token: String? = null

    constructor()
    constructor(uid: String?, mail: String?, nom: String?, prenom: String?, url: String?, token: String?) {
        this.uid = uid
        this.mail = mail
        this.nom = nom
        this.prenom = prenom
        this.url = url
        this.token = token
    }

    fun getUid(): String? {
        return uid
    }

    fun setUid(uid: String?) {
        this.uid = uid
    }

    fun getMail(): String? {
        return mail
    }

    fun setMail(mail: String?) {
        this.mail = mail
    }

    fun getNom(): String? {
        return nom
    }

    fun setNom(nom: String?) {
        this.nom = nom
    }

    fun getPrenom(): String? {
        return prenom
    }

    fun setPrenom(prenom: String?) {
        this.prenom = prenom
    }

    fun getUrl(): String? {
        return url
    }

    fun setUrl(url: String?) {
        this.url = url
    }

    fun getToken(): String? {
        return token
    }

    fun setToken(token: String?) {
        this.token = token
    }
}