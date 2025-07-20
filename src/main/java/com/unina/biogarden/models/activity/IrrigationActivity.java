package com.unina.biogarden.models.activity;

import com.unina.biogarden.enumerations.ActivityStatus;
import com.unina.biogarden.enumerations.ActivityType;

import java.time.LocalDate;

public class IrrigationActivity extends Activity {

    public IrrigationActivity(int id, LocalDate date, ActivityStatus status, int farmerID, String farmer) {
        super(id, date, ActivityType.IRRIGATION, status, farmerID, farmer);
    }

    public IrrigationActivity(int id, LocalDate date, int farmerID, String farmer) {
        super(id, date, ActivityType.IRRIGATION, ActivityStatus.PLANNED, farmerID, farmer);
    }


    @Override
    public String getDetails() {
        return "Nessuna informazione aggiuntiva";
    }
}
