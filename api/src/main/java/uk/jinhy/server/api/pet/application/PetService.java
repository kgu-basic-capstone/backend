package uk.jinhy.server.api.pet.application;

import uk.jinhy.server.api.pet.presentation.PetDto.PetCreateRequest;
import uk.jinhy.server.api.pet.presentation.PetDto.PetDetailResponse;
import uk.jinhy.server.api.pet.presentation.PetDto.PetListResponse;

public interface PetService {
    PetDetailResponse createPet(PetCreateRequest request);
    PetListResponse getPets();
    PetDetailResponse getPet(Long petId);
    void deletePet(Long petId);
}
