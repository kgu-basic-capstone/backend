package uk.jinhy.server.api.vaccination.presentation;

import lombok.*;

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
        private VaccinationStatusType statusType;
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
        private VaccinationStatusType statusType;
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
