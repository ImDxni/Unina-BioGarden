package com.unina.biogarden.dto;

/**
 * Represents a Data Transfer Object (DTO) for a 'lotto' (lot/plot of land).
 * Used to transfer data related to a lot between different application layers.
 */
public record LottoDTO(
    int id,          // Unique identifier for the lot
    String nome,    // Name of the lot
    int area        // Area of the lot in square meters
) {
    /**
     * Constructs a new LottoDTO with the specified details.
     *
     * @param id   The unique identifier for the lot.
     * @param nome The name of the lot.
     * @param area The area of the lot in square meters.
     */
    public LottoDTO {
        // No additional logic needed in the constructor for this record
    }

    /**
     * Creates an abstract instance of LottoDTO with default values.
     * This can be used as a placeholder or for testing purposes.
     *
     * @return A new LottoDTO instance with default values.
     */
    public static LottoDTO emptyInstance() {
        return new LottoDTO(0, "", 0);
    }
}