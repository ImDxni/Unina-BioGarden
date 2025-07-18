package com.unina.biogarden.controller.form;

import com.jfoenix.controls.JFXTextField;
import com.unina.biogarden.dao.LottoDAO;
import com.unina.biogarden.dto.LottoDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import static com.unina.biogarden.utils.Utils.showAlert;

public class NewLotFormController extends AbstractForm{

    @FXML
    private JFXTextField nameField;

    @FXML
    private JFXTextField areaField;

    private final LottoDAO dao = new LottoDAO();

    private Runnable createRunnable;

    public void setOnLotCreated(Runnable createRunnable) {
        this.createRunnable = createRunnable;
    }

    @FXML
    @Override
    protected void handleCreate(ActionEvent event) {
        String nome = nameField.getText();
        String areaText = areaField.getText();

        if (nome.isEmpty() || areaText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR,"Errore", "Per favore, compila tutti i campi.");
            return;
        }

        try {
            int area = Integer.parseInt(areaText);
            if (area <= 0) {
                showAlert(Alert.AlertType.ERROR,"Errore", "L'area deve essere un numero positivo.");
                return;
            }

            dao.createPlot(nome,area);
            try {

                if (createRunnable != null) {
                    createRunnable.run();
                }

                closeStage(event);
            } catch (IllegalStateException e) {
                showAlert(Alert.AlertType.ERROR, "Errore Creazione Lotto", e.getMessage());
            } catch (RuntimeException e) {
                showAlert(Alert.AlertType.ERROR, "Errore Database", "Si è verificato un errore durante la creazione del progetto: " + e.getMessage());
                e.printStackTrace();
            }


        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR,"Errore", "L'area deve essere un numero valido.");
        }
    }


}