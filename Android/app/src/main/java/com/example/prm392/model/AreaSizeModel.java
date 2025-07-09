package com.example.prm392.model;

public class AreaSizeModel {
    private int id;
    private String name;
    private double multiplier;

    public AreaSizeModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public String toString() {
        return name;
    }
}

