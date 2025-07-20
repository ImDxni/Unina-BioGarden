package com.unina.biogarden.controller;

import com.unina.biogarden.dao.UserDAO;
import com.unina.biogarden.dto.UserDTO;
import com.unina.biogarden.enumerations.UserType;
import com.unina.biogarden.exceptions.UtenteEsistenteException;
import com.unina.biogarden.service.UserService;
import com.unina.biogarden.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller per la vista di registrazione dell'utente.
 * Questa classe gestisce l'input dell'utente per la registrazione, valida i dati,
 * tenta di registrare un nuovo utente tramite il {@link UserService}, e gestisce la navigazione
 * di ritorno alla schermata di login.
 * @author Il Tuo Nome
 */
public class RegisterController {
    @FXML
    private Label errorLabel;
    @FXML
    private TextField nomeField;

    @FXML
    private TextField cognomeField;

    @FXML
    private TextField emailField;

    @FXML
    private ComboBox<String> tipologiaCombo;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Hyperlink loginLink;

    /**
     * Inizializza il controller dopo che il suo elemento radice è stato completamente elaborato.
     * Questo metodo imposta la visibilità iniziale dell'etichetta di errore e personalizza la
     * visualizzazione degli elementi nella 'tipologiaCombo' (dropdown del tipo di utente)
     * per includere uno stile per il placeholder e le selezioni reali.
     */
    @FXML
    private void initialize() {
        errorLabel.setVisible(false);
        tipologiaCombo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    if (item.equals("Tipologia...")) {
                        setStyle("-fx-text-fill: #b0b0b0;");
                    } else {
                        setStyle("-fx-text-fill: #388e3c;");
                    }
                }
            }
        });

        tipologiaCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    if (item.equals("Tipologia...")) {
                        setStyle("-fx-text-fill: #b0b0b0;");
                    } else {
                        setStyle("-fx-text-fill: #388e3c;");
                    }
                }
            }
        });

        tipologiaCombo.getSelectionModel().selectFirst();
    }

    /**
     * Gestisce l'azione quando il pulsante di registrazione viene premuto.
     * Valida tutti i campi di input, tenta di registrare l'utente tramite il {@link UserService},
     * e visualizza messaggi di errore se la validazione fallisce o se l'utente esiste già.
     * In caso di successo, naviga alla schermata di login.
     */
    @FXML
    private void onRegister() {
        errorLabel.setVisible(false);

        String nome = nomeField.getText();
        String cognome = cognomeField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || password.isEmpty()) {
            errorLabel.setVisible(true);
            errorLabel.setText("Compila tutti i campi!");
            return;
        }

        if (tipologiaCombo.getValue() == null || tipologiaCombo.getValue().equals("Tipologia...")) {
            errorLabel.setVisible(true);
            errorLabel.setText("Devi selezionare una tipologia!");
            return;
        }

        String tipo = tipologiaCombo.getValue();

        UserService service = new UserService();
        try {
            service.insert(new UserDTO(1, nome, cognome, email, password, UserType.fromString(tipo)));

            System.out.println("Utente registrato!");

            onLoginLink();
        } catch (UtenteEsistenteException ex) {
            errorLabel.setVisible(true);
            errorLabel.setText("Esiste già un utente con questo indirizzo email.");
        } catch (RuntimeException e) {
            errorLabel.setVisible(true);
            errorLabel.setText("Errore durante la registrazione: " + e.getMessage());
            e.printStackTrace(); // Logga l'eccezione per il debug
        }
    }

    /**
     * Gestisce l'azione quando viene cliccato l'hyperlink "Accedi".
     * Carica il file FXML della vista di login e lo imposta come nuova scena sullo stage corrente,
     * applicando il foglio di stile predefinito.
     */
    @FXML
    private void onLoginLink() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/unina/biogarden/login-view.fxml"));
            Stage stage = (Stage) loginLink.getScene().getWindow();
            Utils.setSceneWithStylesheet(stage, loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}