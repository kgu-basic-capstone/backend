package uk.jinhy.server.service.disease.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uk.jinhy.server.api.disease.presentation.DiseaseController;
import uk.jinhy.server.api.disease.presentation.DiseaseDto.DiseaseAnalysisRequest;
import uk.jinhy.server.api.disease.presentation.DiseaseDto.DiseaseDetailResponse;
import uk.jinhy.server.api.disease.presentation.DiseaseDto.DiseaseListResponse;
import uk.jinhy.server.api.disease.presentation.DiseaseDto.DiseaseAnalysisResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class DiseaseControllerImpl implements DiseaseController {

    @Override
    public ResponseEntity<DiseaseListResponse> getDiseases(String keyword) {
        List<DiseaseDetailResponse> mockDiseases = Arrays.asList(
            DiseaseDetailResponse.builder()
                .id(1L)
                .name("파보 바이러스")
                .description("개에게 발생하는 치명적인 바이러스성 질환입니다.")
                .possibleCauses(Arrays.asList("바이러스 감염", "면역력 약화", "어린 강아지"))
                .recommendedTreatments(Arrays.asList("입원 치료", "수액 요법", "항생제 투여"))
                .recommendedDiet(Arrays.asList("소화가 잘 되는 유동식", "영양가 높은 식이요법"))
                .recommendedCare(Arrays.asList("충분한 휴식", "정기적인 체크업"))
                .build(),
            DiseaseDetailResponse.builder()
                .id(2L)
                .name("고양이 범백혈구감소증")
                .description("고양이에게 발생하는 바이러스성 질환입니다.")
                .possibleCauses(Arrays.asList("바이러스 감염", "면역력 약화"))
                .recommendedTreatments(Arrays.asList("입원 치료", "수액 요법", "혈액 검사"))
                .recommendedDiet(Arrays.asList("영양가 높은 식이요법"))
                .recommendedCare(Arrays.asList("충분한 휴식", "정기적인 체크업"))
                .build(),
            DiseaseDetailResponse.builder()
                .id(3L)
                .name("피부 알레르기")
                .description("반려동물에게 흔히 발생하는 피부 질환입니다.")
                .possibleCauses(Arrays.asList("음식 알레르기", "환경적 요인", "기생충"))
                .recommendedTreatments(Arrays.asList("항히스타민제", "국소 치료제"))
                .recommendedDiet(Arrays.asList("저알레르기 음식", "특수 사료"))
                .recommendedCare(Arrays.asList("정기적인 목욕", "털 관리"))
                .build()
        );

        if (keyword != null && !keyword.isEmpty()) {
            mockDiseases = mockDiseases.stream()
                .filter(disease -> disease.getName().contains(keyword) ||
                    disease.getDescription().contains(keyword))
                .collect(Collectors.toList());
        }

        DiseaseListResponse response = DiseaseListResponse.builder()
            .diseases(mockDiseases)
            .total(mockDiseases.size())
            .build();

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<DiseaseDetailResponse> getDisease(Long diseaseId) {
        DiseaseDetailResponse mockResponse = DiseaseDetailResponse.builder()
            .id(diseaseId)
            .name("파보 바이러스")
            .description("개에게 발생하는 치명적인 바이러스성 질환입니다. 주로 어린 강아지에게 발병하며 구토, 설사, 무기력 등의 증상이 나타납니다.")
            .possibleCauses(Arrays.asList(
                "바이러스 감염",
                "면역력 약화",
                "어린 강아지",
                "백신 미접종"
            ))
            .recommendedTreatments(Arrays.asList(
                "입원 치료",
                "수액 요법",
                "항생제 투여",
                "항구토제",
                "영양 보충"
            ))
            .recommendedDiet(Arrays.asList(
                "소화가 잘 되는 유동식",
                "영양가 높은 식이요법",
                "수분 공급"
            ))
            .recommendedCare(Arrays.asList(
                "충분한 휴식",
                "정기적인 체크업",
                "격리 조치",
                "소독 관리"
            ))
            .build();

        return ResponseEntity.ok(mockResponse);
    }

    @Override
    public ResponseEntity<DiseaseAnalysisResponse> analyzeDisease(DiseaseAnalysisRequest request) {
        List<DiseaseAnalysisResponse.DiseasePrediction> mockPredictions = Arrays.asList(
            DiseaseAnalysisResponse.DiseasePrediction.builder()
                .diseaseId(1L)
                .diseaseName("파보 바이러스")
                .confidence(0.85)
                .build(),
            DiseaseAnalysisResponse.DiseasePrediction.builder()
                .diseaseId(4L)
                .diseaseName("장염")
                .confidence(0.65)
                .build(),
            DiseaseAnalysisResponse.DiseasePrediction.builder()
                .diseaseId(7L)
                .diseaseName("구토증")
                .confidence(0.45)
                .build()
        );

        DiseaseAnalysisResponse mockResponse = DiseaseAnalysisResponse.builder()
            .petId(request.getPetId())
            .analyzedAt(java.time.LocalDateTime.now())
            .symptoms(request.getSymptoms())
            .predictions(mockPredictions)
            .build();

        return ResponseEntity.ok(mockResponse);
    }
}
