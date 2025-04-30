package uk.jinhy.server.service.pet.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.jinhy.server.api.domain.User;
import uk.jinhy.server.api.pet.application.PetService;
import uk.jinhy.server.api.pet.domain.Pet;
import uk.jinhy.server.api.pet.presentation.PetDto.PetCreateRequest;
import uk.jinhy.server.api.pet.presentation.PetDto.PetDetailResponse;
import uk.jinhy.server.api.pet.presentation.PetDto.PetListResponse;
import uk.jinhy.server.service.domain.UserEntity;
import uk.jinhy.server.service.pet.domain.PetEntity;
import uk.jinhy.server.service.pet.domain.PetMapper;
import uk.jinhy.server.service.pet.domain.PetRepository;
import uk.jinhy.server.service.pet.exception.PetNotFoundException;
import uk.jinhy.server.service.pet.exception.UserNotFoundException;
import uk.jinhy.server.service.user.domain.UserMapper;
import uk.jinhy.server.service.user.domain.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final PetMapper petMapper;
    private final UserMapper userMapper;

    @Override
    public PetDetailResponse createPet(PetCreateRequest request) {
        UserEntity ownerEntity = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new UserNotFoundException(request.getUserId()));

        Pet pet = Pet.builder()
            .name(request.getName())
            .birthDate(request.getBirthDate())
            .owner(userMapper.toDomain(ownerEntity))
            .build();

        PetEntity petEntity = petMapper.toEntity(pet); // Pet -> PetEntity 변환
        petEntity = petRepository.save(petEntity);    // 저장

        Pet savedPet = petMapper.toDomain(petEntity); // 다시 Entity -> Domain 변환

        return PetDetailResponse.from(savedPet);
    }

    @Override
    public PetListResponse getPets() {
        List<PetEntity> petEntities = petRepository.findAll();

        List<PetDetailResponse> petResponses = petEntities.stream()
            .map(petEntity -> {
                Pet pet = petMapper.toDomain(petEntity);
                return PetDetailResponse.from(pet);
            })
            .collect(Collectors.toList());

        return new PetListResponse(petResponses, petResponses.size());
    }

    @Override
    public PetDetailResponse getPet(Long petId) {
        PetEntity petEntity = petRepository.findById(petId)
            .orElseThrow(() -> new PetNotFoundException(petId));

        Pet pet = petMapper.toDomain(petEntity);

        return PetDetailResponse.from(pet);
    }

    @Override
    public void deletePet(Long petId) {
        PetEntity petEntity = petRepository.findById(petId)
            .orElseThrow(() -> new PetNotFoundException(petId));

        petRepository.delete(petEntity);
    }
}
