package com.unina.biogarden.controller.form;

import com.unina.biogarden.models.*;
import com.unina.biogarden.models.activity.Activity;
import com.unina.biogarden.models.activity.HarvestingActivity;
import com.unina.biogarden.models.activity.IrrigationActivity;
import com.unina.biogarden.models.activity.SeedingActivity;
import com.unina.biogarden.service.ProjectService;
import com.unina.biogarden.service.UserService;
import com.unina.biogarden.utils.Utils;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.util.Arrays;

public class CreateActivityFormController extends AbstractForm {

    @FXML
    private DatePicker datePicker;
    @FXML
    private JFXComboBox<String> activityTypeComboBox;
    @FXML
    private JFXComboBox<Farmer> growerComboBox;
    @FXML
    private VBox specificFieldsContainer;

    // Campi per Semina
    private JFXTextField sowingQuantityField;
    private JFXTextField sowingUnitField;

    // Campi per Raccolta
    private JFXTextField harvestPlannedQuantityField;
    private JFXTextField harvestActualQuantityField;
    private JFXTextField harvestUnitField;

    private Colture currentCultivation;
    private Runnable onActivityCreated;
    private final ProjectService projectService = new ProjectService();
    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        activityTypeComboBox.setItems(FXCollections.observableArrayList(Arrays.asList("Semina", "Irrigazione", "Raccolta")));
        activityTypeComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            updateSpecificFields(newVal);
        });

        datePicker.setValue(LocalDate.now());

        // Popola la ComboBox dei coltivatori
        try {
            growerComboBox.setItems(FXCollections.observableArrayList(userService.fetchAllFarmer()));
        } catch (RuntimeException e) {
            Utils.showAlert(Alert.AlertType.ERROR, "Errore Caricamento", "Impossibile caricare i coltivatori.");
            e.printStackTrace();
        }
    }
    // --- Metodi Mancanti Implementati ---

    /**
     * Imposta la coltivazione corrente per la quale viene creata l'attività.
     * Questo è fondamentale per associare l'attività al giusto oggetto Colture.
     * @param cultivation La coltivazione selezionata.
     */
    public void setCurrentCultivation(Colture cultivation) {
        this.currentCultivation = cultivation;
    }

    /**
     * Imposta la callback da eseguire dopo che un'attività è stata creata con successo.
     * Tipicamente usata per ricaricare la tabella delle attività nella vista principale.
     * @param onActivityCreated Un Runnable che verrà eseguito.
     */
    public void setOnActivityCreated(Runnable onActivityCreated) {
        this.onActivityCreated = onActivityCreated;
    }

    /**
     * Aggiorna dinamicamente i campi di input nel form in base al tipo di attività selezionato.
     * Nasconde i campi non necessari e mostra quelli specifici per il tipo di attività.
     * @param activityType Il tipo di attività selezionato (es. "Semina", "Irrigazione", "Raccolta").
     */
    private void updateSpecificFields(String activityType) {
        specificFieldsContainer.getChildren().clear(); // Pulisci i campi precedenti

        if (activityType == null) return;

        switch (activityType) {
            case "Semina":
                sowingQuantityField = new JFXTextField();
                sowingQuantityField.setPromptText("Quantità Semi");
                sowingQuantityField.setStyle("-jfx-focus-color: #4CAF50; -jfx-unfocus-color: #9E9E9E;");
                sowingQuantityField.textProperty().addListener((observable, oldValue, newValue) -> {
                    // Accetta solo numeri interi (o decimali se decidi di cambiare double)
                    if (!newValue.matches("\\d*")) {
                        sowingQuantityField.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                });

                sowingUnitField = new JFXTextField();
                sowingUnitField.setPromptText("Unità di Misura (es. grammi)");
                sowingUnitField.setStyle("-jfx-focus-color: #4CAF50; -jfx-unfocus-color: #9E9E9E;");

                specificFieldsContainer.getChildren().addAll(
                        new Label("Quantità Semi:"), sowingQuantityField,
                        new Label("Unità di Misura:"), sowingUnitField
                );
                break;
            case "Raccolta":
                harvestPlannedQuantityField = new JFXTextField();
                harvestPlannedQuantityField.setPromptText("Quantità Prevista");
                harvestPlannedQuantityField.setStyle("-jfx-focus-color: #4CAF50; -jfx-unfocus-color: #9E9E9E;");
                harvestPlannedQuantityField.textProperty().addListener((observable, oldValue, newValue) -> {
                    // Accetta solo numeri interi (o decimali se decidi di cambiare double)
                    if (!newValue.matches("\\d*")) {
                        harvestPlannedQuantityField.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                });

                harvestActualQuantityField = new JFXTextField();
                harvestActualQuantityField.setPromptText("Quantità Effettiva");
                harvestActualQuantityField.setStyle("-jfx-focus-color: #4CAF50; -jfx-unfocus-color: #9E9E9E;");
                harvestActualQuantityField.textProperty().addListener((observable, oldValue, newValue) -> {
                    // Accetta solo numeri interi (o decimali se decidi di cambiare double)
                    if (!newValue.matches("\\d*")) {
                        harvestActualQuantityField.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                });

                harvestUnitField = new JFXTextField();
                harvestUnitField.setPromptText("Unità di Misura (es. kg)");
                harvestUnitField.setStyle("-jfx-focus-color: #4CAF50; -jfx-unfocus-color: #9E9E9E;");

                specificFieldsContainer.getChildren().addAll(
                        new Label("Quantità Prevista:"), harvestPlannedQuantityField,
                        new Label("Quantità Effettiva:"), harvestActualQuantityField,
                        new Label("Unità di Misura:"), harvestUnitField
                );
                break;
            case "Irrigazione":
                // Nessun campo aggiuntivo per l'irrigazione
                break;
        }
    }


    @FXML
    @Override
    protected void handleCreate(ActionEvent event) {
        LocalDate date = datePicker.getValue();
        String activityType = activityTypeComboBox.getSelectionModel().getSelectedItem();
        Farmer selectedGrower = growerComboBox.getSelectionModel().getSelectedItem(); // Recupera il grower selezionato

        if (date == null || activityType == null || selectedGrower == null) { // Aggiungi validazione per grower
            Utils.showAlert(Alert.AlertType.WARNING, "Campi Mancanti", "Per favore, compila tutti i campi: data, tipo di attività e coltivatore.");
            return;
        }
        if (currentCultivation == null) {
            Utils.showAlert(Alert.AlertType.ERROR, "Errore Interno", "Coltivazione di riferimento non trovata.");
            return;
        }

        Activity newActivity = null;

        try {
            switch (activityType) {
                case "Semina":
                    if (sowingQuantityField.getText().isEmpty() || sowingUnitField.getText().isEmpty()) {
                        Utils.showAlert(Alert.AlertType.WARNING, "Campi Vuoti", "Per favore, inserisci quantità e unità di misura per la semina.");
                        return;
                    }
                    int sowingQuantity = Integer.parseInt(sowingQuantityField.getText());
                    String sowingUnit = sowingUnitField.getText();
                    newActivity = new SeedingActivity(0, date, selectedGrower.getId(),selectedGrower.getFullName(),sowingQuantity, sowingUnit); // Passa selectedGrower
                    break;
                case "Irrigazione":
                    newActivity = new IrrigationActivity(0, date,selectedGrower.getId(), selectedGrower.getFullName()); // Passa selectedGrower
                    break;
                case "Raccolta":
                    if (harvestPlannedQuantityField.getText().isEmpty() || harvestActualQuantityField.getText().isEmpty() || harvestUnitField.getText().isEmpty()) {
                        Utils.showAlert(Alert.AlertType.WARNING, "Campi Vuoti", "Per favore, inserisci quantità previste, effettive e unità di misura per la raccolta.");
                        return;
                    }
                    int planned = Integer.parseInt(harvestPlannedQuantityField.getText());
                    int actual = Integer.parseInt(harvestActualQuantityField.getText());
                    String harvestUnit = harvestUnitField.getText();
                    newActivity = new HarvestingActivity(0, date, selectedGrower.getId(),selectedGrower.getFullName(),planned, actual, harvestUnit); // Passa selectedGrower
                    break;
            }

            if (newActivity != null) {
                projectService.addActivityToColture(currentCultivation, newActivity);
                Utils.showAlert(Alert.AlertType.INFORMATION, "Successo", "Attività creata e aggiunta con successo.");
                if (onActivityCreated != null) {
                    onActivityCreated.run();
                }
                closeStage(event);
            }

        } catch (NumberFormatException e) {
            Utils.showAlert(Alert.AlertType.ERROR, "Input non valido", "Per favore, inserisci valori numerici validi per le quantità.");
        } catch (RuntimeException e) {
            Utils.showAlert(Alert.AlertType.ERROR, "Errore Creazione Attività", "Errore: " + e.getMessage());
            e.printStackTrace();
        }
    }

}