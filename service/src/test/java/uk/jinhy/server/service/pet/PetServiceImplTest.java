package uk.jinhy.server.service.pet;

import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.jinhy.server.api.domain.User;
import uk.jinhy.server.api.pet.domain.Pet;
import uk.jinhy.server.api.pet.presentation.PetDto.PetCreateRequest;
import uk.jinhy.server.api.pet.presentation.PetDto.PetDetailResponse;
import uk.jinhy.server.service.pet.domain.PetEntity;
import uk.jinhy.server.service.domain.UserEntity;
import uk.jinhy.server.service.pet.application.PetServiceImpl;
import uk.jinhy.server.service.pet.domain.*;
import uk.jinhy.server.service.user.domain.UserMapper;
import uk.jinhy.server.service.user.domain.UserRepository;
import uk.jinhy.server.api.pet.domain.HealthRecord;
import uk.jinhy.server.api.pet.presentation.PetDto.HealthRecordRequest;
import uk.jinhy.server.api.pet.presentation.PetDto.HealthRecordResponse;
import uk.jinhy.server.service.pet.domain.HealthRecordMapper;
import uk.jinhy.server.service.pet.domain.HealthRecordRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

import static java.time.LocalDateTime.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static uk.jinhy.server.api.pet.domain.HealthRecordCategories.*;

class PetServiceImplTest {
    private PetRepository petRepository;
    private UserRepository userRepository;
    private PetMapper petMapper;
    private UserMapper userMapper;
    private PetServiceImpl petService;
    private HealthRecordRepository healthRecordRepository;
    private HealthRecordMapper healthRecordMapper;

    @BeforeEach
    void setUp() {
        petRepository = mock(PetRepository.class);
        userRepository = mock(UserRepository.class);
        healthRecordRepository = mock(HealthRecordRepository.class);
        healthRecordMapper = mock(HealthRecordMapper.class);
        petMapper = mock(PetMapper.class);
        userMapper = mock(UserMapper.class);
        petService = new PetServiceImpl(petRepository, userRepository, healthRecordRepository, healthRecordMapper, petMapper, userMapper);
    }

