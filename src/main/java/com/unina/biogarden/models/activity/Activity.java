package com.unina.biogarden.models.activity;

import com.unina.biogarden.enumerations.ActivityStatus;
import com.unina.biogarden.enumerations.ActivityType;

import java.time.LocalDate;

public abstract class Activity {
    private int id;
    private LocalDate date;
    private ActivityType type;
    private ActivityStatus status;

    private int farmerID;
    private String farmer;

    public Activity(int id, LocalDate date, ActivityType type, ActivityStatus status, int farmerID, String farmer) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.status = status;
        this.farmerID = farmerID;
        this.farmer = farmer;
    }

    public int getFarmerID() {
        return farmerID;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public ActivityType getType() {
        return type;
    }

    public ActivityStatus getStatus() {
        return status;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public void setStatus(ActivityStatus status) {
        this.status = status;
    }

    public void setFarmerID(int farmerID) {
        this.farmerID = farmerID;
    }

    public void setFarmer(String farmer) {
        this.farmer = farmer;
    }

    public String getFarmer() {
        return farmer;
    }


    public abstract String getDetails();
}
