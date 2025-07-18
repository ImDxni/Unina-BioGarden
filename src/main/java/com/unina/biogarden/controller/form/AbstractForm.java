package com.unina.biogarden.controller.form;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.w3c.dom.Node;

public abstract class AbstractForm {

    abstract void handleCreate(ActionEvent event);

    @FXML
    protected void cancel(ActionEvent event) {
        closeStage(event); // Chiude la finestra
    }

    protected void closeStage(ActionEvent event) {
        Stage stage = (Stage) ((JFXButton) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
