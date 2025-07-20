package com.example.prm392.model;

public class UpdateJobStatusRequest {
    private String status;

    public UpdateJobStatusRequest(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
} 