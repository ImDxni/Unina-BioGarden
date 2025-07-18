package com.unina.biogarden.controller.form;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.unina.biogarden.dao.LottoDAO;
import com.unina.biogarden.dao.ProgettoDAO;
import com.unina.biogarden.dto.LottoDTO;
import com.unina.biogarden.dto.ProgettoDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import java.time.LocalDate;
import java.util.Collection;

import static com.unina.biogarden.utils.Utils.showAlert;

public class CreateProjectFormController extends AbstractForm{

    @FXML
    private JFXTextField projectNameField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private JFXComboBox<LottoDTO> lotComboBox;

    private ProgettoDAO progettoDAO;
    private LottoDAO lottoDAO;

    private Runnable createRunnable;

    public void setOnProjectCreated(Runnable createRunnable) {
        this.createRunnable = createRunnable;
    }

    @FXML
    public void initialize() {
        progettoDAO = new ProgettoDAO();
        lottoDAO = new LottoDAO(); // Inizializza LottoDAO
        populateLotComboBox();
    }

    private void populateLotComboBox() {
        try {
            Collection<LottoDTO> lotti = lottoDAO.getAllLots();
            ObservableList<LottoDTO> observableLotti = FXCollections.observableArrayList(lotti);
            lotComboBox.setItems(observableLotti);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Errore Caricamento Lotti", "Impossibile caricare i lotti dal database.");
            e.printStackTrace();
        }
    }

    @Override
    @FXML
    protected void handleCreate(ActionEvent event) {
        String nome = projectNameField.getText();
        LocalDate dataInizio = startDatePicker.getValue();
        LocalDate dataFine = endDatePicker.getValue();
        LottoDTO selectedLot = lotComboBox.getSelectionModel().getSelectedItem();

        if (nome.isEmpty() || dataInizio == null || dataFine == null || selectedLot == null) {
            showAlert(Alert.AlertType.WARNING, "Campi Vuoti", "Per favore, compila tutti i campi.");
            return;
        }

        if (dataInizio.isAfter(dataFine)) {
            showAlert(Alert.AlertType.ERROR, "Errore Date", "La data di inizio non può essere successiva alla data di fine.");
            return;
        }

        try {
            progettoDAO.creaProgetto(nome, dataInizio, dataFine, selectedLot.id());

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