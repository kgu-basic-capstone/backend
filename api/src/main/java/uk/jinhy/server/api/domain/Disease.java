package uk.jinhy.server.api.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class Disease {
    private Long id;
    private String name;
    private String description;
    private List<String> possibleCauses;
    private List<String> recommendedTreatments;
    private List<String> recommendedDiet;
    private List<String> recommendedCare;

    @Builder
    private Disease(String name, String description,
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
