package uk.jinhy.server.service.pet.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uk.jinhy.server.api.pet.application.PetService;
import uk.jinhy.server.api.pet.presentation.PetController;
import uk.jinhy.server.api.pet.presentation.PetDto.HealthRecordRequest;
import uk.jinhy.server.api.pet.presentation.PetDto.HealthRecordResponse;
import uk.jinhy.server.api.pet.presentation.PetDto.PetCreateRequest;
import uk.jinhy.server.api.pet.presentation.PetDto.PetDetailResponse;
import uk.jinhy.server.api.pet.presentation.PetDto.PetListResponse;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PetControllerImpl implements PetController {

    private final PetService petService;

    @Override
    public ResponseEntity<PetDetailResponse> createPet(PetCreateRequest request) {
        PetDetailResponse response = petService.createPet(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<PetListResponse> getPets() {
        PetListResponse response = petService.getPets();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<PetDetailResponse> getPet(Long petId) {
        PetDetailResponse response = petService.getPet(petId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deletePet(Long petId) {
        petService.deletePet(petId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<HealthRecordResponse> addHealthRecord(Long petId, HealthRecordRequest request) {
        HealthRecordResponse response = petService.addHealthRecord(petId, request);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<HealthRecordResponse>> getHealthRecords(Long petId, LocalDateTime since) {
        List<HealthRecordResponse> responses = petService.getHealthRecords(petId, since);
        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<Void> deleteHealthRecord(Long petId, Long recordId) {
        petService.deleteHealthRecord(petId, recordId);
        return ResponseEntity.noContent().build();
    }

}
