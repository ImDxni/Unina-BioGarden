package com.unina.biogarden.controller.form;

import com.jfoenix.controls.JFXTextField;
import com.unina.biogarden.service.ProjectService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import static com.unina.biogarden.utils.Utils.showAlert;

public class CreateCropFormController extends AbstractForm {

    @FXML
    private JFXTextField cropNameField;
    @FXML
    private JFXTextField growthTimeField;

    private Runnable onCropCreated;

    private final ProjectService service = new ProjectService();

    @FXML
    public void initialize() {
        growthTimeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                growthTimeField.setText(newValue.replaceAll("\\D", ""));
            }
        });
    }

    public void setOnCropCreated(Runnable onCropCreated) {
        this.onCropCreated = onCropCreated;
    }

    @FXML
    protected void handleCreate(ActionEvent event) {
        String nomeColtura = cropNameField.getText();
        String tempoMaturazioneText = growthTimeField.getText();

        if (nomeColtura.isEmpty() || tempoMaturazioneText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Campi Vuoti", "Per favore, compila tutti i campi.");
            return;
        }

        int tempoMaturazione;
        try {
            tempoMaturazione = Integer.parseInt(tempoMaturazioneText);
            if (tempoMaturazione <= 0) {
                showAlert(Alert.AlertType.ERROR, "Input non valido", "I giorni di maturazione devono essere un numero positivo.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input non valido", "I giorni di maturazione devono essere un numero intero.");
            return;
        }

        service.createCrop(nomeColtura, tempoMaturazione);
        try {
            if (onCropCreated != null) {
                onCropCreated.run();
            }

            closeStage(event);
        } catch (IllegalStateException e) { // Cattura l'eccezione per duplicati dalla DAO
            showAlert(Alert.AlertType.ERROR, "Errore Creazione Coltura", e.getMessage());
        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.ERROR, "Errore Database", "Si è verificato un errore durante la creazione della coltura: " + e.getMessage());
            e.printStackTrace();
        }
    }
}