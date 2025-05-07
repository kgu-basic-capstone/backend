package uk.jinhy.server.api.hospital.application;

import uk.jinhy.server.api.hospital.presentation.HospitalDto.HospitalReservationRequest;
import uk.jinhy.server.api.hospital.presentation.HospitalDto.HospitalDetailResponse;
import uk.jinhy.server.api.hospital.presentation.HospitalDto.HospitalListResponse;
import uk.jinhy.server.api.hospital.presentation.HospitalDto.HospitalReservationListResponse;
import uk.jinhy.server.api.hospital.presentation.HospitalDto.HospitalReservationResponse;

public interface HospitalService {

    HospitalListResponse getHospitals(Double latitude, Double longitude, Double radius, Boolean surgeryAvailable, int page, int size);

    HospitalDetailResponse getHospital(Long hospitalId);

    HospitalReservationResponse createReservation(Long hospitalId, HospitalReservationRequest request, Long userId);

    HospitalReservationListResponse getUserReservations(Long userId, String status);

    HospitalReservationResponse updateReservationStatus(Long reservationId);

    void cancelReservation(Long reservationId);
}
