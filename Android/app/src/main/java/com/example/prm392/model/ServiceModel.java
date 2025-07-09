package com.example.prm392.model;


public class ServiceModel {
    private int id;
    private String name;
    private String description;
    private double basePrice;
    private String duration;
    private String icon;

    public ServiceModel() {

    }

    public ServiceModel(int id, String name, String description, double basePrice, String duration, String icon) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
        this.duration = duration;
        this.icon = icon;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getBasePrice() { return basePrice; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
}
