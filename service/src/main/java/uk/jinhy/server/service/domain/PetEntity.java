package uk.jinhy.server.service.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "pets")
public class PetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate birthDate;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HealthRecordEntity> healthRecords = new ArrayList<>();

    @Builder
    public PetEntity(UserEntity owner, String name, LocalDate birthDate) {
        this.owner = owner;
        this.name = name;
        this.birthDate = birthDate;
    }

    public HealthRecordEntity saveHealthRecord(HealthRecordEntity healthRecord) {
        healthRecord.setPet(this);
        this.healthRecords.add(healthRecord);
        return healthRecord;
    }

    public HealthRecordEntity deleteHealthRecord(HealthRecordEntity healthRecord) {
        this.healthRecords.remove(healthRecord);
        return healthRecord;
    }

    public HealthRecordEntity updateHealthRecord(HealthRecordEntity originalRecord, HealthRecordEntity updatedRecord) {
        this.healthRecords.remove(originalRecord);
        updatedRecord.setPet(this);
        this.healthRecords.add(updatedRecord);
        return updatedRecord;
    }

    public List<HealthRecordEntity> getHealthRecordsAfter(LocalDateTime dateTime) {
        return this.healthRecords.stream()
            .filter(record -> record.getCheckDate().isAfter(dateTime))
            .collect(Collectors.toList());
    }
}
