package com.unina.biogarden.dto;

/**
 * Represents a Data Transfer Object (DTO) for a 'lotto' (lot/plot of land).
 * Used to transfer data related to a lot between different application layers.
 */
public record LotDTO(
    int id,          // Unique identifier for the lot
    String nome,    // Name of the lot
    int area        // Area of the lot in square meters
) {
}