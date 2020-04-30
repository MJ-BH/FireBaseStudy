package com.example.firebasestudy.model;

public class Article {

    private String url;
    private long creatdAt;
    private String createdby;
    private String content;
    private String id;
    private int claps;

    public Article() {
    }

    public Article(String url, long creatdAt, String createdby, String content, String id, int claps) {
        this.url = url;
        this.creatdAt = creatdAt;
        this.createdby = createdby;
        this.content = content;
        this.id = id;
        this.claps = claps;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getCreatdAt() {
        return creatdAt;
    }

    public void setCreatdAt(long creatdAt) {
        this.creatdAt = creatdAt;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getClaps() {
        return claps;
    }

    public void setClaps(int claps) {
        this.claps = claps;
    }
}
