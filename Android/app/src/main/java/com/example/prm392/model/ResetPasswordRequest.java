package com.example.prm392.model;

public class ResetPasswordRequest {
    private String email;
    private String pin;
    private String newPassword;
    private String confirmPassword;
    public ResetPasswordRequest(String email, String pin, String newPassword, String confirmPassword) {
        this.email = email;
        this.pin = pin;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
} 