package uk.jinhy.server.api.disease.application;

import uk.jinhy.server.api.disease.presentation.dto.request.DiseaseImageRequestDto;
import uk.jinhy.server.api.disease.presentation.dto.response.DiseaseImageResponseDto;
import uk.jinhy.server.api.disease.presentation.dto.response.DiseaseImageResultResponseDto;

import java.io.IOException;

public interface DiseaseImageService {
    DiseaseImageResponseDto requestImageAnalysis(DiseaseImageRequestDto requestDto) throws IOException;

    DiseaseImageResultResponseDto getImageAnalysisResult(String taskId);
} 