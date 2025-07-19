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
 * Data Access Object (DAO) for managing user-related database operations.
 * This class provides methods for user registration and login, interacting with the database
 * through a connection pool managed by {@link ConnectionManager}.
 */
public class UserDAO {
    private final DataSource dataSource = ConnectionManager.getDataSource();

    /**
     * Registers a new user in the database.
     * This method encrypts the password before storing it and calls a stored procedure to handle the insertion.
     *
     * @param nome     The first name of the user.
     * @param cognome  The last name of the user.
     * @param email    The email address of the user (must be unique).
     * @param password The plain-text password of the user.
     * @param tipo     The type or role of the user.
     * @throws UtenteEsistenteException If a user with the provided email already exists.
     * @throws RuntimeException         If a general SQL error occurs during registration.
     */
    public void registerUser(String nome, String cognome, String email, String password, UserType tipo) throws UtenteEsistenteException {
        String hashedPassword = Utils.encryptPassword(password);

        try(Connection conn = dataSource.getConnection()){
            CallableStatement stmt = conn.prepareCall("{ ? = call RegistraUtente(?, ?, ?, ?, ?::TipoUtente) }");

            stmt.registerOutParameter(1, java.sql.Types.INTEGER); // output
            stmt.setString(2, nome);
            stmt.setString(3, cognome);
            stmt.setString(4, email);
            stmt.setString(5, hashedPassword);  // hashed password

            PGobject tipoUtenteObj = new PGobject();
            tipoUtenteObj.setType("TipoUtente");
            tipoUtenteObj.setValue(tipo.getType().toLowerCase());

            stmt.setObject(6, tipoUtenteObj);

            stmt.executeUpdate();
        }catch(SQLException ex) {
            if (ex.getSQLState().equalsIgnoreCase("P0001")) {
                throw new UtenteEsistenteException("Utente con email " + email + " già esistente");
            } else {
                throw new RuntimeException("Errore durante la registrazione di utente ", ex);
            }
        }
    }

    /**
     * Authenticates a user by checking their email and password against stored credentials.
     * It retrieves the user's data based on the email and then verifies the provided password
     * against the stored hashed password.
     *
     * @param email The email address of the user attempting to log in.
     * @param password The plain-text password provided by the user.
     * @return An {@link UserDTO} representing the logged-in user if credentials are valid.
     * @throws LoginFallitoException If the user is not found or the password is incorrect.
     * @throws RuntimeException If a general SQL error occurs during the login process.
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
                    throw new LoginFallitoException("Password errata");
                }
            } else {
                throw new LoginFallitoException("Utente non trovato");
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Errore durante il login dell'utente", ex);
        }
    }

    public Collection<UserDTO> fetchAllUsers(){
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement stmnt = conn.prepareStatement("SELECT * FROM utente");
            var rs = stmnt.executeQuery();
            Collection<UserDTO> users = new java.util.ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String cognome = rs.getString("cognome");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String tipo = rs.getString("tipo");

                users.add(new UserDTO(id, nome, cognome, email, password, UserType.fromString(tipo)));
            }
            return users;
        } catch (SQLException ex) {
            throw new RuntimeException("Errore durante il recupero degli utenti", ex);
        }
    }


}