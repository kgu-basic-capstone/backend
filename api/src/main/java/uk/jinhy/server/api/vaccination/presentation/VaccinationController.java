package uk.jinhy.server.api.vaccination.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.jinhy.server.api.vaccination.presentation.VaccinationDto.VaccinationRequest;
import uk.jinhy.server.api.vaccination.presentation.VaccinationDto.VaccinationResponse;
import uk.jinhy.server.api.vaccination.presentation.VaccinationDto.VaccinationListResponse;

@Tag(name = "Vaccination", description = "백신 관리 API")
public interface VaccinationController {

    @Operation(
        summary = "백신 기록 추가",
        description = "반려동물의 백신 접종 기록을 추가합니다.",
        responses = {
            @ApiResponse(responseCode = "201", description = "백신 기록 추가 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "반려동물을 찾을 수 없음")
        }
    )
    @PostMapping("/api/pets/{petId}/vaccinations")
    ResponseEntity<VaccinationResponse> addVaccination(
        @Parameter(description = "반려동물 ID") @PathVariable Long petId,
        @RequestBody VaccinationRequest request
    );

    @Operation(
        summary = "백신 기록 조회",
        description = "반려동물의 모든 백신 접종 기록을 조회합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "백신 기록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "반려동물을 찾을 수 없음")
        }
    )
    @GetMapping("/api/pets/{petId}/vaccinations")
    ResponseEntity<VaccinationListResponse> getVaccinations(
        @Parameter(description = "반려동물 ID") @PathVariable Long petId,
        @Parameter(description = "완료된 백신만 조회") @RequestParam(required = false) Boolean completed,
        @Parameter(description = "예정된 백신만 조회") @RequestParam(required = false) Boolean upcoming
    );

    @Operation(
        summary = "백신 기록 삭제",
        description = "반려동물의 특정 백신 접종 기록을 삭제합니다.",
        responses = {
            @ApiResponse(responseCode = "204", description = "백신 기록 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "백신 기록을 찾을 수 없음")
        }
    )
    @DeleteMapping("/api/pets/{petId}/vaccinations/{vaccinationId}")
    ResponseEntity<Void> deleteVaccination(
        @Parameter(description = "반려동물 ID") @PathVariable Long petId,
        @Parameter(description = "백신 기록 ID") @PathVariable Long vaccinationId
    );

    @Operation(
        summary = "백신 완료 상태 변경",
        description = "백신 접종 완료 상태를 변경합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "백신 상태 변경 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "백신 기록을 찾을 수 없음")
        }
    )
    @PatchMapping("/api/pets/{petId}/vaccinations/{vaccinationId}/complete")
    ResponseEntity<VaccinationResponse> completeVaccination(
        @Parameter(description = "반려동물 ID") @PathVariable Long petId,
        @Parameter(description = "백신 기록 ID") @PathVariable Long vaccinationId,
        @Parameter(description = "완료 상태") @RequestParam boolean completed
    );

    @Operation(
        summary = "사용자의 모든 백신 기록 조회",
        description = "특정 사용자 ID에 해당하는 모든 반려동물의 백신 접종 기록을 조회합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "백신 기록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음 (또는 사용자는 있지만 백신 기록이 없는 경우 빈 리스트 반환)")
        }
    )
    @GetMapping("/api/users/{userId}/vaccinations") // 새로운 엔드포인트 경로
    ResponseEntity<VaccinationListResponse> getVaccinationsByUserId(
        @Parameter(description = "사용자 ID") @PathVariable Long userId,
        @Parameter(description = "완료된 백신만 조회") @RequestParam(required = false) Boolean completed,
        @Parameter(description = "예정된 백신만 조회 (오늘 이후)") @RequestParam(required = false) Boolean upcoming
    );
}
