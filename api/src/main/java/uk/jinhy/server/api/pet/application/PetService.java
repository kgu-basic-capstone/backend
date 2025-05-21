package uk.jinhy.server.api.pet.application;

import uk.jinhy.server.api.pet.presentation.PetDto.PetCreateRequest;
import uk.jinhy.server.api.pet.presentation.PetDto.PetDetailResponse;
import uk.jinhy.server.api.pet.presentation.PetDto.PetListResponse;
import uk.jinhy.server.api.pet.presentation.PetDto.HealthRecordResponse;
import uk.jinhy.server.api.pet.presentation.PetDto.HealthRecordRequest;

import java.time.LocalDate;
import java.util.List;

public interface PetService {
    PetDetailResponse createPet(PetCreateRequest request);
    PetListResponse getPets();
    PetDetailResponse getPet(Long petId);
    void deletePet(Long petId);
    HealthRecordResponse addHealthRecord(Long petId, HealthRecordRequest request);
    List<HealthRecordResponse> getHealthRecords(Long petId, LocalDate since);
    void deleteHealthRecord(Long petId, Long recordId);

}
