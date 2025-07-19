package com.unina.biogarden.service;

import com.unina.biogarden.dao.UserDAO;
import com.unina.biogarden.dto.UserDTO;
import com.unina.biogarden.enumerations.UserType;
import com.unina.biogarden.exceptions.LoginFallitoException;
import com.unina.biogarden.exceptions.UtenteEsistenteException;
import com.unina.biogarden.models.Farmer;
import com.unina.biogarden.session.Session;

import java.util.Collection;

public class UserService extends AbstractService<UserDTO>{

    private final UserDAO dao = new UserDAO();

    private static Collection<UserDTO> users;

    public UserService() {
        this.users = dao.fetchAllUsers();
    }

    public Collection<Farmer> fetchAllFarmer(){
        return fetchAll().stream().filter(user -> user.tipo() == UserType.FARMER)
                .map(user -> new Farmer(
                        user.id(),
                        user.nome(),
                        user.cognome(),
                        user.email()
                ))
                .toList();
    }

    public void login(String email, String password) throws LoginFallitoException {
        UserDTO dto = dao.loginUser(email, password);
        this.users = dao.fetchAllUsers();
        Session.login(dto);
    }


    @Override
    public UserDTO insert(UserDTO entity) throws UtenteEsistenteException {
        dao.registerUser(entity.nome(), entity.cognome(), entity.password(), entity.email(), entity.tipo());
        return entity;
    }

    @Override
    public Collection<UserDTO> fetchAll() {
        return users;
    }

    public static Collection<UserDTO> getUsers() {
        return users;
    }
}
