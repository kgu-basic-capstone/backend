package uk.jinhy.server.service.pet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.jinhy.server.api.domain.User;
import uk.jinhy.server.api.pet.domain.Pet;
import uk.jinhy.server.api.pet.presentation.PetDto.PetCreateRequest;
import uk.jinhy.server.api.pet.presentation.PetDto.PetDetailResponse;
import uk.jinhy.server.service.domain.UserEntity;
import uk.jinhy.server.service.pet.application.PetServiceImpl;
import uk.jinhy.server.service.pet.domain.PetEntity;
import uk.jinhy.server.service.pet.domain.PetMapper;
import uk.jinhy.server.service.pet.domain.PetRepository;
import uk.jinhy.server.service.user.domain.UserMapper;
import uk.jinhy.server.service.user.domain.UserRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PetServiceImplTest {
    private PetRepository petRepository;
    private UserRepository userRepository;
    private PetMapper petMapper;
    private UserMapper userMapper;
    private PetServiceImpl petService;

    @BeforeEach
    void setUp() {
        petRepository = mock(PetRepository.class);
        userRepository = mock(UserRepository.class);
        petMapper = mock(PetMapper.class);
        userMapper = mock(UserMapper.class);
        petService = new PetServiceImpl(petRepository, userRepository, petMapper, userMapper);
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
        assertEquals("Bobby", response.getName());
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
        assertEquals("Charlie", response.getName());
        assertEquals("jinhy", response.getOwner().getUsername());
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
        assertEquals(1, result.getTotal());
        assertEquals("Milo", result.getPets().get(0).getName());
        assertEquals("minji", result.getPets().get(0).getOwner().getUsername());
    }
}
