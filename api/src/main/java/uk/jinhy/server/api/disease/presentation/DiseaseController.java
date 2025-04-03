package uk.jinhy.server.api.disease.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.jinhy.server.api.disease.presentation.DiseaseDto.DiseaseAnalysisRequest;
import uk.jinhy.server.api.disease.presentation.DiseaseDto.DiseaseDetailResponse;
import uk.jinhy.server.api.disease.presentation.DiseaseDto.DiseaseListResponse;
import uk.jinhy.server.api.disease.presentation.DiseaseDto.DiseaseAnalysisResponse;

@Tag(name = "Disease", description = "반려동물 질병 관리 API")
public interface DiseaseController {

    @Operation(
        summary = "질병 목록 조회",
        description = "등록된 질병 목록을 조회합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "질병 목록 조회 성공")
        }
    )
    @GetMapping("/api/diseases")
    ResponseEntity<DiseaseListResponse> getDiseases(
        @Parameter(description = "검색 키워드") @RequestParam(required = false) String keyword
    );

    @Operation(
        summary = "질병 상세 조회",
        description = "특정 질병의 상세 정보를 조회합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "질병 조회 성공"),
            @ApiResponse(responseCode = "404", description = "질병을 찾을 수 없음")
        }
    )
    @GetMapping("/api/diseases/{diseaseId}")
    ResponseEntity<DiseaseDetailResponse> getDisease(
        @Parameter(description = "질병 ID") @PathVariable Long diseaseId
    );

    @Operation(
        summary = "AI 기반 질병 분석",
        description = "반려동물의 증상을 분석하여 가능성 있는 질병을 예측합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "분석 성공")
        }
    )
    @PostMapping("/api/diseases/analyze")
    ResponseEntity<DiseaseAnalysisResponse> analyzeDisease(
        @RequestBody DiseaseAnalysisRequest request
    );
}
