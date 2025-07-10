package com.example.prm392.model;

public class VerifyPinRequest {
    private String email;
    private String pin;

    public VerifyPinRequest(String email, String pin) {
        this.email = email;
        this.pin = pin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
} 