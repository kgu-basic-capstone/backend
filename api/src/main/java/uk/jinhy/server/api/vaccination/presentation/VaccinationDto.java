package uk.jinhy.server.api.vaccination.presentation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.jinhy.server.api.domain.Vaccination;

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

    // Entity → DTO 변환 메서드
    public static VaccinationResponse fromEntity(Vaccination entity) {
        return VaccinationResponse.builder()
            .id(entity.getId())  // 백신 기록 ID
            .petId(entity.getPet().getId())  // 반려동물 ID
            .vaccineName(entity.getVaccineName())  // 백신 이름
            .vaccinationDate(entity.getVaccinationDate())  // 접종 날짜
            .nextVaccinationDate(entity.getNextVaccinationDate())  // 다음 접종 날짜
            .isCompleted(entity.getIsCompleted())  // 접종 완료 여부
            .build();
    }
}
