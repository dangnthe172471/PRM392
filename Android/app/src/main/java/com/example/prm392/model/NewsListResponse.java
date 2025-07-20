package com.example.prm392.model;
import java.util.List;

public class NewsListResponse {
    private List<NewsArticle> items;
    public List<NewsArticle> getItems() { return items; }
    public void setItems(List<NewsArticle> items) { this.items = items; }
} 