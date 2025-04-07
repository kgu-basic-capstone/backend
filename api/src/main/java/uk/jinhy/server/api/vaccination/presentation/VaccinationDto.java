package uk.jinhy.server.api.vaccination.presentation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class VaccinationDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VaccinationRequest {
        private String vaccineName;
        private LocalDate vaccinationDate;
        private LocalDate nextVaccinationDate;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VaccinationResponse {
        private Long id;
        private Long petId;
        private String vaccineName;
        private LocalDate vaccinationDate;
        private LocalDate nextVaccinationDate;
        private boolean isCompleted;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VaccinationListResponse {
        private List<VaccinationResponse> vaccinations;
        private int total;
    }
}
