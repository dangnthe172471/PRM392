package com.example.prm392.model;

public class UserProfile {
    private String name; // Thêm trường name
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String experience;
    private String status;
    private String role; // Thêm trường role
    private String createdAt; // Thêm trường createdAt

    public UserProfile(String name, String fullName, String email, String phone, String address, String experience, String status, String role, String createdAt) {
        this.name = name;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.experience = experience;
        this.status = status;
        this.role = role;
        this.createdAt = createdAt;
    }

    // Getter cho name (ưu tiên name, fallback sang fullName)
    public String getName() { return name != null ? name : fullName; }
    public String getFullName() { return fullName != null ? fullName : name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getExperience() { return experience; }
    public String getStatus() { return status; }
    public String getRole() { return role; }
    public String getCreatedAt() { return createdAt; }
} 