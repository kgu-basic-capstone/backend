package uk.jinhy.server.service.vaccination.presentation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
    //    @Override
//    public ResponseEntity<VaccinationResponse> addVaccination(Long petId, VaccinationRequest request) {
//        VaccinationResponse mockResponse = VaccinationResponse.builder()
//            .id(1L)
//            .petId(petId)
//            .vaccineName(request.getVaccineName())
//            .vaccinationDate(request.getVaccinationDate())
//            .nextVaccinationDate(request.getNextVaccinationDate())
//            .isCompleted(false)
//            .build();
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(mockResponse);
//    }


//    @Override
//    public ResponseEntity<Void> deleteVaccination(Long petId, Long vaccinationId) {
//        return ResponseEntity.noContent().build();
//    }
//
//    @Override
//    public ResponseEntity<VaccinationResponse> completeVaccination(Long petId, Long vaccinationId, boolean completed) {
//        VaccinationResponse mockResponse = VaccinationResponse.builder()
//            .id(vaccinationId)
//            .petId(petId)
//            .vaccineName("종합백신 3차")
//            .vaccinationDate(LocalDate.now())
//            .nextVaccinationDate(LocalDate.now().plusMonths(12))
//            .isCompleted(completed)
//            .build();
//
//        return ResponseEntity.ok(mockResponse);
//    }
