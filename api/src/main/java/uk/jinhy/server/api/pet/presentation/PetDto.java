package uk.jinhy.server.api.pet.presentation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.jinhy.server.api.pet.domain.HealthRecord;
import uk.jinhy.server.api.pet.domain.Pet;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PetDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PetCreateRequest {
        private String name;
        private LocalDate birthDate;
        private Long UserId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PetDetailResponse {
        private Long id;
        private String name;
        private LocalDate birthDate;
        private OwnerInfo owner;
        private List<HealthRecordResponse> healthRecords;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class OwnerInfo {
            private Long id;
            private String username;
            private String email;
        }

        public static PetDetailResponse from(Pet pet) {
            return PetDetailResponse.builder()
                .id(pet.getId())
                .name(pet.getName())
                .birthDate(pet.getBirthDate())
                .owner(
                    OwnerInfo.builder()
                        .id(pet.getOwner().getId())
                        .username(pet.getOwner().getUsername())
                        .email(null) // 임시로 null
                        .build()
                )
                .healthRecords(List.of()) // 나중에 healthRecords 매핑 추가 예정
                .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PetListResponse {
        private List<PetDetailResponse> pets;
        private int total;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HealthRecordRequest {
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime checkDate;
        private Double weight;
        private String notes;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HealthRecordResponse {
        private Long id;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime checkDate;
        private Double weight;
        private String notes;

        public static HealthRecordResponse from(HealthRecord healthRecord) {
            HealthRecordResponse response = new HealthRecordResponse();
            response.checkDate = healthRecord.getCheckDate();
            response.weight = healthRecord.getWeight();
            response.notes = healthRecord.getNotes();
            return response;
        }

    }
}
