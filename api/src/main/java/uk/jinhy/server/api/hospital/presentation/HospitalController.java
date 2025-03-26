package uk.jinhy.server.api.hospital.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.jinhy.server.api.hospital.presentation.HospitalDto.HospitalReservationRequest;
import uk.jinhy.server.api.hospital.presentation.HospitalDto.HospitalDetailResponse;
import uk.jinhy.server.api.hospital.presentation.HospitalDto.HospitalListResponse;
import uk.jinhy.server.api.hospital.presentation.HospitalDto.HospitalReservationResponse;
import uk.jinhy.server.api.hospital.presentation.HospitalDto.HospitalReservationListResponse;

@Tag(name = "Hospital", description = "동물병원 및 예약 관리 API")
public interface HospitalController {

    @Operation(
        summary = "동물병원 목록 조회",
        description = "동물병원 목록을 조회합니다. 위치 기반 필터링 가능합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "동물병원 목록 조회 성공")
        }
    )
    @GetMapping("/api/hospitals")
    ResponseEntity<HospitalListResponse> getHospitals(
        @Parameter(description = "위도") @RequestParam(required = false) Double latitude,
        @Parameter(description = "경도") @RequestParam(required = false) Double longitude,
        @Parameter(description = "검색 반경(km)") @RequestParam(required = false, defaultValue = "5") Double radius,
        @Parameter(description = "수술 가능 여부로 필터링") @RequestParam(required = false) Boolean surgeryAvailable,
        @Parameter(description = "페이지 번호") @RequestParam(required = false, defaultValue = "0") int page,
        @Parameter(description = "페이지 크기") @RequestParam(required = false, defaultValue = "20") int size
    );

    @Operation(
        summary = "동물병원 상세 조회",
        description = "특정 동물병원의 상세 정보를 조회합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "동물병원 조회 성공"),
            @ApiResponse(responseCode = "404", description = "동물병원을 찾을 수 없음")
        }
    )
    @GetMapping("/api/hospitals/{hospitalId}")
    ResponseEntity<HospitalDetailResponse> getHospital(
        @Parameter(description = "동물병원 ID") @PathVariable Long hospitalId
    );

    @Operation(
        summary = "동물병원 예약 생성",
        description = "동물병원 방문 예약을 생성합니다.",
        responses = {
            @ApiResponse(responseCode = "201", description = "예약 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "동물병원 또는 반려동물을 찾을 수 없음")
        }
    )
    @PostMapping("/api/hospitals/{hospitalId}/reservations")
    ResponseEntity<HospitalReservationResponse> createReservation(
        @Parameter(description = "동물병원 ID") @PathVariable Long hospitalId,
        @RequestBody HospitalReservationRequest request
    );

    @Operation(
        summary = "사용자 예약 목록 조회",
        description = "현재 로그인한 사용자의 모든 병원 예약 목록을 조회합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "예약 목록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
        }
    )
    @GetMapping("/api/reservations")
    ResponseEntity<HospitalReservationListResponse> getUserReservations(
        @Parameter(description = "예약 상태로 필터링") @RequestParam(required = false) String status
    );

    @Operation(
        summary = "예약 상태 변경",
        description = "특정 예약의 상태를 변경합니다 (확인, 취소 등).",
        responses = {
            @ApiResponse(responseCode = "200", description = "예약 상태 변경 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "예약을 찾을 수 없음")
        }
    )
    @PatchMapping("/api/reservations/{reservationId}/status")
    ResponseEntity<HospitalReservationResponse> updateReservationStatus(
        @Parameter(description = "예약 ID") @PathVariable Long reservationId,
        @Parameter(description = "변경할 상태 (PENDING, CONFIRMED, CANCELLED)") @RequestParam String status
    );

    @Operation(
        summary = "예약 취소",
        description = "특정 예약을 취소합니다.",
        responses = {
            @ApiResponse(responseCode = "204", description = "예약 취소 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "예약을 찾을 수 없음")
        }
    )
    @DeleteMapping("/api/reservations/{reservationId}")
    ResponseEntity<Void> cancelReservation(
        @Parameter(description = "예약 ID") @PathVariable Long reservationId
    );
}
