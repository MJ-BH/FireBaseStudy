package com.example.firebasestudy.model


data class Article(
        var url: String? = null,
        var creatdAt: Long = 0,
        var createdby: String? = null,
        var content: String? = null,
        var id: String? = null,
        var claps: Int = 0
)

