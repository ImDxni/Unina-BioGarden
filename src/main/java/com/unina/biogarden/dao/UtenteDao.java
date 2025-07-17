package com.unina.biogarden.dao;

import com.unina.biogarden.database.ConnectionManager;
import com.unina.biogarden.dto.UtenteDTO;
import com.unina.biogarden.exceptions.LoginFallitoException;
import com.unina.biogarden.exceptions.UtenteEsistenteException;
import com.unina.biogarden.utils.Utils;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Data Access Object (DAO) for managing user-related database operations.
 * This class provides methods for user registration and login, interacting with the database
 * through a connection pool managed by {@link ConnectionManager}.
 */
public class UtenteDao {
    private final DataSource dataSource = ConnectionManager.getDataSource();

    /**
     * Registers a new user in the database.
     * This method encrypts the password before storing it and calls a stored procedure to handle the insertion.
     *
     * @param nome The first name of the user.
     * @param cognome The last name of the user.
     * @param email The email address of the user (must be unique).
     * @param password The plain-text password of the user.
     * @param tipo The type or role of the user.
     * @return An {@link UtenteDTO} representing the newly registered user, including their generated ID.
     * @throws UtenteEsistenteException If a user with the provided email already exists.
     * @throws RuntimeException If a general SQL error occurs during registration.
     */
    public UtenteDTO registerUser(String nome, String cognome, String email, String password, String tipo) throws UtenteEsistenteException {
        int id = 1;

        String hashedPassword = Utils.encryptPassword(password);

        try(Connection conn = dataSource.getConnection()){
            CallableStatement stmt = conn.prepareCall("{call registraUtente(?, ?, ?, ?, ?,?)}");

            stmt.setString(1, nome);
            stmt.setString(2, cognome);
            stmt.setString(3, email);
            stmt.setString(4, hashedPassword);
            stmt.setString(5, tipo);
            stmt.registerOutParameter(6, java.sql.Types.INTEGER);
            stmt.executeUpdate();
            id = stmt.getInt(6);
        }catch(SQLException ex) {
            if (ex.getSQLState().equals("45000")) {
                throw new UtenteEsistenteException("Utente con email " + email + " già esistente");
            } else {
                throw new RuntimeException("Errore durante la registrazione di utente ", ex);
            }
        }

        return new UtenteDTO(id, nome, cognome, email, password, tipo);
    }

    /**
     * Authenticates a user by checking their email and password against stored credentials.
     * It retrieves the user's data based on the email and then verifies the provided password
     * against the stored hashed password.
     *
     * @param email The email address of the user attempting to log in.
     * @param password The plain-text password provided by the user.
     * @return An {@link UtenteDTO} representing the logged-in user if credentials are valid.
     * @throws LoginFallitoException If the user is not found or the password is incorrect.
     * @throws RuntimeException If a general SQL error occurs during the login process.
     */
    public UtenteDTO loginUser(String email, String password) throws LoginFallitoException {
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

                    return new UtenteDTO(id, nome, cognome, email, storedPassword, tipo);
                } else {
                    throw new LoginFallitoException("Password errata");
                }
            } else {
                throw new LoginFallitoException("Utente non trovato");
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Errore durante il login dell'utente", ex);
        }
    }
}