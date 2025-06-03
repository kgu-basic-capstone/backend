package uk.jinhy.server.service.hospital.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uk.jinhy.server.api.hospital.presentation.HospitalController;
import uk.jinhy.server.api.hospital.presentation.HospitalDto.HospitalReservationRequest;
import uk.jinhy.server.api.hospital.presentation.HospitalDto.HospitalDetailResponse;
import uk.jinhy.server.api.hospital.presentation.HospitalDto.HospitalListResponse;
import uk.jinhy.server.api.hospital.presentation.HospitalDto.HospitalReservationResponse;
import uk.jinhy.server.api.hospital.presentation.HospitalDto.HospitalReservationListResponse;
import uk.jinhy.server.api.hospital.application.HospitalService;

    @RestController
    @RequiredArgsConstructor
    public class HospitalControllerImpl implements HospitalController {

        private final HospitalService hospitalService;

        @Override
        public ResponseEntity<HospitalListResponse> getHospitals(Double latitude, Double longitude,
                                                                 Double radius, Boolean surgeryAvailable,
                                                                 int page, int size) {
            HospitalListResponse response = hospitalService.getHospitals(
                latitude, longitude, radius, surgeryAvailable, page, size);
            return ResponseEntity.ok(response);
        }

        @Override
        public ResponseEntity<HospitalDetailResponse> getHospital(Long hospitalId) {
            HospitalDetailResponse response = hospitalService.getHospital(hospitalId);
            return ResponseEntity.ok(response);
        }

        @Override
        public ResponseEntity<HospitalReservationResponse> createReservation(Long hospitalId,
                                                                             HospitalReservationRequest request) {
            // 임시 구현
            Long userId = getCurrentUserId();
            HospitalReservationResponse response = hospitalService.createReservation(
                hospitalId, request, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        @Override
        public ResponseEntity<HospitalReservationListResponse> getUserReservations(String status) {
            // 임시 구현
            Long userId = getCurrentUserId();

            HospitalReservationListResponse response = hospitalService.getUserReservations(userId, status);
            return ResponseEntity.ok(response);
        }

        @Override
        public ResponseEntity<HospitalReservationResponse> updateReservationStatus(Long reservationId, String status) {
            HospitalReservationResponse response = hospitalService.updateReservationStatus(reservationId, status);
            return ResponseEntity.ok(response);
        }

        @Override
        public ResponseEntity<Void> cancelReservation(Long reservationId) {
            hospitalService.cancelReservation(reservationId);
            return ResponseEntity.noContent().build();
        }

        private Long getCurrentUserId() {
            // 임시 구현
            // 실제 유저 인증 구현 필요
            return 1L;
        }

    }
