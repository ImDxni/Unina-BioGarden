package com.unina.biogarden.dto;

import java.time.LocalDate;

public record ProgettoDTO(int id, String nome, LocalDate dataInizio, LocalDate dataFine, int idLotto) {


}
