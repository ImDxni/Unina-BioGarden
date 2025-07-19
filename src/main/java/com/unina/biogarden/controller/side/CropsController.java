package com.unina.biogarden.controller.side;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.unina.biogarden.controller.form.CreateCropFormController;
import com.unina.biogarden.models.Crop;
import com.unina.biogarden.service.ProjectService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import static com.unina.biogarden.utils.Utils.showAlert; // Se hai una classe Utils per showAlert

public class CropsController {

    @FXML
    private JFXTreeTableView<Crop> cropsTable;
    @FXML
    private JFXTreeTableColumn<Crop, String> nameCol;
    @FXML
    private JFXTreeTableColumn<Crop, String> growthTimeCol;
    @FXML
    private JFXTreeTableColumn<Crop, String> seededCol; // Questi campi non sono direttamente nel ColturaDTO, dovrai calcolarli o aggiungerli
    @FXML
    private JFXTreeTableColumn<Crop, String> harvestedCol; // Questi campi non sono direttamente nel ColturaDTO, dovrai calcolarli o aggiungerli

    private final ProjectService service = new ProjectService();

    @FXML
    public void initialize() {
        cropsTable.setColumnResizePolicy(JFXTreeTableView.CONSTRAINED_RESIZE_POLICY);

        initCellFactory();
        fetchAndPopulateCrops(); // Carica i dati all'avvio

    }

    private void initCellFactory() {
        nameCol.setCellValueFactory(param -> param.getValue().getValue().nameProperty());
        growthTimeCol.setCellValueFactory(param -> param.getValue().getValue().growthTimeProperty());
        seededCol.setCellValueFactory(param -> new SimpleStringProperty("N/A"));
        harvestedCol.setCellValueFactory(param -> new SimpleStringProperty("N/A"));
    }

    // Metodo per caricare e popolare la tabella
    private void fetchAndPopulateCrops() {
        try {
            ObservableList<Crop> data = FXCollections.observableArrayList(service.getCrops());

            TreeItem<Crop> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);
            cropsTable.setRoot(root);
            cropsTable.setShowRoot(false);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Errore Caricamento Colture", "Impossibile caricare le colture dal database.");
            e.printStackTrace();
        }
    }

    // Metodo per gestire il click sul bottone "Nuova Coltura"
    @FXML
    private void handleNewCropButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/unina/biogarden/form/crop-form-view.fxml"));
            Parent root = loader.load();

            CreateCropFormController controller = loader.getController();
            controller.setOnCropCreated(this::fetchAndPopulateCrops); // Imposta il callback

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Rendi il form modale
            stage.setTitle("Nuova Coltura");
            stage.setScene(new Scene(root));
            stage.showAndWait(); // Mostra il form e aspetta che venga chiuso

            // Se la coltura è stata creata, la tabella verrà aggiornata dal callback
            // Non è necessario fare nulla qui oltre a mostrare/nascondere il form
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Errore Apertura Form", "Impossibile aprire il form di creazione coltura.");
        }
    }
}