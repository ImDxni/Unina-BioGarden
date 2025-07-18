package com.unina.biogarden.dto;

/**
 * Data Transfer Object (DTO) for representing a user.
 * This class encapsulates user data, providing a clean way to transfer it between different layers of the application.
 */
public record UtenteDTO(
    int id,          // Unique identifier for the user
    String nome,    // First name of the user
    String cognome, // Last name of the user
    String password, // Password of the user
    String email,   // Email address of the user (must be unique)
    String tipo     // Type or role of the user (e.g., admin, regular user)
) {
    /**
     * Constructs a new UtenteDTO with the specified details.
     *
     * @param id      The unique identifier for the user.
     * @param nome    The first name of the user.
     * @param cognome The last name of the user.
     * @param email   The email address of the user.
     * @param tipo    The type or role of the user.
     */
    public UtenteDTO {
    }

    /**
     * Creates an abstract instance of UtenteDTO with default values.
     * This can be used as a placeholder or for testing purposes.
     *
     * @return A new UtenteDTO instance with default values.
     */
    public static UtenteDTO emptyInstance() {
        return new UtenteDTO(0, "", "", "", "", "");
    }
}