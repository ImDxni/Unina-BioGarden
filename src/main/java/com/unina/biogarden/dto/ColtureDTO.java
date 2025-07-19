package com.unina.biogarden.dto;

import com.unina.biogarden.enumerations.ColtureStatus;

import java.time.LocalDate;

public record ColtureDTO(
        int id,
        LocalDate startDate,
        ColtureStatus status,
        int projectId,
        int cropId,
        String cropName){
}
