package com.unina.biogarden.dto.activity;

import com.unina.biogarden.enumerations.ActivityStatus;
import com.unina.biogarden.enumerations.ActivityType;

import java.time.LocalDate;


public class ActivityDTO {
    private int id;
    private final LocalDate date;
    private final ActivityStatus status;
    private final ActivityType type;
    private final int lotID,coltureID,farmerID;

    public ActivityDTO(int id, LocalDate date, ActivityStatus status,ActivityType type,int coltureID,int lotID, int farmerID) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.type = type;
        this.coltureID = coltureID;
        this.lotID = lotID;
        this.farmerID = farmerID;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public ActivityStatus getStatus() {
        return status;
    }

    public ActivityType getType() {
        return type;
    }
    public int getColtureID() {
        return coltureID;
    }
    public int getLotID() {
        return lotID;
    }
    public int getFarmerID() {
        return farmerID;
    }

}