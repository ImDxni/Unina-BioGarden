package com.unina.biogarden.models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Rappresenta un tipo di coltura nel sistema BioGarden.
 * Questa classe estende {@link RecursiveTreeObject} per l'utilizzo in {@code JFoenix TreeTableView}
 * e gestisce le proprietà reattive (JavaFX Properties) per il nome della coltura,
 * il tempo di crescita, il numero di volte che è stata seminata e raccolta.
 * @author Il Tuo Nome
 */
public class Crop extends RecursiveTreeObject<Crop> {
    private final int id;
    private final StringProperty name;
    private final StringProperty growthTime;
    private final StringProperty projects;

    /**
     * Costruisce una nuova istanza di {@code Crop}.
     * Le proprietà {@code seeded} e {@code harvested} vengono inizializzate a "0".
     *
     * @param id L'identificatore univoco del tipo di coltura.
     * @param name Il nome del tipo di coltura (es. "Pomodoro").
     * @param growthTime Il tempo di maturazione della coltura in giorni.
     * @param projects Il numero di progetti a cui questa coltura è collegata.
     */
    public Crop(int id, String name, int growthTime,int projects) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.growthTime = new SimpleStringProperty(growthTime + " Giorni");
        this.projects = new SimpleStringProperty(String.valueOf(projects));
    }

    /**
     * Restituisce la proprietà del nome della coltura.
     * Questa è una proprietà osservabile utilizzata per il binding in JavaFX.
     * @return La {@link StringProperty} del nome.
     */
    public StringProperty nameProperty() {
        return name;
    }

    /**
     * Restituisce la proprietà del tempo di crescita della coltura.
     * Questa è una proprietà osservabile utilizzata per il binding in JavaFX.
     * Il valore include l'unità "Giorni".
     * @return La {@link StringProperty} del tempo di crescita.
     */
    public StringProperty growthTimeProperty() {
        return growthTime;
    }

    /**
     * Restituisce la proprietà che indica a quanti progetti è collegata questa coltura.
     * Questa è una proprietà osservabile utilizzata per il binding in JavaFX.
     * @return La {@link StringProperty} del conteggio dei progetti.
     */
    public StringProperty projectsProperty() {
        return projects;
    }

    /**
     * Restituisce una rappresentazione stringa di questo oggetto Crop,
     * che include il nome e il tempo di crescita.
     * @return Una stringa formattata con il nome e il tempo di crescita della coltura.
     */
    @Override
    public String toString() {
        return nameProperty().get() + " (" + growthTimeProperty().get() + ")";
    }

    /**
     * Restituisce l'ID univoco del tipo di coltura.
     * @return L'ID della coltura.
     */
    public int getId() {
        return id;
    }
}