package com.example.prm392.model;

import com.google.gson.annotations.SerializedName;

public class Job {
    @SerializedName("id")
    private int id;
    @SerializedName("serviceName")
    private String serviceName;
    @SerializedName("areaSize")
    private String areaSize;
    @SerializedName("timeSlot")
    private String timeSlot;
    @SerializedName("bookingDate")
    private String bookingDate;
    @SerializedName("address")
    private String address;
    @SerializedName("contactName")
    private String contactName;
    @SerializedName("contactPhone")
    private String contactPhone;
    @SerializedName("notes")
    private String notes;
    @SerializedName("totalPrice")
    private double totalPrice;
    @SerializedName("status")
    private String status;
    @SerializedName("cleanerId")
    private Integer cleanerId;
    @SerializedName("cleanerName")
    private String cleanerName;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("updatedAt")
    private String updatedAt;

    public int getId() { return id; }
    public String getServiceName() { return serviceName; }
    public String getAreaSize() { return areaSize; }
    public String getTimeSlot() { return timeSlot; }
    public String getBookingDate() { return bookingDate; }
    public String getAddress() { return address; }
    public String getContactName() { return contactName; }
    public String getContactPhone() { return contactPhone; }
    public String getNotes() { return notes; }
    public double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public Integer getCleanerId() { return cleanerId; }
    public String getCleanerName() { return cleanerName; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
} 