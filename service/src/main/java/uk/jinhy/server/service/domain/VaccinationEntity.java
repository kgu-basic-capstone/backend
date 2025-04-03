package uk.jinhy.server.service.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "vaccinations")
public class VaccinationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private PetEntity pet;

    @Column(nullable = false)
    private String vaccineName;

    @Column(nullable = false)
    private LocalDate vaccinationDate;

    private LocalDate nextVaccinationDate;

    @Column(nullable = false)
    private Boolean isCompleted;

    @Builder
    public VaccinationEntity(PetEntity pet, String vaccineName,
                             LocalDate vaccinationDate,
                             LocalDate nextVaccinationDate,
                             Boolean isCompleted) {
        this.pet = pet;
        this.vaccineName = vaccineName;
        this.vaccinationDate = vaccinationDate;
        this.nextVaccinationDate = nextVaccinationDate;
        this.isCompleted = isCompleted;
    }
}
