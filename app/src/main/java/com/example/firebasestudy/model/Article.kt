package com.example.firebasestudy.model


class Article {
    private var url: String? = null
    private var creatdAt: Long = 0
    private var createdby: String? = null
    private var content: String? = null
    private var id: String? = null
    private var claps = 0

    fun getUrl(): String? {
        return url
    }

    fun setUrl(url: String?) {
        this.url = url
    }

    fun getCreatdAt(): Long {
        return creatdAt
    }

    fun setCreatdAt(creatdAt: Long) {
        this.creatdAt = creatdAt
    }

    fun setCreatedby(createdby: String?) {
        this.createdby = createdby
    }

    fun getContent(): String? {
        return content
    }

    fun setContent(content: String?) {
        this.content = content
    }

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

}