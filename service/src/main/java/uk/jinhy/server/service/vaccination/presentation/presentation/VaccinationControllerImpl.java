package uk.jinhy.server.service.vaccination.presentation.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import uk.jinhy.server.api.common.exception.ErrorResponse;
import uk.jinhy.server.api.vaccination.presentation.VaccinationController;
import uk.jinhy.server.service.pet.exception.PetNotFoundException;
import uk.jinhy.server.service.vaccination.presentation.VaccinationService;
import uk.jinhy.server.api.vaccination.presentation.VaccinationDto;

@RestController
@RequiredArgsConstructor
public class VaccinationControllerImpl implements VaccinationController {

    private final VaccinationService vaccinationService;

    @Override
    public ResponseEntity<VaccinationDto.VaccinationResponse> addVaccination(Long petId, VaccinationDto.VaccinationRequest request) {
        VaccinationDto.VaccinationResponse response = vaccinationService.addVaccination(petId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<VaccinationDto.VaccinationListResponse> getVaccinations(Long petId, Boolean completed, Boolean upcoming) {
        VaccinationDto.VaccinationListResponse response = vaccinationService.getVaccinations(petId, completed, upcoming);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deleteVaccination(Long petId, Long vaccinationId) {
        vaccinationService.deleteVaccination(petId, vaccinationId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<VaccinationDto.VaccinationResponse> completeVaccination(Long petId, Long vaccinationId, boolean completed) {
        VaccinationDto.VaccinationResponse response = vaccinationService.completeVaccination(petId, vaccinationId, completed);
        return ResponseEntity.ok(response);
    }
    @Override
    public ResponseEntity<VaccinationDto.VaccinationListResponse> getVaccinationsByUserId(Long userId, Boolean completed, Boolean upcoming) {
        VaccinationDto.VaccinationListResponse response = vaccinationService.getVaccinationsByUserId(userId, completed, upcoming);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(PetNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePetNotFoundException(PetNotFoundException ex) {
        ErrorResponse errorResponse =  ErrorResponse.of(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = ErrorResponse.of(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "예상치 못한 오류가 발생했습니다: " + ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

