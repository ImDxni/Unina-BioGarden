package com.unina.biogarden.controller.form;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.unina.biogarden.dto.ProjectDTO;
import com.unina.biogarden.models.Lot;
import com.unina.biogarden.service.ProjectService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;

import static com.unina.biogarden.utils.Utils.showAlert;

/**
 * Controller per il form di creazione di un nuovo progetto.
 * Gestisce l'input del nome del progetto, le date di inizio e fine,
 * la selezione di un lotto e l'interazione con il servizio per la persistenza del progetto.
 * @author Il Tuo Nome
 */
public class CreateProjectFormController extends AbstractForm {

    @FXML
    private JFXTextField projectNameField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private JFXComboBox<Lot> lotComboBox;

    private Runnable createRunnable;

    private final ProjectService service = new ProjectService();

    /**
     * Imposta un callback {@code Runnable} da eseguire dopo che un nuovo progetto è stato creato con successo.
     * Questo è utile per aggiornare la vista chiamante o eseguire altre azioni post-creazione.
     * @param createRunnable Un {@code Runnable} che verrà eseguito.
     */
    public void setOnProjectCreated(Runnable createRunnable) {
        this.createRunnable = createRunnable;
    }

    /**
     * Inizializza il controller dopo che il suo FXML è stato completamente caricato.
     * Provvede a popolare la ComboBox dei lotti disponibili.
     */
    @FXML
    public void initialize() {
        populateLotComboBox();
    }

    /**
     * Popola la ComboBox {@code lotComboBox} con la lista di tutti i lotti disponibili
     * recuperati dal servizio {@code ProjectService}. In caso di errore durante il caricamento,
     * mostra un messaggio di avviso all'utente.
     */
    private void populateLotComboBox() {
        try {
            ObservableList<Lot> observableLotti = FXCollections.observableArrayList(service.fetchAllLots());
            lotComboBox.setItems(observableLotti);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Errore Caricamento Lotti", "Impossibile caricare i lotti dal database.");
            e.printStackTrace();
        }
    }

    /**
     * Gestisce l'evento di creazione di un nuovo progetto.
     * Valida gli input del form, crea un oggetto {@code ProjectDTO} e tenta di inserirlo tramite il servizio.
     * In caso di successo, esegue il callback {@code createRunnable} e chiude il form.
     * Gestisce anche errori di validazione dell'input (campi vuoti, date non valide) e altri errori di runtime.
     * @param event L'evento di azione che ha scatenato la chiamata, solitamente da un bottone "Crea".
     */
    @Override
    @FXML
    protected void handleCreate(ActionEvent event) {
        String nome = projectNameField.getText();
        LocalDate dataInizio = startDatePicker.getValue();
        LocalDate dataFine = endDatePicker.getValue();
        Lot selectedLot = lotComboBox.getSelectionModel().getSelectedItem();

        if (nome.isEmpty() || dataInizio == null || dataFine == null || selectedLot == null) {
            showAlert(Alert.AlertType.WARNING, "Campi Vuoti", "Per favore, compila tutti i campi.");
            return;
        }

        if (dataInizio.isAfter(dataFine)) {
            showAlert(Alert.AlertType.ERROR, "Errore Date", "La data di inizio non può essere successiva alla data di fine.");
            return;
        }

        try {
            service.insert(new ProjectDTO(
                    0,
                    nome,
                    dataInizio,
                    dataFine,
                    selectedLot.getId()
            ));

            if (createRunnable != null) {
                createRunnable.run();
            }

            closeStage(event);
        } catch (IllegalStateException e) {
            showAlert(Alert.AlertType.ERROR, "Errore Creazione Progetto", e.getMessage());
        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.ERROR, "Errore Database", "Si è verificato un errore durante la creazione del progetto: " + e.getMessage());
            e.printStackTrace();
        }
    }
}