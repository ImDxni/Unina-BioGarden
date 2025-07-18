package com.unina.biogarden.controller;

import com.unina.biogarden.dao.UtenteDao;
import com.unina.biogarden.dto.UtenteDTO;
import com.unina.biogarden.session.Session;
import com.unina.biogarden.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the user login view.
 * This class handles user interactions on the login screen, including
 * authenticating users and navigating to the registration view.
 */
public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    /**
     * Handles the action when the login button is pressed.
     * It validates input fields, attempts to log in the user using {@link UtenteDao},
     * and displays appropriate error messages or proceeds with successful login.
     */
    @FXML
    private void onLogin() {
        errorLabel.setVisible(false);
        UtenteDao userDao = new UtenteDao();

        String email = emailField.getText();
        String password = passwordField.getText();

        if(email.isEmpty() || password.isEmpty()){
            errorLabel.setVisible(true);
            errorLabel.setText("Email e password non possono essere vuoti.");
            return;
        }

        UtenteDTO user;
        try {
            user = userDao.loginUser(email, password);
        } catch (Exception e) {
            errorLabel.setText("Login fallito: " + e.getMessage());
            errorLabel.setVisible(true);
            return;
        }

        Session.login(user);

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
     * Handles the action when the register link is clicked.
     * It loads the registration view FXML and sets it as the new scene on the current stage,
     * applying the predefined stylesheet.
     */
    @FXML
    private void onRegisterLink() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/unina/biogarden/register-view.fxml"));
            Stage stage = (Stage) emailField.getScene().getWindow();
            Utils.setSceneWithStylesheet(stage, loader.load());
        } catch (IOException e) {
            e.printStackTrace(); // Log the exception in a real application
            // TODO: Display an error message to the user
        }
    }
}