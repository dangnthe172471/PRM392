package com.example.prm392.model;

public class BlogModel {
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
}
