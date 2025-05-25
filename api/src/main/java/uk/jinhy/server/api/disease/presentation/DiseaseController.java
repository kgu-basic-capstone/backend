package uk.jinhy.server.api.disease.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uk.jinhy.server.api.disease.presentation.dto.response.DiseaseImageResponseDto;
import uk.jinhy.server.api.disease.presentation.dto.response.DiseaseImageResultResponseDto;

import java.io.IOException;

@Tag(name = "Disease", description = "반려동물 질병 관리 API")
public interface DiseaseController {

    @Operation(
        summary = "이미지 기반 질병 분석 요청",
        description = "반려동물 사진을 분석하여 질병을 진단하는 작업을 요청합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "분석 요청 성공")
        }
    )
    @PostMapping(value = "/api/diseases", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<DiseaseImageResponseDto> analyzeImage(
            @RequestPart("image") MultipartFile image) throws IOException;

    @Operation(
        summary = "이미지 기반 질병 분석 결과 조회",
        description = "이미지 기반 질병 분석 작업의 결과를 조회합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "작업을 찾을 수 없음")
        }
    )
    @GetMapping("/api/diseases/{taskId}")
    ResponseEntity<DiseaseImageResultResponseDto> getImageAnalysisResult(
            @PathVariable String taskId);
}
