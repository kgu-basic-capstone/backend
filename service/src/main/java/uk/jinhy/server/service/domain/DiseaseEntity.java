package uk.jinhy.server.service.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "diseases")
public class DiseaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @ElementCollection
    @CollectionTable(name = "disease_possible_causes", joinColumns = @JoinColumn(name = "disease_id"))
    @Column(name = "cause")
    private List<String> possibleCauses;

    @ElementCollection
    @CollectionTable(name = "disease_recommended_treatments", joinColumns = @JoinColumn(name = "disease_id"))
    @Column(name = "treatment")
    private List<String> recommendedTreatments;

    @ElementCollection
    @CollectionTable(name = "disease_recommended_diet", joinColumns = @JoinColumn(name = "disease_id"))
    @Column(name = "diet")
    private List<String> recommendedDiet;

    @ElementCollection
    @CollectionTable(name = "disease_recommended_care", joinColumns = @JoinColumn(name = "disease_id"))
    @Column(name = "care")
    private List<String> recommendedCare;

    @Builder
    public DiseaseEntity(String name, String description,
                         List<String> possibleCauses,
                         List<String> recommendedTreatments,
                         List<String> recommendedDiet,
                         List<String> recommendedCare) {
        this.name = name;
        this.description = description;
        this.possibleCauses = possibleCauses;
        this.recommendedTreatments = recommendedTreatments;
        this.recommendedDiet = recommendedDiet;
        this.recommendedCare = recommendedCare;
    }
}
