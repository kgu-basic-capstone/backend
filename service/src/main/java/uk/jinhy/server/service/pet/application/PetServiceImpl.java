package uk.jinhy.server.service.pet.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.jinhy.server.api.pet.application.PetService;
import uk.jinhy.server.api.pet.domain.Pet;
import uk.jinhy.server.api.pet.presentation.PetDto.PetCreateRequest;
import uk.jinhy.server.api.pet.presentation.PetDto.PetDetailResponse;
import uk.jinhy.server.api.pet.presentation.PetDto.PetListResponse;
import uk.jinhy.server.service.domain.UserEntity;
import uk.jinhy.server.service.pet.domain.*;
import uk.jinhy.server.service.pet.exception.PetNotFoundException;
import uk.jinhy.server.service.pet.exception.UserNotFoundException;
import uk.jinhy.server.service.user.domain.UserMapper;
import uk.jinhy.server.service.user.domain.UserRepository;
import uk.jinhy.server.api.pet.presentation.PetDto.HealthRecordResponse;
import uk.jinhy.server.api.pet.presentation.PetDto.HealthRecordRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final HealthRecordRepository healthRecordRepository;
    private final HealthRecordMapper healthRecordMapper;
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

    @Override
    public HealthRecordResponse addHealthRecord(Long petId, HealthRecordRequest request) {
        PetEntity petEntity = petRepository.findById(petId)
            .orElseThrow(() -> new PetNotFoundException(petId));

        HealthRecordEntity entity = HealthRecordEntity.builder()
            .pet(petEntity)
            .checkDate(request.getCheckDate())
            .weight(request.getWeight())
            .category(request.getCategory())
            .notes(request.getNotes())
            .build();

        HealthRecordEntity saved = healthRecordRepository.save(entity);

        return HealthRecordResponse.from(healthRecordMapper.toDomain(saved));
    }


    @Override
    public List<HealthRecordResponse> getHealthRecords(Long petId, LocalDate since) {
        PetEntity petEntity = petRepository.findById(petId)
            .orElseThrow(() -> new PetNotFoundException(petId));

        List<HealthRecordEntity> records;

        if (since != null) {
            records = healthRecordRepository.findByPetAndCheckDateAfter(petEntity, since);
        } else {
            records = healthRecordRepository.findByPet(petEntity);
        }

        return records.stream()
            .map(healthRecordMapper::toDomain)
            .map(HealthRecordResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    public void deleteHealthRecord(Long petId, Long recordId) {
        PetEntity petEntity = petRepository.findById(petId)
            .orElseThrow(() -> new PetNotFoundException(petId));

        HealthRecordEntity record = healthRecordRepository.findById(recordId)
            .orElseThrow(() -> new IllegalArgumentException("건강 기록이 존재하지 않습니다."));

        if (!record.getPet().getId().equals(petEntity.getId())) {
            throw new IllegalArgumentException("해당 반려동물의 건강 기록이 아닙니다.");
        }

        healthRecordRepository.delete(record);
    }

}
