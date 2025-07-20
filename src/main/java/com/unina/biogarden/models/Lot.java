package com.unina.biogarden.models;

import com.unina.biogarden.dto.LotDTO;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Rappresenta un lotto di terreno all'interno del sistema BioGarden.
 * Questa classe incapsula i dettagli di un lotto, come l'identificatore, il nome e l'area.
 * Offre anche un metodo per generare una rappresentazione visiva (Pane JavaFX) del lotto.
 * @author Il Tuo Nome
 */
public class Lot {
    private final int id;
    private final String name;
    private final int area;

    /**
     * Costruisce una nuova istanza di {@code Lot} con i dettagli specificati.
     *
     * @param id L'identificatore univoco del lotto.
     * @param name Il nome del lotto.
     * @param area L'area del lotto in metri quadrati.
     */
    public Lot(int id, String name, int area) {
        this.name = name;
        this.area = area;
        this.id = id;
    }

    /**
     * Costruisce una nuova istanza di {@code Lot} a partire da un oggetto {@link LotDTO}.
     * Questo costruttore facilita la conversione da DTO a modello.
     *
     * @param lot L'oggetto {@link LotDTO} da cui inizializzare il lotto.
     */
    public Lot(LotDTO lot) {
        this(lot.id(), lot.nome(), lot.area());
    }

    /**
     * Restituisce l'ID univoco del lotto.
     * @return L'ID del lotto.
     */
    public int getId() {
        return id;
    }

    /**
     * Restituisce il nome del lotto.
     * @return Il nome del lotto.
     */
    public String getName() {
        return name;
    }

    /**
     * Restituisce l'area del lotto.
     * @return L'area del lotto in metri quadrati.
     */
    public int getArea() {
        return area;
    }

    /**
     * Restituisce una rappresentazione stringa di questo oggetto Lot,
     * che include il nome e l'area con l'unità di misura.
     * @return Una stringa formattata con il nome e l'area del lotto.
     */
    @Override
    public String toString() {
        return getName() + " (" + getArea() + " km²)";
    }

    /**
     * Crea e restituisce un {@link Pane} di JavaFX che rappresenta visivamente questo lotto.
     * Il pane mostra il nome e l'area del lotto e può essere associato a un gestore di eventi per il clic del mouse.
     *
     * @param onClickHandler Un {@link EventHandler} da associare all'evento di clic del mouse sul pane del lotto.
     * Può essere {@code null} se non è richiesto un gestore di eventi.
     * @return Un {@link Pane} configurato per visualizzare i dettagli del lotto e gestirne i clic.
     */
    public Pane createLotBox(EventHandler<? super MouseEvent> onClickHandler) {
        Label name = new Label(this.name);
        name.setStyle("-fx-font-weight: 600; -fx-font-size: 14px; -fx-text-fill: #222;");

        Label area = new Label(this.area + " km²");
        area.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

        VBox content = new VBox(name, area);
        content.setSpacing(4);
        content.setLayoutX(12);
        content.setLayoutY(12);

        Pane box = new Pane(content);
        box.setPrefSize(110, 80);
        box.getStyleClass().add("lot-pane");

        box.setOnMouseClicked(onClickHandler);

        return box;
    }
}