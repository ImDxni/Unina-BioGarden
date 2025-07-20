package com.unina.biogarden.models;

import com.unina.biogarden.enumerations.ColtureStatus;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.unina.biogarden.utils.Utils.firstCapitalLetter;

/**
 * Rappresenta una coltivazione specifica all'interno di un progetto nel sistema BioGarden.
 * Questa classe contiene i dettagli di una coltivazione, come l'identificatore,
 * la data di inizio, lo stato attuale e il tipo di {@link Crop} associato.
 * Offre anche un metodo per generare una rappresentazione visiva (Pane JavaFX) della coltivazione.
 * @author Il Tuo Nome
 */
public class Colture {
    private final int id;
    private final LocalDate startDate;
    private final ColtureStatus status;

    private final Crop crop;

    /**
     * Costruisce una nuova istanza di {@code Colture} con tutti i dettagli specificati.
     *
     * @param id L'identificatore univoco della coltivazione.
     * @param startDate La data di inizio della coltivazione.
     * @param status Lo stato attuale della coltivazione (es. {@link ColtureStatus#WAITING}, {@link ColtureStatus#SEEDED}).
     * @param crop Il {@link Crop} (tipo di coltura) associato a questa coltivazione.
     */
    public Colture(int id, LocalDate startDate, ColtureStatus status, Crop crop) {
        this.id = id;
        this.startDate = startDate;
        this.status = status;
        this.crop = crop;
    }

    /**
     * Costruisce una nuova istanza di {@code Colture} con stato iniziale predefinito.
     * La data di inizio viene impostata alla data corrente e lo stato a {@link ColtureStatus#WAITING}.
     *
     * @param id L'identificatore univoco della coltivazione.
     * @param crop Il {@link Crop} (tipo di coltura) associato a questa coltivazione.
     */
    public Colture(int id, Crop crop) {
        this.id = id;
        this.startDate = LocalDate.now();
        this.status = ColtureStatus.WAITING;
        this.crop = crop;
    }

    /**
     * Restituisce l'ID univoco della coltivazione.
     * @return L'ID della coltivazione.
     */
    public int getId() {
        return id;
    }

    /**
     * Restituisce la data di inizio della coltivazione.
     * @return La data di inizio della coltivazione.
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Restituisce lo stato attuale della coltivazione.
     * @return Lo stato della coltivazione.
     */
    public ColtureStatus getStatus() {
        return status;
    }

    /**
     * Restituisce il tipo di coltura (Crop) associato a questa coltivazione.
     * @return L'oggetto {@link Crop} di questa coltivazione.
     */
    public Crop getCrop() {
        return crop;
    }

    /**
     * Crea e restituisce un {@link Pane} di JavaFX che rappresenta visivamente questa coltivazione.
     * Questo pane carica il layout da "colture-pane.fxml" e popola le etichette con il nome della coltura
     * e la data di inizio/stato. Un gestore di eventi per il clic del mouse può essere opzionalmente specificato.
     *
     * @param onClickHandler Un {@link EventHandler} da associare all'evento di clic del mouse sul pane della coltivazione.
     * Può essere {@code null} se non è richiesto un gestore di eventi.
     * @return Un {@link Pane} configurato per visualizzare i dettagli della coltivazione e gestirne i clic.
     * @throws RuntimeException Se si verifica un errore I/O durante il caricamento del file FXML.
     */
    public Pane createColturePane(EventHandler<? super MouseEvent> onClickHandler) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/unina/biogarden/pane/colture-pane.fxml"));
            Pane box = new Pane();
            loader.setRoot(box);
            loader.load();

            box.setPrefHeight(100);
            box.setPrefWidth(Region.USE_COMPUTED_SIZE);

            Label nameLabel = (Label) box.lookup("#coltureNameLabel");
            Label dateStatusLabel = (Label) box.lookup("#coltureDateStatusLabel");

            nameLabel.setText(crop.nameProperty().get());
            String formattedDate = this.startDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
            dateStatusLabel.setText(formattedDate + " (" + firstCapitalLetter(status.getStatus()) + ")");
            box.setOnMouseClicked(onClickHandler);

            return box;
        } catch (IOException e) {
            System.err.println("Errore caricamento FXML per ColturePane: " + e.getMessage());
            e.printStackTrace();
            // Restituisce un Pane di errore per indicare che qualcosa è andato storto
            return new Pane(new Label("Errore caricamento coltivazione."));
        }
    }
}