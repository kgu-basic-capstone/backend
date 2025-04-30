package uk.jinhy.server.service.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.jinhy.server.service.pet.domain.PetEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "health_records")
public class HealthRecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private PetEntity pet;

    @Column(nullable = false)
    private LocalDateTime checkDate;

    @Column(nullable = false)
    private Double weight;

    @Column(length = 1000)
    private String notes;

    @Builder
    public HealthRecordEntity(PetEntity pet, LocalDateTime checkDate, Double weight, String notes) {
        this.pet = pet;
        this.checkDate = checkDate;
        this.weight = weight;
        this.notes = notes;
    }

    public void setPet(PetEntity pet) {
        this.pet = pet;
    }
}
