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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class HospitalControllerImpl implements HospitalController {

    @Override
    public ResponseEntity<HospitalListResponse> getHospitals(Double latitude, Double longitude,
                                                             Double radius, Boolean surgeryAvailable,
                                                             int page, int size) {
        List<HospitalDetailResponse> mockHospitals = Arrays.asList(
            HospitalDetailResponse.builder()
                .id(1L)
                .name("24시 동물의료센터")
                .address("서울시 강남구 테헤란로 123")
                .phoneNumber("02-123-4567")
                .rating(4.8)
                .surgeryAvailable(true)
                .operatingHours("00:00-24:00")
                .latitude(37.5023)
                .longitude(127.0403)
                .build(),
            HospitalDetailResponse.builder()
                .id(2L)
                .name("우리동네 동물병원")
                .address("서울시 서초구 서초대로 456")
                .phoneNumber("02-456-7890")
                .rating(4.5)
                .surgeryAvailable(false)
                .operatingHours("09:00-18:00")
                .latitude(37.4923)
                .longitude(127.0292)
                .build(),
            HospitalDetailResponse.builder()
                .id(3L)
                .name("펫 메디컬 센터")
                .address("서울시 강남구 도산대로 789")
                .phoneNumber("02-789-0123")
                .rating(4.9)
                .surgeryAvailable(true)
                .operatingHours("09:00-21:00")
                .latitude(37.5133)
                .longitude(127.0523)
                .build()
        );

        if (surgeryAvailable != null) {
            mockHospitals = mockHospitals.stream()
                .filter(hospital -> hospital.isSurgeryAvailable() == surgeryAvailable)
                .collect(Collectors.toList());
        }

        if (latitude != null && longitude != null) {
            mockHospitals = mockHospitals.stream()
                .filter(hospital -> calculateDistance(latitude, longitude,
                    hospital.getLatitude(), hospital.getLongitude()) <= radius)
                .collect(Collectors.toList());
        }

        HospitalListResponse response = HospitalListResponse.builder()
            .hospitals(mockHospitals)
            .total(mockHospitals.size())
            .page(page)
            .size(size)
            .build();

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<HospitalDetailResponse> getHospital(Long hospitalId) {
        HospitalDetailResponse mockResponse = HospitalDetailResponse.builder()
            .id(hospitalId)
            .name("24시 동물의료센터")
            .address("서울시 강남구 테헤란로 123")
            .phoneNumber("02-123-4567")
            .rating(4.8)
            .surgeryAvailable(true)
            .operatingHours("00:00-24:00")
            .latitude(37.5023)
            .longitude(127.0403)
            .build();

        return ResponseEntity.ok(mockResponse);
    }

    @Override
    public ResponseEntity<HospitalReservationResponse> createReservation(Long hospitalId,
                                                                         HospitalReservationRequest request) {
        HospitalReservationResponse mockResponse = HospitalReservationResponse.builder()
            .id(1L)
            .petId(request.getPetId())
            .petName("멍멍이")
            .hospitalId(hospitalId)
            .hospitalName("24시 동물의료센터")
            .reservationDateTime(request.getReservationDateTime())
            .status("PENDING")
            .createdAt(LocalDateTime.now())
            .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(mockResponse);
    }

    @Override
    public ResponseEntity<HospitalReservationListResponse> getUserReservations(String status) {
        List<HospitalReservationResponse> mockReservations = Arrays.asList(
            HospitalReservationResponse.builder()
                .id(1L)
                .petId(1L)
                .petName("멍멍이")
                .hospitalId(1L)
                .hospitalName("24시 동물의료센터")
                .reservationDateTime(LocalDateTime.now().plusDays(1))
                .status("CONFIRMED")
                .createdAt(LocalDateTime.now().minusDays(1))
                .build(),
            HospitalReservationResponse.builder()
                .id(2L)
                .petId(2L)
                .petName("야옹이")
                .hospitalId(2L)
                .hospitalName("우리동네 동물병원")
                .reservationDateTime(LocalDateTime.now().plusDays(3))
                .status("PENDING")
                .createdAt(LocalDateTime.now().minusHours(12))
                .build(),
            HospitalReservationResponse.builder()
                .id(3L)
                .petId(1L)
                .petName("멍멍이")
                .hospitalId(3L)
                .hospitalName("펫 메디컬 센터")
                .reservationDateTime(LocalDateTime.now().minusDays(5))
                .status("CANCELLED")
                .createdAt(LocalDateTime.now().minusDays(7))
                .build()
        );

        if (status != null && !status.isEmpty()) {
            mockReservations = mockReservations.stream()
                .filter(reservation -> reservation.getStatus().equals(status))
                .collect(Collectors.toList());
        }

        HospitalReservationListResponse response = HospitalReservationListResponse.builder()
            .reservations(mockReservations)
            .total(mockReservations.size())
            .build();

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<HospitalReservationResponse> updateReservationStatus(Long reservationId, String status) {
        HospitalReservationResponse mockResponse = HospitalReservationResponse.builder()
            .id(reservationId)
            .petId(1L)
            .petName("멍멍이")
            .hospitalId(1L)
            .hospitalName("24시 동물의료센터")
            .reservationDateTime(LocalDateTime.now().plusDays(1))
            .status(status)
            .createdAt(LocalDateTime.now().minusDays(1))
            .build();

        return ResponseEntity.ok(mockResponse);
    }

    @Override
    public ResponseEntity<Void> cancelReservation(Long reservationId) {
        return ResponseEntity.noContent().build();
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371.0;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