    @Test
    void createPet_ShouldCreateAndReturnPetDetailResponse() {
        // Given
        Long userId = 1L;
        PetCreateRequest request = new PetCreateRequest("Bobby", LocalDate.of(2021, 1, 1), userId);
        UserEntity mockUserEntity = mock(UserEntity.class);
        User mockUser = mock(User.class);
        Pet pet = Pet.builder().name("Bobby").birthDate(LocalDate.of(2021, 1, 1)).owner(mockUser).build();
        PetEntity petEntity = mock(PetEntity.class);
        Pet savedPet = Pet.builder().name("Bobby").birthDate(LocalDate.of(2021, 1, 1)).owner(mockUser).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUserEntity));
        when(userMapper.toDomain(mockUserEntity)).thenReturn(mockUser);
        when(petMapper.toEntity(any(Pet.class))).thenReturn(petEntity);
        when(petRepository.save(petEntity)).thenReturn(petEntity);
        when(petMapper.toDomain(petEntity)).thenReturn(savedPet);

        // When
        PetDetailResponse response = petService.createPet(request);

        // Then
        assertNotNull(response);
        assertThat(response.getName()).isEqualTo("Bobby");
    }

    @Test
    void getPet_ShouldReturnPetDetailResponse() {
        // Given
        Long petId = 1L;
        PetEntity petEntity = mock(PetEntity.class);
        Pet pet = mock(Pet.class);
        User owner = mock(User.class);

        when(owner.getId()).thenReturn(2L);
        when(owner.getUsername()).thenReturn("jinhy");

        when(pet.getOwner()).thenReturn(owner);
        when(pet.getName()).thenReturn("Charlie");
        when(pet.getBirthDate()).thenReturn(LocalDate.of(2022, 1, 1));

        when(petRepository.findById(petId)).thenReturn(Optional.of(petEntity));
        when(petMapper.toDomain(petEntity)).thenReturn(pet);

        // When
        PetDetailResponse response = petService.getPet(petId);

        // Then
        assertNotNull(response);
        assertThat(response.getName()).isEqualTo("Charlie");
        assertThat(response.getOwner().getUsername()).isEqualTo("jinhy");
    }


    @Test
    void deletePet_ShouldCallRepositoryDelete() {
        // Given
        Long petId = 1L;
        PetEntity petEntity = mock(PetEntity.class);
        when(petRepository.findById(petId)).thenReturn(Optional.of(petEntity));

        // When
        petService.deletePet(petId);

        // Then
        verify(petRepository).delete(petEntity);
    }

    @Test
    void getPets_ShouldReturnListOfPetDetailResponses() {
        // Given
        PetEntity petEntity = mock(PetEntity.class);
        Pet pet = mock(Pet.class);
        User owner = mock(User.class);

        when(owner.getId()).thenReturn(3L);
        when(owner.getUsername()).thenReturn("minji");

        when(pet.getOwner()).thenReturn(owner);
        when(pet.getName()).thenReturn("Milo");
        when(pet.getBirthDate()).thenReturn(LocalDate.of(2020, 5, 15));

        when(petRepository.findAll()).thenReturn(List.of(petEntity));
        when(petMapper.toDomain(petEntity)).thenReturn(pet);

        // When
        var result = petService.getPets();

        // Then
        assertThat(result.getTotal()).isEqualTo(1);
        assertThat(result.getPets().get(0).getName()).isEqualTo("Milo");
        assertThat(result.getPets().get(0).getOwner().getUsername()).isEqualTo("minji");
    }

    @Test
    void addHealthRecord_ShouldAddAndReturnHealthRecordResponse() {
        // Given
        Long petId = 1L;
        LocalDate date = LocalDate.of(2023, 4, 10);

        HealthRecordRequest request = new HealthRecordRequest(
            date,
            5.5,
            CHECKUP,
            "정기 검진"
        );

        PetEntity petEntity = mock(PetEntity.class);

        HealthRecordEntity savedEntity = HealthRecordEntity.builder()
            .pet(petEntity)
            .checkDate(date)
            .weight(5.5)
            .category(CHECKUP)
            .notes("정기 검진")
            .build();

        HealthRecord domain = HealthRecord.builder()
            .pet(null)
            .checkDate(date)
            .weight(5.5)
            .category(CHECKUP)
            .notes("정기 검진")
            .build();

        HealthRecordResponse expectedResponse = HealthRecordResponse.from(domain);

        when(petRepository.findById(petId)).thenReturn(Optional.of(petEntity));
        when(healthRecordRepository.save(any(HealthRecordEntity.class))).thenReturn(savedEntity);
        when(healthRecordMapper.toDomain(savedEntity)).thenReturn(domain);

        // When
        HealthRecordResponse response = petService.addHealthRecord(petId, request);

        // Then
        assertThat(response)
            .usingRecursiveComparison()
            .isEqualTo(expectedResponse);

        verify(healthRecordRepository).save(any(HealthRecordEntity.class));
    }


    @Test
    void getHealthRecords_WithSince_ShouldReturnFilteredList() {
        // Given
        Long petId = 1L;
        LocalDate since = LocalDate.of(2024, 1, 1);
        PetEntity petEntity = mock(PetEntity.class);
        HealthRecordEntity recordEntity = mock(HealthRecordEntity.class);
        HealthRecord domain = mock(HealthRecord.class);

        when(petRepository.findById(petId)).thenReturn(Optional.of(petEntity));
        when(healthRecordRepository.findByPetAndCheckDateAfter(petEntity, since))
            .thenReturn(List.of(recordEntity));
        when(healthRecordMapper.toDomain(recordEntity)).thenReturn(domain);

        HealthRecordResponse expected = HealthRecordResponse.builder()
            .checkDate(LocalDate.of(2024, 2, 1))
            .weight(4.2)
            .category(DISEASE)
            .notes("안구 질환 발견")
            .build();

        when(domain.getCheckDate()).thenReturn(expected.getCheckDate());
        when(domain.getWeight()).thenReturn(expected.getWeight());
        when(domain.getCategory()).thenReturn(expected.getCategory());
        when(domain.getNotes()).thenReturn(expected.getNotes());

        // When
        List<HealthRecordResponse> result = petService.getHealthRecords(petId, since);

        // Then
        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(List.of(expected));
    }

    @Test
    void getHealthRecords_WithoutSince_ShouldReturnAllRecords() {
        // Given
        Long petId = 1L;
        PetEntity petEntity = mock(PetEntity.class);
        HealthRecordEntity recordEntity = mock(HealthRecordEntity.class);
        HealthRecord domain = mock(HealthRecord.class);

        when(petRepository.findById(petId)).thenReturn(Optional.of(petEntity));
        when(healthRecordRepository.findByPet(petEntity))
            .thenReturn(List.of(recordEntity));
        when(healthRecordMapper.toDomain(recordEntity)).thenReturn(domain);

        HealthRecordResponse expected = HealthRecordResponse.builder()
            .checkDate(LocalDate.of(2024, 2, 1))
            .weight(4.2)
            .category(DISEASE)
            .notes("안구 질환 발견")
            .build();

        when(domain.getCheckDate()).thenReturn(expected.getCheckDate());
        when(domain.getWeight()).thenReturn(expected.getWeight());
        when(domain.getCategory()).thenReturn(expected.getCategory());
        when(domain.getNotes()).thenReturn(expected.getNotes());

        // When
        List<HealthRecordResponse> result = petService.getHealthRecords(petId, null);

        // Then
        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(List.of(expected));
    }

    @Test
    void deleteHealthRecord_ShouldDeleteIfExistsAndBelongsToPet() {
        // Given
        Long petId = 1L;
        Long recordId = 10L;

        PetEntity petEntity = mock(PetEntity.class);
        HealthRecordEntity record = mock(HealthRecordEntity.class);

        when(petRepository.findById(petId)).thenReturn(Optional.of(petEntity));
        when(healthRecordRepository.findById(recordId)).thenReturn(Optional.of(record));
        when(record.getPet()).thenReturn(petEntity);
        when(petEntity.getId()).thenReturn(petId);

        // When
        petService.deleteHealthRecord(petId, recordId);

        // Then
        verify(healthRecordRepository).delete(record);
    }

}
