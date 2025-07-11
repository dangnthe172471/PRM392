package com.example.prm392.model;

public class TimeSlotModel {
    private int id;
    private String timeRange;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TimeSlotModel() {

    }

    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    @Override
    public String toString() {
        return timeRange;
    }
}

