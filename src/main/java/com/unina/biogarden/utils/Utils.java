package com.unina.biogarden.utils;

import com.unina.biogarden.BioGarden;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

/**
 * Utility class for password encryption and verification, and for UI-related helper methods in a JavaFX application.
 * This class provides methods to securely encrypt passwords using PBKDF2WithHmacSHA1
 * and to verify a given password against a stored encrypted password.
 * It also includes a utility method to set a JavaFX scene with a predefined stylesheet.
 */
public class Utils {

    /**
     * Encrypts a plain-text password using a randomly generated salt and PBKDF2WithHmacSHA1.
     * The salt is prepended to the hashed password, separated by a colon, and both are Base64 encoded.
     *
     * @param password The plain-text password to encrypt.
     * @return A Base64 encoded string containing the salt and the hashed password, separated by a colon (e.g., "saltBase64:hashBase64").
     */
    public static String encryptPassword(String password){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        byte[] hash = encrypt(password, salt);

        String saltBase64 = Base64.getEncoder().encodeToString(salt);
        String hashBase64 = Base64.getEncoder().encodeToString(hash);
        return saltBase64 + ":" + hashBase64;
    }

    /**
     * Verifies a plain-text password against a stored encrypted password.
     * It extracts the salt and hash from the stored password, re-hashes the given password with the extracted salt,
     * and compares the newly computed hash with the stored hash.
     *
     * @param password The plain-text password to verify.
     * @param storedPassword The stored encrypted password string (in "saltBase64:hashBase64" format).
     * @return true if the password matches the stored encrypted password, false otherwise.
     * @throws IllegalArgumentException If the format of the stored password is invalid.
     */
    public static boolean verifyPassword(String password, String storedPassword) {
        String[] parts = storedPassword.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Stored password format is invalid");
        }

        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] hash = Base64.getDecoder().decode(parts[1]);

        byte[] computedHash = encrypt(password, salt);

        return Arrays.equals(hash, computedHash);
    }

    /**
     * Hashes a password using PBKDF2WithHmacSHA1 with a given salt.
     * This is a private helper method used internally for both encryption and verification processes.
     *
     * @param password The plain-text password to hash.
     * @param salt The salt to use for hashing.
     * @return The hashed password as a byte array.
     * @throws RuntimeException If a suitable cryptographic algorithm is not found or key specification is invalid.
     */
    private static byte[] encrypt(String password, byte[] salt) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        byte[] computedHash;
        try {
            computedHash = factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

        return computedHash;
    }

    /**
     * Sets a new scene on the given stage and applies a default stylesheet.
     * This method is useful for consistently styling different views in a JavaFX application.
     *
     * @param stage The {@link Stage} on which to set the new scene.
     * @param root The {@link Parent} node that serves as the root of the new scene's content graph.
     */
    public static void setSceneWithStylesheet(Stage stage, Parent root) {
        Scene scene = new Scene(root);
        scene.getStylesheets().add(BioGarden.class.getResource("/com/unina/biogarden/style.css").toExternalForm());
        stage.setScene(scene);
        stage.centerOnScreen();
    }
}