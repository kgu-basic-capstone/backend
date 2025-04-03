package uk.jinhy.server.api.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Pet {

    private Long id;
    private User owner;
    private String name;
    private LocalDate birthDate;
    private List<HealthRecord> healthRecords;

    @Builder
    private Pet(User owner, String name, LocalDate birthDate) {
        this.owner = owner;
        this.name = name;
        this.birthDate = birthDate;
        this.healthRecords = new ArrayList<>();
    }

    public HealthRecord saveHealthRecord(HealthRecord healthRecord) {
        this.healthRecords.add(healthRecord);
        return healthRecord;
    }

    public HealthRecord deleteHealthRecord(HealthRecord healthRecord) {
        this.healthRecords.remove(healthRecord);
        return healthRecord;
    }

    public HealthRecord updateHealthRecord(HealthRecord originalRecord, HealthRecord updatedRecord) {
        this.healthRecords.remove(originalRecord);
        this.healthRecords.add(updatedRecord);
        return updatedRecord;
    }

    public List<HealthRecord> getHealthRecordsAfter(LocalDateTime dateTime) {
        return this.healthRecords.stream()
            .filter(record -> record.getCheckDate().isAfter(dateTime))
            .collect(Collectors.toList());
    }

}
