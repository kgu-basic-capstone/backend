package uk.jinhy.server.api.pet.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.jinhy.server.api.pet.presentation.PetDto.HealthRecordRequest;
import uk.jinhy.server.api.pet.presentation.PetDto.HealthRecordResponse;
import uk.jinhy.server.api.pet.presentation.PetDto.PetCreateRequest;
import uk.jinhy.server.api.pet.presentation.PetDto.PetDetailResponse;
import uk.jinhy.server.api.pet.presentation.PetDto.PetListResponse;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Pet", description = "반려동물 관리 API")
public interface PetController {

    @Operation(
        summary = "반려동물 등록",
        description = "새로운 반려동물을 등록합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "반려동물 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
        }
    )
    @PostMapping("/api/pets")
    ResponseEntity<PetDetailResponse> createPet(
        @RequestBody PetCreateRequest request
    );

    @Operation(
        summary = "반려동물 목록 조회",
        description = "사용자에게 등록된 모든 반려동물 목록을 조회합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "반려동물 목록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
        }
    )
    @GetMapping("/api/pets")
    ResponseEntity<PetListResponse> getPets();

    @Operation(
        summary = "반려동물 상세 조회",
        description = "지정된 ID의 반려동물 상세 정보를 조회합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "반려동물 조회 성공"),
            @ApiResponse(responseCode = "404", description = "반려동물을 찾을 수 없음"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
        }
    )
    @GetMapping("/api/pets/{petId}")
    ResponseEntity<PetDetailResponse> getPet(
        @Parameter(description = "반려동물 ID") @PathVariable Long petId
    );

    @Operation(
        summary = "반려동물 삭제",
        description = "지정된 ID의 반려동물을 삭제합니다.",
        responses = {
            @ApiResponse(responseCode = "204", description = "반려동물 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "반려동물을 찾을 수 없음"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
        }
    )
    @DeleteMapping("/api/pets/{petId}")
    ResponseEntity<Void> deletePet(
        @Parameter(description = "반려동물 ID") @PathVariable Long petId
    );

    @Operation(
        summary = "건강기록 추가",
        description = "반려동물의 건강 기록을 추가합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "건강기록 추가 성공"),
            @ApiResponse(responseCode = "404", description = "반려동물을 찾을 수 없음"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
        }
    )
    @PostMapping("/api/pets/{petId}/health-records")
    ResponseEntity<HealthRecordResponse> addHealthRecord(
        @Parameter(description = "반려동물 ID") @PathVariable Long petId,
        @RequestBody HealthRecordRequest request
    );

    @Operation(
        summary = "건강기록 조회",
        description = "반려동물의 건강 기록을 조회합니다. 날짜 필터링이 가능합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "건강기록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "반려동물을 찾을 수 없음"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
        }
    )
    @GetMapping("/api/pets/{petId}/health-records")
    ResponseEntity<List<HealthRecordResponse>> getHealthRecords(
        @Parameter(description = "반려동물 ID") @PathVariable Long petId,
        @Parameter(description = "특정 날짜 이후 기록만 조회") @RequestParam(required = false) LocalDate since
    );

    @Operation(
        summary = "건강기록 삭제",
        description = "반려동물의 특정 건강 기록을 삭제합니다.",
        responses = {
            @ApiResponse(responseCode = "204", description = "건강기록 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "건강기록을 찾을 수 없음"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
        }
    )
    @DeleteMapping("/api/pets/{petId}/health-records/{recordId}")
    ResponseEntity<Void> deleteHealthRecord(
        @Parameter(description = "반려동물 ID") @PathVariable Long petId,
        @Parameter(description = "건강기록 ID") @PathVariable Long recordId
    );
}
