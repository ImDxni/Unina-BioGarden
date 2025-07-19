package com.unina.biogarden.dto.activity;

import com.unina.biogarden.enumerations.ActivityStatus;
import com.unina.biogarden.enumerations.ActivityType;

import java.time.LocalDate;

public class IrrigationActivityDTO extends ActivityDTO {

    public IrrigationActivityDTO(int id, LocalDate date, ActivityStatus status,ActivityType type, int coltureID, int lotID, int farmerID) {
        super(id, date, status, type, coltureID, lotID, farmerID);
    }

    public IrrigationActivityDTO(int id, LocalDate date, ActivityStatus status, int coltureID, int lotID, int farmerID) {
        super(id, date, status, ActivityType.IRRIGATION, coltureID, lotID, farmerID);
    }

}
