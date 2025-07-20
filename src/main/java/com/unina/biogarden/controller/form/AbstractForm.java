package com.unina.biogarden.controller.form;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * Questa classe astratta fornisce la base per i controller dei form nell'applicazione BioGarden.
 * Include metodi comuni per la gestione delle azioni di chiusura e un metodo astratto per la creazione.
 * @author Il Tuo Nome
 */
public abstract class AbstractForm {

    /**
     * Gestisce l'evento di creazione (ad esempio, l'invio di un form).
     * Questo metodo deve essere implementato dalle sottoclassi.
     * @param event L'evento di azione che ha scatenato la chiamata.
     */
    abstract void handleCreate(ActionEvent event);

    /**
     * Gestisce l'azione di annullamento, chiudendo la finestra corrente del form.
     * Questo metodo è tipicamente associato a un bottone "Annulla" nell'interfaccia utente.
     * @param event L'evento di azione che ha scatenato la chiamata, solitamente da un JFXButton.
     */
    @FXML
    protected void cancel(ActionEvent event) {
        closeStage(event); // Chiude la finestra
    }

    /**
     * Chiude la finestra (Stage) associata all'evento.
     * Questo metodo recupera lo Stage dal nodo sorgente dell'evento e lo chiude.
     * @param event L'evento di azione che ha scatenato la chiusura, tipicamente da un JFXButton.
     */
    protected void closeStage(ActionEvent event) {
        Stage stage = (Stage) ((JFXButton) event.getSource()).getScene().getWindow();
        stage.close();
    }
}