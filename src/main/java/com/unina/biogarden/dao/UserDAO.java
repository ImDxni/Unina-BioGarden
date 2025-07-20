package com.unina.biogarden.dao;

import com.unina.biogarden.database.ConnectionManager;
import com.unina.biogarden.dto.UserDTO;
import com.unina.biogarden.enumerations.UserType;
import com.unina.biogarden.exceptions.LoginFallitoException;
import com.unina.biogarden.exceptions.UtenteEsistenteException;
import com.unina.biogarden.utils.Utils;
import org.postgresql.util.PGobject;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Data Access Object (DAO) per la gestione delle operazioni di database relative agli utenti.
 * Questa classe fornisce metodi per la registrazione e il login degli utenti, interagendo con il database
 * tramite un pool di connessioni gestito da {@link ConnectionManager}.
 * @author Il Tuo Nome
 */
public class UserDAO {
    private final DataSource dataSource = ConnectionManager.getDataSource();

    /**
     * Registra un nuovo utente nel database.
     * Questo metodo cripta la password prima di memorizzarla e chiama una stored procedure per gestire l'inserimento.
     *
     * @param nome Il nome dell'utente.
     * @param cognome Il cognome dell'utente.
     * @param email L'indirizzo email dell'utente (deve essere unico).
     * @param password La password in chiaro dell'utente.
     * @param tipo Il tipo o ruolo dell'utente (es. Amministratore, Ricercatore, Agricoltore).
     * @throws UtenteEsistenteException Se esiste già un utente con l'indirizzo email fornito.
     * @throws RuntimeException Se si verifica un errore SQL generale durante la registrazione.
     */
    public void registerUser(String nome, String cognome, String email, String password, UserType tipo) throws UtenteEsistenteException {
        String hashedPassword = Utils.encryptPassword(password);

        try (Connection conn = dataSource.getConnection()) {
            CallableStatement stmt = conn.prepareCall("{ ? = call RegistraUtente(?, ?, ?, ?, ?::TipoUtente) }");

            stmt.registerOutParameter(1, java.sql.Types.INTEGER); // output
            stmt.setString(2, nome);
            stmt.setString(3, cognome);
            stmt.setString(4, email);
            stmt.setString(5, hashedPassword);  // password hashata

            PGobject tipoUtenteObj = new PGobject();
            tipoUtenteObj.setType("TipoUtente");
            tipoUtenteObj.setValue(tipo.getType().toLowerCase());

            stmt.setObject(6, tipoUtenteObj);

            stmt.executeUpdate();
        } catch (SQLException ex) {
            // "P0001" è un codice SQLSTATE per errori definiti dall'utente, qui usato per utente esistente.
            if (ex.getSQLState().equalsIgnoreCase("P0001")) {
                throw new UtenteEsistenteException("Utente con email " + email + " già esistente.");
            } else {
                throw new RuntimeException("Errore durante la registrazione dell'utente.", ex);
            }
        }
    }

    /**
     * Autentica un utente verificando la sua email e password rispetto alle credenziali memorizzate.
     * Recupera i dati dell'utente basandosi sull'email e quindi verifica la password fornita
     * rispetto alla password hashata memorizzata.
     *
     * @param email L'indirizzo email dell'utente che tenta di accedere.
     * @param password La password in chiaro fornita dall'utente.
     * @return Un {@link UserDTO} che rappresenta l'utente loggato se le credenziali sono valide.
     * @throws LoginFallitoException Se l'utente non viene trovato o la password non è corretta.
     * @throws RuntimeException Se si verifica un errore SQL generale durante il processo di login.
     */
    public UserDTO loginUser(String email, String password) throws LoginFallitoException {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmnt = conn.prepareStatement("SELECT * FROM utente WHERE email = ?");
            stmnt.setString(1, email);

            var rs = stmnt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");

                if (Utils.verifyPassword(password, storedPassword)) {
                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
                    String cognome = rs.getString("cognome");
                    String tipo = rs.getString("tipo");

                    return new UserDTO(id, nome, cognome, email, storedPassword, UserType.fromString(tipo));
                } else {
                    throw new LoginFallitoException("Password errata.");
                }
            } else {
                throw new LoginFallitoException("Utente non trovato.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Errore durante il login dell'utente.", ex);
        }
    }

    /**
     * Recupera una collezione di tutti gli utenti registrati nel database.
     *
     * @return Una {@link Collection} di {@link UserDTO} che rappresenta tutti gli utenti.
     * @throws RuntimeException Se si verifica un errore SQL durante il recupero degli utenti.
     */
    public Collection<UserDTO> fetchAllUsers() {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmnt = conn.prepareStatement("SELECT * FROM utente");
            var rs = stmnt.executeQuery();
            Collection<UserDTO> users = new java.util.ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String cognome = rs.getString("cognome");
                String email = rs.getString("email");
                String password = rs.getString("password"); // Nota: questa sarà la password hashata
                String tipo = rs.getString("tipo");

                users.add(new UserDTO(id, nome, cognome, email, password, UserType.fromString(tipo)));
            }
            return users;
        } catch (SQLException ex) {
            throw new RuntimeException("Errore durante il recupero degli utenti.", ex);
        }
    }
}