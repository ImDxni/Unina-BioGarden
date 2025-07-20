package com.unina.biogarden.controller;

import com.unina.biogarden.dao.UserDAO;
import com.unina.biogarden.dto.UserDTO;
import com.unina.biogarden.enumerations.UserType;
import com.unina.biogarden.service.UserService;
import com.unina.biogarden.session.Session;
import com.unina.biogarden.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller per la vista di login dell'utente.
 * Questa classe gestisce le interazioni dell'utente nella schermata di login,
 * inclusa l'autenticazione degli utenti e la navigazione alla vista di registrazione.
 * @author Il Tuo Nome
 */
public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    /**
     * Gestisce l'azione quando il pulsante di login viene premuto.
     * Valida i campi di input (email e password), tenta di autenticare l'utente
     * tramite il {@link UserService}, e verifica il tipo di utente.
     * Se l'autenticazione ha successo e l'utente non è un {@code FARMER},
     * naviga alla vista della dashboard. Altrimenti, mostra un messaggio di errore appropriato.
     */
    @FXML
    private void onLogin() {
        errorLabel.setVisible(false);
        UserService service = new UserService();

        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setVisible(true);
            errorLabel.setText("Email e password non possono essere vuoti.");
            return;
        }

        try {
            service.login(email, password);
        } catch (Exception e) {
            errorLabel.setText("Login fallito: " + e.getMessage());
            errorLabel.setVisible(true);
            return;
        }

        UserDTO sessionUser = Session.getUtente();

        if (sessionUser.tipo() == UserType.FARMER) {
            errorLabel.setText("Accesso non autorizzato: l'utente è un agricoltore.");
            errorLabel.setVisible(true);
            return;
        }

        System.out.println("Login effettuato con successo per l'utente: " + email);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/unina/biogarden/dashboard-view.fxml"));
            Stage stage = (Stage) emailField.getScene().getWindow();
            Utils.setSceneWithStylesheet(stage, loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gestisce l'azione quando viene cliccato il link di registrazione.
     * Carica il file FXML della vista di registrazione e lo imposta come nuova scena sullo stage corrente,
     * applicando il foglio di stile predefinito. Se il file FXML non può essere caricato, stampa lo stack trace.
     */
    @FXML
    private void onRegisterLink() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/unina/biogarden/register-view.fxml"));
            Stage stage = (Stage) emailField.getScene().getWindow();
            Utils.setSceneWithStylesheet(stage, loader.load());
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: Mostra un messaggio di errore all'utente se la vista di registrazione non può essere caricata.
        }
    }
}