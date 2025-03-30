package uk.jinhy.server.api.disease.presentation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class DiseaseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiseaseAnalysisRequest {
        private Long petId;
        private List<String> symptoms;
        private String imageUrl;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiseaseDetailResponse {
        private Long id;
        private String name;
        private String description;
        private List<String> possibleCauses;
        private List<String> recommendedTreatments;
        private List<String> recommendedDiet;
        private List<String> recommendedCare;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiseaseListResponse {
        private List<DiseaseDetailResponse> diseases;
        private int total;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiseaseAnalysisResponse {
        private Long petId;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime analyzedAt;
        private List<String> symptoms;
        private List<DiseasePrediction> predictions;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class DiseasePrediction {
            private Long diseaseId;
            private String diseaseName;
            private Double confidence;
        }
    }
}
