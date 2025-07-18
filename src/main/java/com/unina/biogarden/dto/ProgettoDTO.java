package com.unina.biogarden.dto;

import java.time.LocalDate;

public record ProgettoDTO(String nome, LocalDate dataInizio, LocalDate dataFine, String stato, String azioni) {


}
