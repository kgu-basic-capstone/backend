package uk.jinhy.server.api.hospital.presentation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class HospitalDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HospitalDetailResponse {
        private Long id;
        private String name;
        private String address;
        private String phoneNumber;
        private Double rating;
        private boolean surgeryAvailable;
        private String operatingHours;
        private Double latitude;
        private Double longitude;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HospitalListResponse {
        private List<HospitalDetailResponse> hospitals;
        private int total;
        private int page;
        private int size;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HospitalReservationRequest {
        private Long petId;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime reservationDateTime;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HospitalReservationResponse {
        private Long id;
        private Long petId;
        private String petName;
        private Long hospitalId;
        private String hospitalName;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime reservationDateTime;
        private String status;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HospitalReservationListResponse {
        private List<HospitalReservationResponse> reservations;
        private int total;
    }
}
