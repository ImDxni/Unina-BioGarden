package com.unina.biogarden.models;

import com.unina.biogarden.dto.LotDTO;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Lot {
    private final int id;
    private final String name;
    private final int area;

    public Lot(int id,String name, int area) {
        this.name = name;
        this.area = area;
        this.id = id;
    }

    public Lot(LotDTO lot) {
        this(lot.id(), lot.nome(), lot.area());
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public int getArea() {
        return area;
    }

    @Override
    public String toString() {
        return getName() + " (" + getArea() + " km²)";
    }

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
