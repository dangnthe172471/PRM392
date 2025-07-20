package com.example.prm392.model;

import com.google.gson.annotations.SerializedName;

public class CleanerDashboardStats {
    @SerializedName("availableJobs")
    private int availableJobs;
    @SerializedName("myJobs")
    private int myJobs;
    @SerializedName("completedJobs")
    private int completedJobs;
    @SerializedName("totalEarnings")
    private double totalEarnings;
    @SerializedName("pendingJobs")
    private int pendingJobs;
    @SerializedName("inProgressJobs")
    private int inProgressJobs;

    public int getAvailableJobs() { return availableJobs; }
    public int getMyJobs() { return myJobs; }
    public int getCompletedJobs() { return completedJobs; }
    public double getTotalEarnings() { return totalEarnings; }
    public int getPendingJobs() { return pendingJobs; }
    public int getInProgressJobs() { return inProgressJobs; }
} 