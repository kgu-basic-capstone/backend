package uk.jinhy.server.service.hospital.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.jinhy.server.api.domain.Pet;
import uk.jinhy.server.api.hospital.presentation.HospitalDto.HospitalReservationRequest;
import uk.jinhy.server.api.hospital.presentation.HospitalDto.HospitalDetailResponse;
import uk.jinhy.server.api.hospital.presentation.HospitalDto.HospitalListResponse;
import uk.jinhy.server.api.hospital.presentation.HospitalDto.HospitalReservationListResponse;
import uk.jinhy.server.api.hospital.presentation.HospitalDto.HospitalReservationResponse;
import uk.jinhy.server.api.hospital.application.HospitalService;
import uk.jinhy.server.service.hospital.domain.HospitalEntity;
import uk.jinhy.server.service.hospital.domain.HospitalRepository;
import uk.jinhy.server.service.hospital.domain.HospitalReservationEntity;
import uk.jinhy.server.service.hospital.domain.ReservationRepository;
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

    @Override
    public HospitalListResponse getHospitals(Double latitude, Double longitude, Double radius, Boolean surgeryAvailable, int page, int size) {
        Page<HospitalEntity> hospitals = hospitalRepository.findByFilters(
            surgeryAvailable, latitude, longitude, radius,
            PageRequest.of(page, size));

        List<HospitalDetailResponse> hospitalResponses = hospitals.getContent().stream()
            .map(this::mapToDetailResponse)
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
            .orElseThrow(() -> new NoSuchElementException("병원을 찾을 수 없습니다. ID: " + hospitalId));

        return mapToDetailResponse(hospital);
    }

    @Override
    @Transactional
    public HospitalReservationResponse createReservation(Long hospitalId, HospitalReservationRequest request, Long userId) {
        HospitalEntity hospital = hospitalRepository.findById(hospitalId)
            .orElseThrow(() -> new NoSuchElementException("병원을 찾을 수 없습니다. ID: " + hospitalId));

        String petName = petService.getPetNameById(request.getPetId());

        Pet pet = petRepository.findById(request.getPetId())
            .orElseThrow(() -> new NoSuchElementException("반려동물을 찾을 수 없습니다. ID: " + request.getPetId()));

        HospitalReservationEntity reservation = HospitalReservationEntity.builder()
            .pet(pet)
            .hospitalEntity(hospital)
            .reservationDateTime(request.getReservationDateTime())
            .status(HospitalReservationEntity.ReservationStatus.PENDING)
            .build();

        HospitalReservationEntity savedReservation = reservationRepository.save(reservation);

        return mapToReservationResponse(savedReservation);
    }

    @Override
    public HospitalReservationListResponse getUserReservations(Long userId, String status) {
        List<HospitalReservationEntity> reservations;

        if (status != null && !status.isEmpty()) {
            reservations = reservationRepository.findByUserIdAndStatusOrderByReservationDateTimeDesc(userId, status);
        } else {
            reservations = reservationRepository.findByUserIdOrderByReservationDateTimeDesc(userId);
        }

        List<HospitalReservationResponse> reservationResponses = reservations.stream()
            .map(this::mapToReservationResponse)
            .collect(Collectors.toList());

        return HospitalReservationListResponse.builder()
            .reservations(reservationResponses)
            .total(reservationResponses.size())
            .build();
    }

    @Override
    @Transactional
    public HospitalReservationResponse updateReservationStatus(Long reservationId) {
        HospitalReservationEntity reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new NoSuchElementException("예약을 찾을 수 없습니다. ID: " + reservationId));

        reservation.changeStatus(HospitalReservationEntity.ReservationStatus.CONFIRMED);
        HospitalReservationEntity updatedReservation = reservationRepository.save(reservation);

        return mapToReservationResponse(updatedReservation);
    }

    @Override
    @Transactional
    public void cancelReservation(Long reservationId) {
        HospitalReservationEntity reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new NoSuchElementException("예약을 찾을 수 없습니다. ID: " + reservationId));

        reservation.changeStatus(HospitalReservationEntity.ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }

    private HospitalDetailResponse mapToDetailResponse(HospitalEntity hospital) {
        return HospitalDetailResponse.builder()
            .id(hospital.getId())
            .name(hospital.getName())
            .address(hospital.getAddress())
            .phoneNumber(hospital.getPhoneNumber())
            .rating(hospital.getRating())
            .surgeryAvailable(hospital.isSurgeryAvailable())
            .operatingHours(hospital.getOperatingHours())
            .latitude(hospital.getLatitude())
            .longitude(hospital.getLongitude())
            .build();
    }

    private HospitalReservationResponse mapToReservationResponse(HospitalReservationEntity reservation) {
        return HospitalReservationResponse.builder()
            .id(reservation.getId())
            .petId(reservation.getPet().getId())
            .petName(reservation.getPet().getName())
            .hospitalId(reservation.getHospitalEntity().getId())
            .hospitalName(reservation.getHospitalEntity().getName())
            .reservationDateTime(reservation.getReservationDateTime())
            .status(reservation.getStatus().name())
            .createdAt(reservation.getCreatedAt())
            .build();
    }
}
