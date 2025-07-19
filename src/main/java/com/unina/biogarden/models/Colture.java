package com.unina.biogarden.models;

import com.unina.biogarden.enumerations.ColtureStatus;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.unina.biogarden.utils.Utils.firstCapitalLetter;

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
            dateStatusLabel.setText(formattedDate + " (" + firstCapitalLetter(status.getStatus())+ ")");
            box.setOnMouseClicked(onClickHandler);

            return box;
        } catch (IOException e) {
            e.printStackTrace();
            return new Pane(new Label("Errore caricamento coltivazione"));
        }
    }


}
