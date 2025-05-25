package uk.jinhy.server.service.pet.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.jinhy.server.api.pet.domain.HealthRecordCategories;

import java.time.LocalDate;
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
    private LocalDate checkDate;

    @Column(nullable = false)
    private Double weight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private HealthRecordCategories category;

    @Column(length = 1000)
    private String notes;

    @Builder
    public HealthRecordEntity(PetEntity pet, LocalDate checkDate, Double weight, HealthRecordCategories category, String notes) {
        this.pet = pet;
        this.checkDate = checkDate;
        this.weight = weight;
        this.category = category;
        this.notes = notes;
    }
}
