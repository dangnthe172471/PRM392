package com.example.prm392.model;

public class CreateBookingRequest {
    private int serviceId;
    private int areaSizeId;
    private int timeSlotId;
    private String bookingDate; // yyyy-MM-dd
    private String addressDistrict;
    private String addressDetail;
    private String contactName;
    private String contactPhone;
    private String notes;

    public CreateBookingRequest(int serviceId, int areaSizeId, int timeSlotId, String bookingDate, String addressDistrict, String addressDetail, String contactName, String contactPhone, String notes) {
        this.serviceId = serviceId;
        this.areaSizeId = areaSizeId;
        this.timeSlotId = timeSlotId;
        this.bookingDate = bookingDate;
        this.addressDistrict = addressDistrict;
        this.addressDetail = addressDetail;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.notes = notes;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getAreaSizeId() {
        return areaSizeId;
    }

    public void setAreaSizeId(int areaSizeId) {
        this.areaSizeId = areaSizeId;
    }

    public int getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(int timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getAddressDistrict() {
        return addressDistrict;
    }

    public void setAddressDistrict(String addressDistrict) {
        this.addressDistrict = addressDistrict;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
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
}