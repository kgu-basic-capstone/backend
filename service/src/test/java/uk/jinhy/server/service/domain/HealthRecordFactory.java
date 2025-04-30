package uk.jinhy.server.service.domain;

import uk.jinhy.server.service.pet.domain.HealthRecordMapper;
import uk.jinhy.server.service.pet.domain.PetEntity;

import java.time.LocalDateTime;

public class HealthRecordFactory {
    private PetEntity pet;
    private LocalDateTime checkDate;
    private Double weight;
    private String notes;

    private HealthRecordFactory() {
        this.pet = PetFactory.create().build();
        this.checkDate = LocalDateTime.now();
        this.weight = 10.0;
        this.notes = "Default health record";
    }

    public static HealthRecordFactory create() {
        return new HealthRecordFactory();
    }

    public HealthRecordFactory pet(PetEntity pet) {
        this.pet = pet;
        return this;
    }

    public HealthRecordFactory checkDate(LocalDateTime checkDate) {
        this.checkDate = checkDate;
        return this;
    }

    public HealthRecordFactory weight(Double weight) {
        this.weight = weight;
        return this;
    }

    public HealthRecordFactory notes(String notes) {
        this.notes = notes;
        return this;
    }

    public HealthRecordMapper.HealthRecordEntity build() {
        return HealthRecordMapper.HealthRecordEntity.builder()
            .pet(this.pet)
            .checkDate(this.checkDate)
            .weight(this.weight)
            .notes(this.notes)
            .build();
    }
}
