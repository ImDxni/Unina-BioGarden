package com.unina.biogarden.controller;

import com.unina.biogarden.dao.UtenteDAO;
import com.unina.biogarden.exceptions.UtenteEsistenteException;
import com.unina.biogarden.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the user registration view.
 * This class handles user input for registration, validates the data,
 * attempts to register a new user via {@link UtenteDAO}, and manages navigation
 * back to the login screen.
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
     * Initializes the controller after its root element has been completely processed.
     * This method sets the initial visibility of the error label and customizes the
     * display of items in the 'tipologiaCombo' (user type dropdown).
     */
    @FXML
    private void initialize() {
        errorLabel.setVisible(false);
        // Custom cell factory for the ComboBox to style the "Tipologia..." placeholder
        tipologiaCombo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    if (item.equals("Tipologia...")) {
                        setStyle("-fx-text-fill: #b0b0b0;"); // Grey out placeholder
                    } else {
                        setStyle("-fx-text-fill: #388e3c;"); // Green for actual selections
                    }
                }
            }
        });

        // Custom button cell for the ComboBox to style the selected item display
        tipologiaCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    if (item.equals("Tipologia...")) {
                        setStyle("-fx-text-fill: #b0b0b0;"); // Grey out placeholder
                    } else {
                        setStyle("-fx-text-fill: #388e3c;"); // Green for actual selections
                    }
                }
            }
        });

        tipologiaCombo.getSelectionModel().selectFirst(); // Select the first item by default
    }

    /**
     * Handles the action when the register button is pressed.
     * It validates all input fields, attempts to register the user through {@link UtenteDAO},
     * and displays error messages if validation fails or if the user already exists.
     */
    @FXML
    private void onRegister() {
        errorLabel.setVisible(false);

        UtenteDAO userDao = new UtenteDAO();

        String nome = nomeField.getText();
        String cognome = cognomeField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if(nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || password.isEmpty()){
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

        try{
            userDao.registerUser(nome,cognome,email,password,tipo);
            System.out.println("Utente registrato!");

            onLoginLink();
        }catch(UtenteEsistenteException ex){
            errorLabel.setVisible(true);
            errorLabel.setText("Esiste già un utente con questo indirizzo");
        } catch (RuntimeException e) {
            errorLabel.setVisible(true);
            errorLabel.setText("Errore durante la registrazione: " + e.getMessage());
            e.printStackTrace(); // Log the actual exception for debugging
        }
    }

    /**
     * Handles the action when the login hyperlink is clicked.
     * It loads the login view FXML and sets it as the new scene on the current stage,
     * applying the predefined stylesheet.
     */
    @FXML
    private void onLoginLink() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/unina/biogarden/login-view.fxml"));
            Stage stage = (Stage) loginLink.getScene().getWindow();
            Utils.setSceneWithStylesheet(stage, loader.load());
        } catch (IOException e) {
            e.printStackTrace(); // Log the exception in a real application
            // TODO: Display an error message to the user
        }
    }
}