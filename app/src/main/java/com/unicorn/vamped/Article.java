package com.unicorn.vamped;

public class Article {
    private String name;
    private String content;
    private String imageUrl;
    private Integer artUrl;

    public Article (String name, String content, String imageUrl, Integer artUrl){
        this.name = name;
        this.content = content;
        this.imageUrl = imageUrl;
        this.artUrl = artUrl;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    String getImageUrl() {
        return imageUrl;
    }

    Integer getArtUrl() {
        return artUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setArtUrl(Integer artUrl) {
        this.artUrl = artUrl;
    }
}