package uk.jinhy.server.service.disease.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uk.jinhy.server.api.disease.application.DiseaseImageService;
import uk.jinhy.server.api.disease.domain.exception.TaskNotFoundException;
import uk.jinhy.server.api.disease.presentation.DiseaseController;
import uk.jinhy.server.api.disease.presentation.dto.request.DiseaseImageRequestDto;
import uk.jinhy.server.api.disease.presentation.dto.response.DiseaseImageResponseDto;
import uk.jinhy.server.api.disease.presentation.dto.response.DiseaseImageResultResponseDto;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class DiseaseControllerImpl implements DiseaseController {

    private final DiseaseImageService diseaseImageService;

    @Override
    public ResponseEntity<DiseaseImageResponseDto> analyzeImage(
            MultipartFile image) throws IOException {

        DiseaseImageRequestDto requestDto = new DiseaseImageRequestDto();
        requestDto.setImage(image);

        DiseaseImageResponseDto responseDto = diseaseImageService.requestImageAnalysis(requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @Override
    public ResponseEntity<DiseaseImageResultResponseDto> getImageAnalysisResult(String taskId) {
        try {
            DiseaseImageResultResponseDto resultDto = diseaseImageService.getImageAnalysisResult(taskId);
            return ResponseEntity.ok(resultDto);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
