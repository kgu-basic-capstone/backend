package uk.jinhy.server.service.vaccination.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import uk.jinhy.server.api.vaccination.presentation.VaccinationController;
import uk.jinhy.server.api.vaccination.presentation.VaccinationDto.VaccinationRequest;
import uk.jinhy.server.api.vaccination.presentation.VaccinationDto.VaccinationResponse;
import uk.jinhy.server.api.vaccination.presentation.VaccinationDto.VaccinationListResponse;

@RestController
@RequiredArgsConstructor
public class VaccinationControllerImpl implements VaccinationController {

    private final VaccinationService vaccinationService;

    @Override
    public ResponseEntity<VaccinationResponse> addVaccination(Long petId, VaccinationRequest request) {
        VaccinationResponse response = vaccinationService.addVaccination(petId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<VaccinationListResponse> getVaccinations(Long petId, Boolean completed, Boolean upcoming) {
        VaccinationListResponse response = vaccinationService.getVaccinations(petId, completed, upcoming);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deleteVaccination(Long petId, Long vaccinationId) {
        vaccinationService.deleteVaccination(petId, vaccinationId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<VaccinationResponse> completeVaccination(Long petId, Long vaccinationId, boolean completed) {
        VaccinationResponse response = vaccinationService.completeVaccination(petId, vaccinationId, completed);
        return ResponseEntity.ok(response);
    }
    @Override
    public ResponseEntity<VaccinationListResponse> getVaccinationsByUserId(Long userId, Boolean completed, Boolean upcoming) {
        VaccinationListResponse response = vaccinationService.getVaccinationsByUserId(userId, completed, upcoming);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(PetNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePetNotFoundException(PetNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "예상치 못한 오류가 발생했습니다: " + ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

