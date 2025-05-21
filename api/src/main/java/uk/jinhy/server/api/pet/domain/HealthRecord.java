package uk.jinhy.server.api.pet.domain;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class HealthRecord {

    private Long id;
    private Pet pet;
    private LocalDate checkDate;
    private Double weight;
    private HealthRecordCategories category;
    private String notes;

    @Builder
    private HealthRecord(Pet pet, LocalDate checkDate, Double weight, HealthRecordCategories category, String notes) {
        this.pet = pet;
        this.checkDate = checkDate;
        this.weight = weight;
        this.category = category;
        this.notes = notes;
    }
}
