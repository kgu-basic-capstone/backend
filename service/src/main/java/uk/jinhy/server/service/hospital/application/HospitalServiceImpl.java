package uk.jinhy.server.service.hospital.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.jinhy.server.api.domain.Pet;
import uk.jinhy.server.api.hospital.domain.exception.HospitalNotFoundException;
import uk.jinhy.server.api.hospital.domain.exception.InvalidReservationStatusException;
import uk.jinhy.server.api.hospital.domain.exception.PetNotFoundException;
import uk.jinhy.server.api.hospital.domain.exception.ReservationNotFoundException;
import uk.jinhy.server.api.hospital.presentation.HospitalDto.HospitalReservationRequest;
import uk.jinhy.server.api.hospital.presentation.HospitalDto.HospitalDetailResponse;
import uk.jinhy.server.api.hospital.presentation.HospitalDto.HospitalListResponse;
import uk.jinhy.server.api.hospital.presentation.HospitalDto.HospitalReservationListResponse;
import uk.jinhy.server.api.hospital.presentation.HospitalDto.HospitalReservationResponse;
import uk.jinhy.server.api.hospital.application.HospitalService;
import uk.jinhy.server.service.hospital.domain.*;
import uk.jinhy.server.service.pet.PetService;
import uk.jinhy.server.service.pet.domain.PetRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HospitalServiceImpl implements HospitalService {

    private final HospitalRepository hospitalRepository;
    private final ReservationRepository reservationRepository;
    private final PetRepository petRepository;
    private final PetService petService;
    private final HospitalMapper hospitalMapper;

    @Override
    public HospitalListResponse getHospitals(Double latitude, Double longitude, Double radius, Boolean surgeryAvailable, int page, int size) {
        Page<HospitalEntity> hospitals = hospitalRepository.findByFilters(
            surgeryAvailable, latitude, longitude, radius,
            PageRequest.of(page, size));

        // Mapper 사용으로 변경
        List<HospitalDetailResponse> hospitalResponses = hospitals.getContent().stream()
            .map(hospitalMapper::toDetailResponse)
            .collect(Collectors.toList());

        return HospitalListResponse.builder()
            .hospitals(hospitalResponses)
            .total((int) hospitals.getTotalElements())
            .page(page)
            .size(size)
            .build();
    }

    @Override
    public HospitalDetailResponse getHospital(Long hospitalId) {
        HospitalEntity hospital = hospitalRepository.findById(hospitalId)
            .orElseThrow(() -> new HospitalNotFoundException("병원을 찾을 수 없습니다. ID: " + hospitalId));

        // Mapper 사용으로 변경
        return hospitalMapper.toDetailResponse(hospital);
    }

    @Override
    @Transactional
    public HospitalReservationResponse createReservation(Long hospitalId, HospitalReservationRequest request, Long userId) {
        HospitalEntity hospital = hospitalRepository.findById(hospitalId)
            .orElseThrow(() -> new HospitalNotFoundException("병원을 찾을 수 없습니다. ID: " + hospitalId));

        Pet pet = petRepository.findById(request.getPetId())
            .orElseThrow(() -> new PetNotFoundException("반려동물을 찾을 수 없습니다. ID: " + request.getPetId()));

        HospitalReservationEntity reservation = HospitalReservationEntity.builder()
            .pet(pet)
            .hospitalEntity(hospital)
            .reservationDateTime(request.getReservationDateTime())
            .status(HospitalReservationEntity.ReservationStatus.PENDING)
            .build();

        HospitalReservationEntity savedReservation = reservationRepository.save(reservation);

        // Mapper 사용으로 변경
        return hospitalMapper.toReservationResponse(savedReservation);
    }

    @Override
    public HospitalReservationListResponse getUserReservations(Long userId, String status) {
        List<HospitalReservationEntity> reservations;

        if (status != null && !status.isEmpty()) {
            reservations = reservationRepository.findByUserIdAndStatusOrderByReservationDateTimeDesc(userId, status);
        } else {
            reservations = reservationRepository.findByUserIdOrderByReservationDateTimeDesc(userId);
        }

        // Mapper 사용으로 변경
        List<HospitalReservationResponse> reservationResponses = reservations.stream()
            .map(hospitalMapper::toReservationResponse)
            .collect(Collectors.toList());

        return HospitalReservationListResponse.builder()
            .reservations(reservationResponses)
            .total(reservationResponses.size())
            .build();
    }

    @Override
    @Transactional
    public HospitalReservationResponse updateReservationStatus(Long reservationId, String status) {
        HospitalReservationEntity reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new ReservationNotFoundException("예약을 찾을 수 없습니다. ID: " + reservationId));

        HospitalReservationEntity.ReservationStatus reservationStatus;
        try {
            reservationStatus = HospitalReservationEntity.ReservationStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidReservationStatusException("유효하지 않은 예약 상태입니다: " + status +
                ". 가능한 상태: PENDING, CONFIRMED, CANCELLED");
        }

        reservation.changeStatus(reservationStatus);
        HospitalReservationEntity updatedReservation = reservationRepository.save(reservation);

        // Mapper 사용으로 변경
        return hospitalMapper.toReservationResponse(updatedReservation);
    }

    @Override
    @Transactional
    public void cancelReservation(Long reservationId) {
        HospitalReservationEntity reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new ReservationNotFoundException("예약을 찾을 수 없습니다. ID: " + reservationId));

        reservation.changeStatus(HospitalReservationEntity.ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }

}
