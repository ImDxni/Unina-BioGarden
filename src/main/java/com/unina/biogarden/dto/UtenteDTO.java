package com.unina.biogarden.dto;

/**
 * Data Transfer Object (DTO) for representing a user.
 * This class encapsulates user data, providing a clean way to transfer it between different layers of the application.
 */
public class UtenteDTO {
    /**
     * The unique identifier for the user.
     */
    private int id;
    /**
     * The first name of the user.
     */
    private String nome;
    /**
     * The last name of the user.
     */
    private String cognome;
    /**
     * The email address of the user, which can also be used as a username.
     */
    private String email;
    /**
     * The password of the user. (Hashed or encrypted for security reasons)
     */
    private String password;
    /**
     * The type or role of the user (e.g., "proprietario", "coltivatore").
     */
    private String tipo;

    /**
     * Constructs a new UtenteDTO with the specified details.
     *
     * @param id The unique identifier for the user.
     * @param nome The first name of the user.
     * @param cognome The last name of the user.
     * @param email The email address of the user.
     * @param password The password of the user.
     * @param tipo The type or role of the user.
     */
    public UtenteDTO(int id, String nome, String cognome, String email, String password, String tipo) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.tipo = tipo;
    }

    /**
     * Returns the unique identifier of the user.
     *
     * @return The user's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the user.
     *
     * @param id The new ID for the user.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the first name of the user.
     *
     * @return The user's first name.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Sets the first name of the user.
     *
     * @param nome The new first name for the user.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Returns the last name of the user.
     *
     * @return The user's last name.
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Sets the last name of the user.
     *
     * @param cognome The new last name for the user.
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Returns the email address of the user.
     *
     * @return The user's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email The new email address for the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the password of the user.
     *
     * @return The user's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password The new password for the user.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the type or role of the user.
     *
     * @return The user's type.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Sets the type or role of the user.
     *
     * @param tipo The new type for the user.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}