package com.unina.biogarden.models;

import com.unina.biogarden.enumerations.ColtureStatus;

import java.time.LocalDate;

public class Colture {
    private final LocalDate startDate;
    private final ColtureStatus status;

    private final Crop crop;


    public Colture(LocalDate startDate, ColtureStatus status, Crop crop) {
        this.startDate = startDate;
        this.status = status;
        this.crop = crop;
    }

    public Colture(Crop crop){
        this.startDate = LocalDate.now();
        this.status = ColtureStatus.SEEDED;
        this.crop = crop;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public ColtureStatus getStatus() {
        return status;
    }

    public Crop getCrop() {
        return crop;
    }

}
