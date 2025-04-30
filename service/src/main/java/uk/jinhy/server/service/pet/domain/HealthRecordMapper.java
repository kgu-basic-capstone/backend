package uk.jinhy.server.service.pet.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uk.jinhy.server.api.pet.domain.HealthRecord;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", uses = PetMapper.class)
public interface HealthRecordMapper {

    @Mapping(source = "pet", target = "pet")
    HealthRecordEntity toEntity(HealthRecord domain);

    @Mapping(source = "pet", target = "pet")
    HealthRecord toDomain(HealthRecordEntity entity);

    @Entity
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Table(name = "health_records")
    class HealthRecordEntity {
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
}
