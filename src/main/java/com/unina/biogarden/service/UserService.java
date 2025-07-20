package com.unina.biogarden.service;

import com.unina.biogarden.dao.UserDAO;
import com.unina.biogarden.dto.UserDTO;
import com.unina.biogarden.enumerations.UserType;
import com.unina.biogarden.exceptions.LoginFallitoException;
import com.unina.biogarden.exceptions.UtenteEsistenteException;
import com.unina.biogarden.models.Farmer;
import com.unina.biogarden.session.Session;

import java.util.Collection;

/**
 * Servizio per la gestione degli utenti nel sistema BioGarden.
 * Questa classe gestisce la logica di business relativa agli utenti,
 * inclusi il login, la registrazione e il recupero delle informazioni sugli utenti.
 * Estende {@link AbstractService} per le operazioni CRUD di base sui {@link UserDTO}.
 * @author Il Tuo Nome
 */
public class UserService extends AbstractService<UserDTO> {

    private final UserDAO dao = new UserDAO();

    private static Collection<UserDTO> users;

    /**
     * Costruisce una nuova istanza di {@code UserService}.
     * All'inizializzazione, recupera tutti gli utenti dal database e li memorizza in una cache statica.
     */
    public UserService() {
        this.users = dao.fetchAllUsers();
    }

    /**
     * Recupera tutti gli utenti di tipo {@link UserType#FARMER} (coltivatore).
     * Mappa i {@link UserDTO} a oggetti {@link Farmer}.
     * @return Una collezione di oggetti {@link Farmer}.
     */
    public Collection<Farmer> fetchAllFarmer() {
        return fetchAll().stream().filter(user -> user.tipo() == UserType.FARMER)
                .map(user -> new Farmer(
                        user.id(),
                        user.nome(),
                        user.cognome(),
                        user.email()
                ))
                .toList();
    }

    /**
     * Tenta di autenticare un utente con le credenziali fornite.
     * In caso di successo, aggiorna la cache degli utenti e avvia una nuova sessione utente.
     * @param email L'email dell'utente.
     * @param password La password dell'utente.
     * @throws LoginFallitoException Se le credenziali non sono valide e il login fallisce.
     */
    public void login(String email, String password) throws LoginFallitoException {
        UserDTO dto = dao.loginUser(email, password);
        this.users = dao.fetchAllUsers(); // Aggiorna la cache dopo un login riuscito
        Session.login(dto);
    }

    /**
     * Inserisce un nuovo utente nel sistema.
     * @param entity Il {@link UserDTO} contenente i dati del nuovo utente da registrare.
     * @return Il {@link UserDTO} dell'utente registrato.
     * @throws UtenteEsistenteException Se un utente con la stessa email esiste già.
     */
    @Override
    public UserDTO insert(UserDTO entity) throws UtenteEsistenteException {
        dao.registerUser(entity.nome(), entity.cognome(), entity.password(), entity.email(), entity.tipo());
        return entity;
    }

    /**
     * Recupera tutti gli utenti attualmente presenti nella cache del servizio.
     * @return Una collezione di tutti i {@link UserDTO} memorizzati nella cache.
     */
    @Override
    public Collection<UserDTO> fetchAll() {
        return users;
    }

    /**
     * Fornisce accesso statico alla collezione di tutti gli utenti memorizzati nella cache.
     * Questo metodo è utile per accedere agli utenti senza istanziare il servizio.
     * @return Una collezione statica di tutti i {@link UserDTO}.
     */
    public static Collection<UserDTO> getUsers() {
        return users;
    }
}