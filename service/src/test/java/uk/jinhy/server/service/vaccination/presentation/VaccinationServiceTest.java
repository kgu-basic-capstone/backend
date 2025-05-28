package uk.jinhy.server.service.vaccination.presentation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import uk.jinhy.server.api.vaccination.presentation.exception.VaccinationNotFoundException;
import uk.jinhy.server.api.vaccination.presentation.VaccinationDto;
import uk.jinhy.server.service.pet.exception.PetNotFoundException;
import uk.jinhy.server.service.vaccination.presentation.domain.VaccinationEntity;
import uk.jinhy.server.service.pet.domain.PetEntity;
import uk.jinhy.server.service.pet.domain.PetRepository;
import uk.jinhy.server.service.vaccination.presentation.domain.VaccinationMapper;
import uk.jinhy.server.service.vaccination.presentation.domain.VaccinationRepository;
import uk.jinhy.server.api.vaccination.presentation.VaccinationStatusType;
import uk.jinhy.server.service.user.domain.UserEntity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class VaccinationServiceTest {

    @Mock
    private VaccinationRepository vaccinationRepository;
    @Mock
    private PetRepository petRepository;
    @Mock
    private VaccinationMapper vaccinationMapper;

    @InjectMocks
    private VaccinationService vaccinationService;

    private final Long ownerId = 1L;
    private final Long pet1Id = 10L;
    private final Long pet2Id = 11L;
    private final Long vaccination1Id = 100L;
    private final Long vaccination2Id = 101L;

    private UserEntity testOwner;
    private PetEntity testPet1;
    private PetEntity testPet2;

    @BeforeEach
    void setUp() {
        testOwner = UserEntity.builder().username("TestOwner").build();

        testPet1 = PetEntity.builder().name("Buddy").owner(testOwner).birthDate(LocalDate.now().minusYears(1)).build();
        testPet2 = PetEntity.builder().name("Lucy").owner(testOwner).birthDate(LocalDate.now().minusMonths(6)).build();
    }

    private VaccinationEntity createVaccination(Long id, PetEntity pet, String vaccineName, LocalDate vaccinationDate, LocalDate nextDate, boolean isCompleted, VaccinationStatusType statusType) {
        VaccinationEntity vaccination = VaccinationEntity.builder()
            .id(id)
            .pet(pet)
            .vaccineName(vaccineName)
            .vaccinationDate(vaccinationDate)
            .nextVaccinationDate(nextDate)
            .isCompleted(isCompleted)
            .statusType(statusType)
            .build();
        // 테스트 편의를 위해 ID 설정 (실제 엔티티에 setId가 있거나, ReflectionTestUtils 사용)
        return vaccination;
    }

    // === 기능 2: 기록 추가 (POST) ===
    @Test
    @DisplayName("[기능2] 백신 기록 추가 성공 (상태/타입 포함)")
    void addVaccination_withStatusType_success() {
        VaccinationDto.VaccinationRequest request = VaccinationDto.VaccinationRequest.builder()
            .vaccineName("1차")
            .vaccinationDate(LocalDate.now())
            .nextVaccinationDate(LocalDate.now().plusWeeks(4))
            .statusType(VaccinationStatusType.MANDATORY)
            .build();

         PetEntity mockPet = PetEntity.builder().name("Buddy").owner(testOwner).birthDate(LocalDate.now().minusYears(1)).build();
         ReflectionTestUtils.setField(mockPet, "id", pet1Id); // mockPet에 ID 설정

        //  petRepository.findById가 반환할 객체에 ID가 있다고 가정
        PetEntity foundPet = PetEntity.builder().name(testPet1.getName()).owner(testPet1.getOwner()).birthDate(testPet1.getBirthDate()).build();
        when(petRepository.findById(pet1Id)).thenReturn(Optional.of(foundPet));

        when(vaccinationRepository.save(any(VaccinationEntity.class))).thenAnswer(invocation -> {
            VaccinationEntity entityToSave = invocation.getArgument(0);
            return entityToSave;
        });
        VaccinationDto.VaccinationResponse expectedResponseDto = VaccinationDto.VaccinationResponse.builder()
            .vaccineName("1차")
            .petId(pet1Id)
            .vaccinationDate(LocalDate.now())
            .nextVaccinationDate(LocalDate.now().plusWeeks(4))
            .statusType(VaccinationStatusType.MANDATORY)
            .build();

        when(vaccinationMapper.fromEntity(any(VaccinationEntity.class))).thenReturn(expectedResponseDto);

        VaccinationDto.VaccinationResponse response = vaccinationService.addVaccination(pet1Id, request);

        assertThat(response).isNotNull();
        assertThat(response.getVaccineName()).isEqualTo("1차");
        assertThat(response.getStatusType()).isEqualTo(VaccinationStatusType.MANDATORY);
        assertThat(response.isCompleted()).isFalse();
        assertThat(response.getPetId()).isEqualTo(pet1Id);
        //assertThat(response.getId()).isEqualTo(vaccination1Id); // 저장 시 반환되는 DTO의 ID 검증

        ArgumentCaptor<VaccinationEntity> entityCaptor = ArgumentCaptor.forClass(VaccinationEntity.class);
        verify(vaccinationRepository).save(entityCaptor.capture());
        VaccinationEntity capturedEntity = entityCaptor.getValue();

        assertThat(capturedEntity.getVaccineName()).isEqualTo("1차");
        assertThat(capturedEntity.getVaccinationStatus()).isEqualTo(VaccinationStatusType.MANDATORY);
        assertThat(capturedEntity.isCompleted()).isFalse();
        assertThat(capturedEntity.getPet()).isEqualTo(foundPet);
    }

    @Test
    @DisplayName("[기능2] 백신 추가 실패 - 반려동물 없음")
    void addVaccination_fail_petNotFound() {
        Long nonExistentPetId = 9999L;
        VaccinationDto.VaccinationRequest request = VaccinationDto.VaccinationRequest.builder()
            .vaccineName("TestVax")
            .vaccinationDate(LocalDate.now())
            .statusType(VaccinationStatusType.OPTIONAL)
            .build();
        when(petRepository.findById(nonExistentPetId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vaccinationService.addVaccination(nonExistentPetId, request))
            .isInstanceOf(PetNotFoundException.class) // PetNotFoundException 사용 확인
            .hasMessageContaining("Pet not found with id: " + nonExistentPetId);
        verify(petRepository).findById(nonExistentPetId);
        verify(vaccinationRepository, never()).save(any());
    }

    // === 기능 1: 백신 기록 조회 (GET) ===
    // --- 1a. petId 기반 조회 ---
    @Test
    @DisplayName("[기능1a] petId로 백신 목록 조회 - 데이터 있는 경우")
    void getVaccinations_byPetId_withData() {

        VaccinationEntity v1 = createVaccination(1L, testPet1, "V1", LocalDate.now(), null, true, VaccinationStatusType.MANDATORY);
        VaccinationEntity v2 = createVaccination(2L, testPet1, "V2", LocalDate.now().plusDays(10), LocalDate.now().plusMonths(1), false, VaccinationStatusType.SCHEDULED);

        when(petRepository.existsById(pet1Id)).thenReturn(true);
        when(vaccinationRepository.findByPetId(pet1Id)).thenReturn(List.of(v1, v2));

        VaccinationDto.VaccinationListResponse response = vaccinationService.getVaccinations(pet1Id, null, null);

        assertThat(response.getVaccinations()).hasSize(2);
        assertThat(response.getTotal()).isEqualTo(2);
        assertThat(response.getVaccinations().get(0).getVaccineName()).isEqualTo("V1");
        assertThat(response.getVaccinations().get(1).getVaccineName()).isEqualTo("V2");
        assertThat(response.getVaccinations().get(0).getStatusType()).isEqualTo(VaccinationStatusType.MANDATORY);
        assertThat(response.getVaccinations().get(1).getStatusType()).isEqualTo(VaccinationStatusType.SCHEDULED);
        verify(vaccinationRepository).findByPetId(pet1Id);
    }

    @Test
    @DisplayName("[기능1a] petId로 백신 목록 조회 - 완료된 것만")
    void getVaccinations_byPetId_completedOnly() {
        VaccinationEntity v1 = createVaccination(1L, testPet1, "V1-Completed", LocalDate.now().minusDays(10), null, true, VaccinationStatusType.MANDATORY);
        VaccinationEntity v2 = createVaccination(2L, testPet1, "V2-Upcoming", LocalDate.now(), LocalDate.now().plusMonths(1), false, VaccinationStatusType.SCHEDULED);

        when(petRepository.existsById(pet1Id)).thenReturn(true);
        when(vaccinationRepository.findByPetId(pet1Id)).thenReturn(List.of(v1, v2));

        VaccinationDto.VaccinationResponse vaccinationResponse = new VaccinationDto.VaccinationResponse(1L, 1L, "V1-Completed", LocalDate.now().minusDays(10), null, true, VaccinationStatusType.MANDATORY);
        when(vaccinationMapper.fromEntity(any())).thenReturn(vaccinationResponse);

        VaccinationDto.VaccinationListResponse response = vaccinationService.getVaccinations(pet1Id, true, null);

        assertThat(response.getVaccinations()).hasSize(1);
        assertThat(response.getVaccinations().get(0).getVaccineName()).isEqualTo("V1-Completed");
        assertThat(response.getVaccinations().get(0).isCompleted()).isTrue();
    }


    // --- 1b. userId 기반 조회 ---
    @Test
    @DisplayName("[기능1b] userId로 백신 목록 조회 - 데이터 있는 경우")
    void getVaccinationsByUserId_withData() {
        // setUp에서 생성된 testOwner, testPet1, testPet2 사용 (ID 설정 가정)
        // testOwner.setId(ownerId);
        // testPet1.setId(pet1Id); testPet1.setOwner(testOwner);
        // testPet2.setId(pet2Id); testPet2.setOwner(testOwner);

        VaccinationEntity v1 = createVaccination(1L, testPet1, "P1_V1", LocalDate.now().minusDays(5), null, true, VaccinationStatusType.MANDATORY);
        VaccinationEntity v2 = createVaccination(2L, testPet2, "P2_V1", LocalDate.now().minusDays(2), null, false, VaccinationStatusType.SCHEDULED);

        when(vaccinationRepository.findByPetOwnerId(ownerId)).thenReturn(List.of(v1, v2));

        VaccinationDto.VaccinationListResponse response = vaccinationService.getVaccinationsByUserId(ownerId, null, null);

        assertThat(response.getVaccinations()).hasSize(2);
        verify(vaccinationRepository).findByPetOwnerId(ownerId);
    }


    // === 기능 4: 백신 완료 상태 변경 (PATCH) ===
    @Test
    @DisplayName("[기능4] 백신 완료 상태 변경 성공 (false -> true)")
    void completeVaccination_setToTrue_success() {
        //testPet1.setId(pet1Id);
        VaccinationEntity existingVaccination = createVaccination(vaccination1Id, testPet1, "TestVax", LocalDate.now().minusDays(7), null, false, VaccinationStatusType.SCHEDULED);

        when(vaccinationRepository.findById(vaccination1Id)).thenReturn(Optional.of(existingVaccination));
        when(vaccinationRepository.save(any(VaccinationEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VaccinationDto.VaccinationResponse response = vaccinationService.completeVaccination(pet1Id, vaccination1Id, true);

        assertThat(response.isCompleted()).isTrue();
        assertThat(response.getId()).isEqualTo(vaccination1Id);

        ArgumentCaptor<VaccinationEntity> captor = ArgumentCaptor.forClass(VaccinationEntity.class);
        verify(vaccinationRepository).save(captor.capture());
        assertThat(captor.getValue().isCompleted()).isTrue();
        assertThat(captor.getValue().getVaccinationStatus()).isEqualTo(VaccinationStatusType.SCHEDULED);
    }

    @Test
    @DisplayName("[기능4] 백신 완료 상태 변경 실패 - 백신 없음")
    void completeVaccination_fail_vaccinationNotFound() {
        Long nonExistentVaccinationId = 999L;
        when(vaccinationRepository.findById(nonExistentVaccinationId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vaccinationService.completeVaccination(pet1Id, nonExistentVaccinationId, true))
            .isInstanceOf(VaccinationNotFoundException.class)
            .hasMessageContaining("Vaccination not found with id: " + nonExistentVaccinationId);
    }

    @Test
    @DisplayName("[기능4] 백신 완료 상태 변경 실패 - 반려동물 불일치")
    void completeVaccination_fail_petMismatch() {
        testPet1.setId(pet1Id);
        testPet2.setId(pet2Id);
        VaccinationEntity existingVaccination = createVaccination(vaccination1Id, testPet1, "VaxForPet1", LocalDate.now(), null, false, VaccinationStatusType.OPTIONAL);

        when(vaccinationRepository.findById(vaccination1Id)).thenReturn(Optional.of(existingVaccination));
        ReflectionTestUtils.setField(existingVaccination, "id", vaccination1Id);

        assertThatThrownBy(() -> vaccinationService.completeVaccination(pet2Id, vaccination1Id, true)) // 다른 petId (pet2Id) 사용
            .isInstanceOf(VaccinationNotFoundException.class)
            .hasMessage("Vaccination does not belong to the specified pet");
    }

    // === 기능 5: 백신 기록 삭제 (DELETE) ===
    @Test
    @DisplayName("[기능5] 백신 기록 삭제 성공")
    void deleteVaccination_success() {
        // testPet1에 ID가 설정되어 있다고 가정 (예: setUp에서 pet1Id 값을 사용)
        ReflectionTestUtils.setField(testPet1, "id", pet1Id);

        // createVaccination 헬퍼 메소드가 반환하는 엔티티에 ID가 설정되어 있다고 가정
        VaccinationEntity existingVaccination = createVaccination(
            vaccination1Id, // createVaccination 헬퍼에 ID를 넘기도록 수정했다고 가정
            testPet1,
            "ToDelete",
            LocalDate.now(),
            null,
            false,
            VaccinationStatusType.ETC
        );

        Long idToDelete = existingVaccination.getId();

        when(vaccinationRepository.findById(idToDelete)).thenReturn(Optional.of(existingVaccination));
        doNothing().when(vaccinationRepository).delete(existingVaccination);

        assertDoesNotThrow(() -> vaccinationService.deleteVaccination(pet1Id, idToDelete));

        verify(vaccinationRepository).delete(existingVaccination);
    }

    @Test
    @DisplayName("[기능5] 백신 기록 삭제 실패 - 백신 없음")
    void deleteVaccination_fail_vaccinationNotFound() {
        Long nonExistentVaccinationId = 999L;
        when(vaccinationRepository.findById(nonExistentVaccinationId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vaccinationService.deleteVaccination(pet1Id, nonExistentVaccinationId))
            .isInstanceOf(VaccinationNotFoundException.class)
            .hasMessageContaining("Vaccination not found with id: " + nonExistentVaccinationId);
    }

    // TODO: 기능 3 (일반 정보 수정 - updateVaccination)에 대한 테스트 케이스 추가 필요
}
