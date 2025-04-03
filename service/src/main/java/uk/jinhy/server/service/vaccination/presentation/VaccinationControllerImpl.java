package uk.jinhy.server.service.vaccination.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uk.jinhy.server.api.vaccination.presentation.VaccinationController;
import uk.jinhy.server.api.vaccination.presentation.VaccinationDto.VaccinationRequest;
import uk.jinhy.server.api.vaccination.presentation.VaccinationDto.VaccinationResponse;
import uk.jinhy.server.api.vaccination.presentation.VaccinationDto.VaccinationListResponse;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class VaccinationControllerImpl implements VaccinationController {

    @Override
    public ResponseEntity<VaccinationResponse> addVaccination(Long petId, VaccinationRequest request) {
        VaccinationResponse mockResponse = VaccinationResponse.builder()
            .id(1L)
            .petId(petId)
            .vaccineName(request.getVaccineName())
            .vaccinationDate(request.getVaccinationDate())
            .nextVaccinationDate(request.getNextVaccinationDate())
            .isCompleted(false)
            .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(mockResponse);
    }

    @Override
    public ResponseEntity<VaccinationListResponse> getVaccinations(Long petId, Boolean completed) {
        List<VaccinationResponse> mockVaccinations = Arrays.asList(
            VaccinationResponse.builder()
                .id(1L)
                .petId(petId)
                .vaccineName("종합백신 1차")
                .vaccinationDate(LocalDate.now().minusMonths(6))
                .nextVaccinationDate(LocalDate.now().minusMonths(5))
                .isCompleted(true)
                .build(),
            VaccinationResponse.builder()
                .id(2L)
                .petId(petId)
                .vaccineName("종합백신 2차")
                .vaccinationDate(LocalDate.now().minusMonths(5))
                .nextVaccinationDate(LocalDate.now().minusMonths(4))
                .isCompleted(true)
                .build(),
            VaccinationResponse.builder()
                .id(3L)
                .petId(petId)
                .vaccineName("광견병 예방접종")
                .vaccinationDate(LocalDate.now().minusMonths(3))
                .nextVaccinationDate(LocalDate.now().plusMonths(9))
                .isCompleted(true)
                .build(),
            VaccinationResponse.builder()
                .id(4L)
                .petId(petId)
                .vaccineName("종합백신 3차")
                .vaccinationDate(LocalDate.now().plusMonths(1))
                .nextVaccinationDate(null)
                .isCompleted(false)
                .build()
        );

        if (completed != null) {
            mockVaccinations = mockVaccinations.stream()
                .filter(vaccination -> vaccination.isCompleted() == completed)
                .collect(Collectors.toList());
        }

        VaccinationListResponse response = VaccinationListResponse.builder()
            .vaccinations(mockVaccinations)
            .total(mockVaccinations.size())
            .build();

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deleteVaccination(Long petId, Long vaccinationId) {
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<VaccinationResponse> completeVaccination(Long petId, Long vaccinationId, boolean completed) {
        VaccinationResponse mockResponse = VaccinationResponse.builder()
            .id(vaccinationId)
            .petId(petId)
            .vaccineName("종합백신 3차")
            .vaccinationDate(LocalDate.now())
            .nextVaccinationDate(LocalDate.now().plusMonths(12))
            .isCompleted(completed)
            .build();

        return ResponseEntity.ok(mockResponse);
    }
}
