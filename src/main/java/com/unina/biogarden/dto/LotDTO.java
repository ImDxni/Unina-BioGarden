package com.unina.biogarden.dto;

/**
 * Data Transfer Object (DTO) che rappresenta un lotto di terreno nel sistema BioGarden.
 * Questa è una classe record immutabile utilizzata per trasferire dati relativi a un lotto
 * tra i diversi strati dell'applicazione.
 *
 * @param id L'identificatore univoco del lotto.
 * @param nome Il nome assegnato al lotto.
 * @param area L'area del lotto, espressa in metri quadrati.
 * @author Il Tuo Nome
 */
public record LotDTO(
        int id,
        String nome,
        int area
) {
}