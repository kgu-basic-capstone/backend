package uk.jinhy.server.api.pet.presentation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    }
}
