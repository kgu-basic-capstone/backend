package uk.jinhy.server.service.disease.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uk.jinhy.server.api.disease.application.DiseaseImageService;
import uk.jinhy.server.api.disease.presentation.DiseaseController;
import uk.jinhy.server.api.disease.presentation.dto.request.DiseaseImageRequestDto;
import uk.jinhy.server.api.disease.presentation.dto.response.DiseaseImageResponseDto;
import uk.jinhy.server.api.disease.presentation.dto.response.DiseaseImageResultResponseDto;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DiseaseControllerImpl implements DiseaseController {

    private final DiseaseImageService diseaseImageService;

    @Override
    public ResponseEntity<DiseaseImageResponseDto> analyzeImage(
        MultipartFile image) throws IOException {
        log.info("질병 이미지 분석 요청 수신: fileName={}, size={}",
            image.getOriginalFilename(), image.getSize());

        DiseaseImageRequestDto requestDto = new DiseaseImageRequestDto();
        requestDto.setImage(image);

        DiseaseImageResponseDto responseDto = diseaseImageService.requestImageAnalysis(requestDto);
        log.info("질병 이미지 분석 요청 처리 완료: taskId={}", responseDto.getTaskId());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Override
    public ResponseEntity<DiseaseImageResultResponseDto> getImageAnalysisResult(String taskId) {
        log.info("질병 이미지 분석 결과 조회 요청: taskId={}", taskId);
        try {
            DiseaseImageResultResponseDto resultDto = diseaseImageService.getImageAnalysisResult(taskId);
            log.info("질병 이미지 분석 결과 조회 완료: taskId={}, status={}",
                taskId, resultDto.getStatus());
            return ResponseEntity.status(HttpStatus.OK).body(resultDto);
        } catch (TaskNotFoundException e) {
            log.warn("존재하지 않는 태스크 ID 조회: taskId={}", taskId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("질병 이미지 분석 결과 조회 중 오류 발생: taskId={}", taskId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
