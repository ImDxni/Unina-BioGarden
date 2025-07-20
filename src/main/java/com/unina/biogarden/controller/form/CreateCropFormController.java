package com.unina.biogarden.controller.form;

import com.jfoenix.controls.JFXTextField;
import com.unina.biogarden.service.ProjectService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import static com.unina.biogarden.utils.Utils.showAlert;

/**
 * Controller per il form di creazione di un nuovo tipo di coltura.
 * Gestisce l'input del nome della coltura e del tempo di maturazione,
 * interagendo con il servizio per persistere la nuova coltura.
 * @author Il Tuo Nome
 */
public class CreateCropFormController extends AbstractForm {

    @FXML
    private JFXTextField cropNameField;
    @FXML
    private JFXTextField growthTimeField;

    private Runnable onCropCreated;

    private final ProjectService service = new ProjectService();

    /**
     * Inizializza il controller dopo che il suo FXML è stato completamente caricato.
     * Aggiunge un listener al campo {@code growthTimeField} per assicurare che accetti solo input numerici.
     */
    @FXML
    public void initialize() {
        growthTimeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                growthTimeField.setText(newValue.replaceAll("\\D", ""));
            }
        });
    }

    /**
     * Imposta un callback {@code Runnable} da eseguire dopo che una nuova coltura è stata creata con successo.
     * Questo è utile per aggiornare la vista chiamante o eseguire altre azioni post-creazione.
     * @param onCropCreated Un {@code Runnable} che verrà eseguito.
     */
    public void setOnCropCreated(Runnable onCropCreated) {
        this.onCropCreated = onCropCreated;
    }

    /**
     * Gestisce l'evento di creazione di una nuova coltura.
     * Valida gli input del nome della coltura e del tempo di maturazione, quindi tenta di creare la coltura.
     * In caso di successo, esegue il callback {@code onCropCreated} e chiude il form.
     * Gestisce anche errori di validazione dell'input, duplicati e altri errori di runtime.
     * @param event L'evento di azione che ha scatenato la chiamata, solitamente da un bottone "Crea".
     */
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

        try {
            service.createCrop(nomeColtura, tempoMaturazione);
            if (onCropCreated != null) {
                onCropCreated.run();
            }
            closeStage(event);
        } catch (IllegalStateException e) {
            showAlert(Alert.AlertType.ERROR, "Errore Creazione Coltura", e.getMessage());
        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.ERROR, "Errore Database", "Si è verificato un errore durante la creazione della coltura: " + e.getMessage());
            e.printStackTrace();
        }
    }
}