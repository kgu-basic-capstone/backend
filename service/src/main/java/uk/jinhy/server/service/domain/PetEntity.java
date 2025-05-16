package uk.jinhy.server.service.domain;

import jakarta.persistence.*;
import lombok.*;
import uk.jinhy.server.service.user.domain.UserEntity;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@AllArgsConstructor
@Builder
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

    // 백신 기록 (새로 추가)
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VaccinationEntity> vaccinations = new ArrayList<>();

    @Builder
    public PetEntity(UserEntity owner, String name, LocalDate birthDate) {
        this.owner = owner;
        this.name = name;
        this.birthDate = birthDate;
    }
    //  백신 기록 추가
    public VaccinationEntity saveVaccination(VaccinationEntity vaccination) {
        vaccination.setPet(this);  // 관계 설정
        this.vaccinations.add(vaccination);
        return vaccination;
    }

    // 백신 기록 삭제
    public void deleteVaccination(VaccinationEntity vaccination) {
        this.vaccinations.remove(vaccination);
        vaccination.setPet(null);
    }

    // 백신 기록 조회 (특정 날짜 이후)
    public List<VaccinationEntity> getVaccinationsAfter(LocalDateTime dateTime) {
        return this.vaccinations.stream()
            .filter(record -> record.getVaccinationDate().isAfter(dateTime.toLocalDate()))
            .collect(Collectors.toList());
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
