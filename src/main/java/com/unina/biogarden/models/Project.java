package com.unina.biogarden.models;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.unina.biogarden.controller.side.ActivitiesController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

public class Project extends RecursiveTreeObject<Project> {
    private final int id;
    private final int lotId;
    private final SimpleStringProperty name, lot, season, status;

    private final Collection<Colture> coltures = new ArrayList<>();

    public Project(int id,String name, int lotId,String lotName, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.lotId = lotId;
        this.name = new SimpleStringProperty(name);

        this.lot = new SimpleStringProperty(lotName);
        this.season = new SimpleStringProperty(getSeason(startDate));
        this.status = new SimpleStringProperty(getStatus(startDate, endDate));
    }

    public Project(int id,String name, int lotId,String lotName, LocalDate startDate, LocalDate endDate, Collection<Colture> coltures) {
        this(id,name,lotId,lotName,startDate,endDate);

        this.coltures.addAll(coltures);
    }

    private String getSeason(LocalDate date) {
        if (date == null) return "N/A";
        int month = date.getMonthValue();
        if (month >= 3 && month <= 5) return "Primavera";
        if (month >= 6 && month <= 8) return "Estate";
        if (month >= 9 && month <= 11) return "Autunno";
        return "Inverno";
    }

    private String getStatus(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) return "N/A";
        LocalDate now = LocalDate.now();
        if (now.isBefore(startDate)) return "Pianificato";
        if (now.isAfter(endDate)) return "Completato";
        return "In Corso";
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty lotProperty() {
        return lot;
    }

    public SimpleStringProperty seasonProperty() {
        return season;
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void setColtures(Collection<Colture> coltures) {
        this.coltures.clear();
        this.coltures.addAll(coltures);
    }

    public Node buildProjectPane(EventHandler<ActionEvent> onAddCultivation, Consumer<Colture> onCultivationClick) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/unina/biogarden/pane/project-pane.fxml"));
            VBox projectBlock = new VBox();

            loader.setRoot(projectBlock);

            loader.load();

            Label projectTitleLabel = (Label) projectBlock.lookup("#projectTitleLabel");
            JFXButton addCultivationButton = (JFXButton) projectBlock.lookup("#addCultivationButton");
            ScrollPane scrollPane = (ScrollPane) projectBlock.lookup(".scroll-pane");
            HBox cultivationsContainer = (HBox) scrollPane.getContent();

            projectTitleLabel.setText(nameProperty().get());

            addCultivationButton.setUserData(getId());
            addCultivationButton.setOnAction(onAddCultivation);

            for (Colture cultivation : coltures) {
                Pane cultivationBlock = cultivation.createColturePane(e -> onCultivationClick.accept(cultivation));
                cultivationsContainer.getChildren().add(cultivationBlock);
            }

            return projectBlock;
        } catch (IOException e) {
            e.printStackTrace();
            return new VBox(new Label("Errore caricamento progetto: " + nameProperty().get()));
        }
    }

    private static Callback<TreeTableColumn.CellDataFeatures<Project, String>, ObservableValue<String>> getSafeCellValueFactory(
            Function<Project, SimpleStringProperty> extractor) {
        return param -> {
            TreeItem<Project> item = param.getValue();
            if (item != null && item.getValue() != null) {
                return extractor.apply(item.getValue());
            }
            return new SimpleStringProperty("");
        };
    }

    public static void initCellFactory(JFXTreeTableColumn<Project, String> nameCol,
                                       JFXTreeTableColumn<Project, String> lotCol,
                                       JFXTreeTableColumn<Project, String> seasonCol,
                                       JFXTreeTableColumn<Project, String> statusCol) {

        nameCol.setCellValueFactory(getSafeCellValueFactory(Project::nameProperty));
        lotCol.setCellValueFactory(getSafeCellValueFactory(Project::lotProperty));
        seasonCol.setCellValueFactory(getSafeCellValueFactory(Project::seasonProperty));
        statusCol.setCellValueFactory(getSafeCellValueFactory(Project::statusProperty));
    }



}
