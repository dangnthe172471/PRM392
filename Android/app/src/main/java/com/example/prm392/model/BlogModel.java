package com.example.prm392.model;

import java.io.Serializable;

public class BlogModel implements Serializable {
    private int id;
    private String title;
    private String content;
    private String imageUrl;
    private String publishDate;
    private String readTime;
    private int views;
    private int likes;
    private int comments;

    public BlogModel(int id, String title, String content, String imageUrl, String publishDate,
                     String readTime, int views, int likes, int comments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.publishDate = publishDate;
        this.readTime = readTime;
        this.views = views;
        this.likes = likes;
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public String getReadTime() {
        return readTime;
    }

    public int getViews() {
        return views;
    }

    public int getLikes() {
        return likes;
    }

    public int getComments() {
        return comments;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }
}
