package uk.jinhy.server.service.vaccination.presentation.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.jinhy.server.api.vaccination.presentation.VaccinationStatusType;
import uk.jinhy.server.service.pet.domain.PetEntity;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "vaccinations")
public class VaccinationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private PetEntity pet;

    @Column(nullable = false)
    private String vaccineName;

    @Column(nullable = false)
    private LocalDate vaccinationDate;

    private LocalDate nextVaccinationDate;

    @Column(name = "is_completed", nullable = false)
    private boolean isCompleted;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_type")
    private VaccinationStatusType vaccinationStatus;

    @Builder
    public VaccinationEntity(Long id, PetEntity pet, String vaccineName,
                             LocalDate vaccinationDate,
                             LocalDate nextVaccinationDate,
                             Boolean isCompleted,
                             VaccinationStatusType statusType) {
        this.id = id;
        this.pet = pet;
        this.vaccineName = vaccineName;
        this.vaccinationDate = vaccinationDate;
        this.nextVaccinationDate = nextVaccinationDate;
        this.isCompleted = (isCompleted != null) ? isCompleted : false;
        this.vaccinationStatus = statusType;
    }
    public void setStatusType(VaccinationStatusType statusType) {
        this.vaccinationStatus = statusType;
    }


    public String getVaccineName() {
        return vaccineName;
    }

    public LocalDate getVaccinationDate() {
        return vaccinationDate;
    }

    public LocalDate getNextVaccinationDate() {
        return nextVaccinationDate;
    }

    public void setIsCompleted(boolean completedStatus) {
        this.isCompleted = completedStatus;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public void setVaccinationDate(LocalDate vaccinationDate) {
        this.vaccinationDate = vaccinationDate;
    }

    public void setNextVaccinationDate(LocalDate nextVaccinationDate) {
        this.nextVaccinationDate = nextVaccinationDate;
    }


}
