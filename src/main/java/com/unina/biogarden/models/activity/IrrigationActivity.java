package com.unina.biogarden.models.activity;

import com.unina.biogarden.enumerations.ActivityStatus;
import com.unina.biogarden.enumerations.ActivityType;

import java.time.LocalDate;

/**
 * Rappresenta un'attività di irrigazione nel sistema BioGarden.
 * Estende la classe astratta {@link Activity}. Le attività di irrigazione
 * non hanno attributi aggiuntivi specifici oltre a quelli ereditati.
 * @author Il Tuo Nome
 */
public class IrrigationActivity extends Activity {

    /**
     * Costruisce una nuova istanza di {@code IrrigationActivity} con uno stato specificato.
     *
     * @param id L'identificatore univoco dell'attività di irrigazione.
     * @param date La data in cui l'attività è pianificata o è stata svolta.
     * @param status Lo stato attuale dell'attività (es. completata, in corso).
     * @param farmerID L'ID dell'agricoltore responsabile dell'attività.
     * @param farmer Il nome dell'agricoltore responsabile.
     */
    public IrrigationActivity(int id, LocalDate date, ActivityStatus status, int farmerID, String farmer) {
        super(id, date, ActivityType.IRRIGATION, status, farmerID, farmer);
    }

    /**
     * Costruisce una nuova istanza di {@code IrrigationActivity} con stato iniziale {@link ActivityStatus#PLANNED}.
     *
     * @param id L'identificatore univoco dell'attività di irrigazione.
     * @param date La data in cui l'attività è pianificata o è stata svolta.
     * @param farmerID L'ID dell'agricoltore responsabile dell'attività.
     * @param farmer Il nome dell'agricoltore responsabile.
     */
    public IrrigationActivity(int id, LocalDate date, int farmerID, String farmer) {
        super(id, date, ActivityType.IRRIGATION, ActivityStatus.PLANNED, farmerID, farmer);
    }

    /**
     * Fornisce una stringa dettagliata che descrive l'attività di irrigazione.
     * Per questo tipo di attività, non ci sono informazioni aggiuntive specifiche
     * oltre a quelle già presenti nella classe base {@link Activity}.
     * @return Una stringa che indica l'assenza di dettagli aggiuntivi specifici per l'irrigazione.
     */
    @Override
    public String getDetails() {
        return "Nessuna informazione aggiuntiva";
    }
}