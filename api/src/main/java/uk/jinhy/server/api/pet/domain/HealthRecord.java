package uk.jinhy.server.api.pet.domain;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class HealthRecord {

    private Long id;
    private Pet pet;
    private LocalDateTime checkDate;
    private Double weight;
    private String notes;

    @Builder
    private HealthRecord(Pet pet, LocalDateTime checkDate, Double weight, String notes) {
        this.pet = pet;
        this.checkDate = checkDate;
        this.weight = weight;
        this.notes = notes;
    }
}
