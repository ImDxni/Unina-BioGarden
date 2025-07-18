package com.unina.biogarden.controller.side;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.unina.biogarden.controller.form.CreateCropFormController; // Importa il controller del form
import com.unina.biogarden.dao.ColturaDAO; // Importa il DAO
import com.unina.biogarden.dto.ColturaDTO; // Importa il DTO
import com.unina.biogarden.models.CropRow; // Dovrai creare questa classe, simile a ProjectRow
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

import static com.unina.biogarden.utils.Utils.showAlert; // Se hai una classe Utils per showAlert

public class CropsController {

    @FXML
    private JFXTreeTableView<CropRow> cropsTable;
    @FXML
    private JFXTreeTableColumn<CropRow, String> nameCol;
    @FXML
    private JFXTreeTableColumn<CropRow, String> growthTimeCol;
    @FXML
    private JFXTreeTableColumn<CropRow, String> seededCol; // Questi campi non sono direttamente nel ColturaDTO, dovrai calcolarli o aggiungerli
    @FXML
    private JFXTreeTableColumn<CropRow, String> harvestedCol; // Questi campi non sono direttamente nel ColturaDTO, dovrai calcolarli o aggiungerli
    @FXML
    private JFXButton newCropButton; // Aggiungi fx:id al bottone "Nuova Coltura"

    private ColturaDAO colturaDAO; // Istanza del DAO

    @FXML
    public void initialize() {
        colturaDAO = new ColturaDAO();
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
            Collection<ColturaDTO> coltureDTO = colturaDAO.fetchAllColture();
            ObservableList<CropRow> data = coltureDTO.stream()
                    .map(c -> new CropRow(
                            c.nome(),
                            c.giorniMaturazione()
                    ))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));

            TreeItem<CropRow> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);
            cropsTable.setRoot(root);
            cropsTable.setShowRoot(false);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Errore Caricamento Colture", "Impossibile caricare le colture dal database.");
            e.printStackTrace();
        }
    }

    // Metodo per gestire il click sul bottone "Nuova Coltura"
    @FXML
    private void handleNewCropButton(ActionEvent event) {
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