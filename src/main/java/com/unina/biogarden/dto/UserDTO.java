package com.unina.biogarden.dto;

import com.unina.biogarden.enumerations.UserType;

/**
 * Data Transfer Object (DTO) for representing a user.
 * This class encapsulates user data, providing a clean way to transfer it between different layers of the application.
 */
public record UserDTO(
        int id,          // Unique identifier for the user
        String nome,    // First name of the user
        String cognome, // Last name of the user
        String password, // Password of the user
        String email,   // Email address of the user (must be unique)
        UserType tipo     // Type or role of the user (e.g., admin, regular user)
) {
}