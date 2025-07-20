package com.unina.biogarden.utils;

import com.unina.biogarden.BioGarden;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
 * Classe di utilità che fornisce metodi per la crittografia e verifica delle password,
 * e per funzionalità ausiliarie relative all'interfaccia utente JavaFX.
 * Questa classe offre metodi per crittografare in modo sicuro le password usando
 * l'algoritmo PBKDF2WithHmacSHA1 e per verificare una password data rispetto a una
 * password crittografata memorizzata. Include inoltre metodi di utilità per
 * impostare scene JavaFX con un foglio di stile predefinito e per mostrare avvisi.
 * @author Il Tuo Nome
 */
public class Utils {

    /**
     * Cripta una password in chiaro utilizzando un salt generato casualmente
     * e l'algoritmo PBKDF2WithHmacSHA1.
     * Il salt viene anteposto all'hash della password, separato da due punti,
     * ed entrambi sono codificati in Base64.
     *
     * @param password La password in chiaro da criptare.
     * @return Una stringa codificata in Base64 contenente il salt e l'hash della password,
     * separati da due punti (es. "saltBase64:hashBase64").
     * @throws RuntimeException Se si verificano problemi con gli algoritmi di crittografia sottostanti.
     */
    public static String encryptPassword(String password) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        byte[] hash = encrypt(password, salt);

        String saltBase64 = Base64.getEncoder().encodeToString(salt);
        String hashBase64 = Base64.getEncoder().encodeToString(hash);
        return saltBase64 + ":" + hashBase64;
    }

    /**
     * Verifica una password in chiaro rispetto a una password crittografata memorizzata.
     * Estrae il salt e l'hash dalla password memorizzata, ricalcola l'hash della password
     * fornita con il salt estratto e confronta l'hash appena calcolato con quello memorizzato.
     *
     * @param password La password in chiaro da verificare.
     * @param storedPassword La stringa della password crittografata memorizzata (nel formato "saltBase64:hashBase64").
     * @return {@code true} se la password corrisponde a quella crittografata memorizzata, {@code false} altrimenti.
     * @throws IllegalArgumentException Se il formato della password memorizzata non è valido.
     * @throws RuntimeException Se si verificano problemi con gli algoritmi di crittografia sottostanti.
     */
    public static boolean verifyPassword(String password, String storedPassword) {
        String[] parts = storedPassword.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Il formato della password memorizzata non è valido");
        }

        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] hash = Base64.getDecoder().decode(parts[1]);

        byte[] computedHash = encrypt(password, salt);

        return Arrays.equals(hash, computedHash);
    }

    /**
     * Calcola l'hash di una password utilizzando PBKDF2WithHmacSHA1 con un salt dato.
     * Questo è un metodo helper privato utilizzato internamente sia per i processi di crittografia che di verifica.
     *
     * @param password La password in chiaro di cui calcolare l'hash.
     * @param salt Il salt da utilizzare per l'hashing.
     * @return L'hash della password come array di byte.
     * @throws RuntimeException Se non viene trovato un algoritmo crittografico adatto o la specifica della chiave non è valida.
     */
    private static byte[] encrypt(String password, byte[] salt) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Errore nell'ottenimento dell'algoritmo di crittografia PBKDF2WithHmacSHA1", e);
        }

        byte[] computedHash;
        try {
            computedHash = factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("Errore nella generazione della chiave segreta", e);
        }

        return computedHash;
    }

    /**
     * Imposta una nuova scena sullo stage fornito e applica un foglio di stile predefinito.
     * Questo metodo è utile per stilare in modo consistente diverse viste in un'applicazione JavaFX.
     * La scena viene anche centrata sullo schermo.
     *
     * @param stage Lo {@link Stage} su cui impostare la nuova scena.
     * @param root Il nodo {@link Parent} che funge da radice del grafo del contenuto della nuova scena.
     */
    public static void setSceneWithStylesheet(Stage stage, Parent root) {
        Scene scene = new Scene(root);
        // Assicurati che il percorso del foglio di stile sia corretto e accessibile
        scene.getStylesheets().add(BioGarden.class.getResource("/com/unina/biogarden/style.css").toExternalForm());
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    /**
     * Mostra un messaggio di avviso (Alert) all'utente.
     *
     * @param type Il tipo di avviso (es. {@link Alert.AlertType#ERROR}, {@link Alert.AlertType#INFORMATION}).
     * @param title Il titolo della finestra di avviso.
     * @param message Il messaggio di contenuto dell'avviso.
     */
    public static void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null); // Non usiamo un header testuale separato
        alert.setContentText(message);
        alert.showAndWait(); // Mostra l'avviso e attende che l'utente lo chiuda
    }

    /**
     * Converte la prima lettera di una stringa in maiuscolo e le restanti in minuscolo.
     * Se la stringa è nulla o vuota, viene restituita così com'è.
     *
     * @param s La stringa da modificare.
     * @return La stringa con la prima lettera maiuscola e le successive minuscole,
     * o la stringa originale se nulla o vuota.
     */
    public static String firstCapitalLetter(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}