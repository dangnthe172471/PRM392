package com.example.prm392.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class BookingResponse {
    @SerializedName("id")
    private int id;
    
    @SerializedName("userName")
    private String userName;
    
    @SerializedName("serviceName")
    private String serviceName;
    
    @SerializedName("areaSizeName")
    private String areaSizeName;
    
    @SerializedName("timeSlotRange")
    private String timeSlotRange;
    
    @SerializedName("bookingDate")
    private Date bookingDate;
    
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
    
    @SerializedName("cleanerName")
    private String cleanerName;
    
    @SerializedName("createdAt")
    private Date createdAt;

    // Constructors
    public BookingResponse() {}

    public BookingResponse(int id, String userName, String serviceName, String areaSizeName, 
                          String timeSlotRange, Date bookingDate, String address, String contactName, 
                          String contactPhone, String notes, double totalPrice, String status, 
                          String cleanerName, Date createdAt) {
        this.id = id;
        this.userName = userName;
        this.serviceName = serviceName;
        this.areaSizeName = areaSizeName;
        this.timeSlotRange = timeSlotRange;
        this.bookingDate = bookingDate;
        this.address = address;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.notes = notes;
        this.totalPrice = totalPrice;
        this.status = status;
        this.cleanerName = cleanerName;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getAreaSizeName() {
        return areaSizeName;
    }

    public void setAreaSizeName(String areaSizeName) {
        this.areaSizeName = areaSizeName;
    }

    public String getTimeSlotRange() {
        return timeSlotRange;
    }

    public void setTimeSlotRange(String timeSlotRange) {
        this.timeSlotRange = timeSlotRange;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCleanerName() {
        return cleanerName;
    }

    public void setCleanerName(String cleanerName) {
        this.cleanerName = cleanerName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
} 