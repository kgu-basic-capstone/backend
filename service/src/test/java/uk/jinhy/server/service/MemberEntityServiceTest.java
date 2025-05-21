package uk.jinhy.server.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import uk.jinhy.server.api.vaccination.presentation.VaccinationDto;
import uk.jinhy.server.service.common.IntegrationTest;
import uk.jinhy.server.service.domain.VaccinationEntity;
import uk.jinhy.server.service.pet.domain.PetEntity;
import uk.jinhy.server.service.pet.domain.PetRepository;
import uk.jinhy.server.service.user.domain.UserEntity;
import uk.jinhy.server.service.user.domain.UserRepository;
import uk.jinhy.server.service.vaccination.presentation.PetNotFoundException;
import uk.jinhy.server.service.vaccination.presentation.VaccinationRepository;
import uk.jinhy.server.service.vaccination.presentation.VaccinationService;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class VaccinationServiceTest extends IntegrationTest {

    @Autowired
    private VaccinationService vaccinationService;

    @Autowired
    private VaccinationRepository vaccinationRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity testOwner;
    private PetEntity testPet1;
    private PetEntity testPet2;

    @BeforeEach
    void setUp() {
        vaccinationRepository.deleteAll();
        petRepository.deleteAll();
        userRepository.deleteAll();

        testOwner = UserEntity.builder()
            .username("TestOwner")
            .build();
        userRepository.save(testOwner);

        // 테스트용 반려동물 생성 및 저장
        testPet1 = PetEntity.builder().name("Buddy").owner(testOwner).build();
        petRepository.save(testPet1);

        testPet2 = PetEntity.builder().name("Lucy").owner(testOwner).build();
        petRepository.save(testPet2);
    }

    private VaccinationEntity createAndSaveVaccination(PetEntity pet, String vaccineName, LocalDate vaccinationDate, LocalDate nextDate, boolean isCompleted) {
        VaccinationEntity vaccination = VaccinationEntity.builder()
            .pet(pet)
            .vaccineName(vaccineName)
            .vaccinationDate(vaccinationDate)
            .nextVaccinationDate(nextDate)
            .isCompleted(isCompleted)
            .build();
        return vaccinationRepository.save(vaccination);
    }

    @Test
    @DisplayName("백신 기록 추가 성공")
    void addVaccination_success() {
        VaccinationDto.VaccinationRequest request = VaccinationDto.VaccinationRequest.builder()
            .vaccineName(" 종합백신1차 ")
            .vaccinationDate(LocalDate.now())
            .nextVaccinationDate(LocalDate.now().plusWeeks(3))
            .build();
        VaccinationDto.VaccinationResponse response = vaccinationService.addVaccination(testPet1.getId(), request);
        assertThat(response).isNotNull();
        assertThat(response.getVaccineName()).isEqualTo(" 종합백신1차 ");
        assertThat(response.getPetId()).isEqualTo(testPet1.getId());
        assertThat(response.isCompleted()).isFalse();
    }

    @Test
    @DisplayName("존재하지 않는 반려동물에 백신 추가 시 PetNotFoundException 발생")
    void addVaccination_fail_petNotFound() {
        Long nonExistentPetId = 9999L;
        VaccinationDto.VaccinationRequest request = VaccinationDto.VaccinationRequest.builder().vaccineName("Test").vaccinationDate(LocalDate.now()).build();
        // 서비스의 addVaccination은 PetNotFoundException을 던지도록 수정되었다고 가정 (또는 RuntimeException)
        assertThatThrownBy(() -> vaccinationService.addVaccination(nonExistentPetId, request))
            .isInstanceOf(PetNotFoundException.class) // PetNotFoundException으로 변경되었는지 확인
            .hasMessageContaining("Pet not found with id: " + nonExistentPetId);
    }

    @Test
    @DisplayName("petId로 백신 목록 조회 - 데이터 없음")
    void getVaccinations_byPetId_empty() {
        VaccinationDto.VaccinationListResponse response = vaccinationService.getVaccinations(testPet1.getId(), null, null);
        assertThat(response.getVaccinations()).isEmpty();
        assertThat(response.getTotal()).isZero();
    }

    @Test
    @DisplayName("petId로 백신 목록 조회 - 여러 건 및 필터링 (완료된 것)")
    void getVaccinations_byPetId_completedOnly() {
        createAndSaveVaccination(testPet1, "V1", LocalDate.now().minusDays(10), null, true);
        createAndSaveVaccination(testPet1, "V2", LocalDate.now(), LocalDate.now().plusDays(20), false);
        VaccinationDto.VaccinationListResponse response = vaccinationService.getVaccinations(testPet1.getId(), true, null);
        assertThat(response.getVaccinations()).hasSize(1);
        assertThat(response.getVaccinations().get(0).isCompleted()).isTrue();
    }

    @Test
    @DisplayName("petId로 백신 목록 조회 - 필터링 (예정된 것)")
    void getVaccinations_byPetId_upcomingOnly() {
        createAndSaveVaccination(testPet1, "V1", LocalDate.now().minusDays(10), null, true); // 완료
        createAndSaveVaccination(testPet1, "V2_Upcoming", LocalDate.now().plusDays(5), LocalDate.now().plusMonths(1), false); // 예정
        VaccinationDto.VaccinationListResponse response = vaccinationService.getVaccinations(testPet1.getId(), null, true);
        assertThat(response.getVaccinations()).hasSize(1);
        assertThat(response.getVaccinations().get(0).getVaccineName()).isEqualTo("V2_Upcoming");
    }

    @Test
    @DisplayName("userId로 백신 목록 조회 - 여러 반려동물, 여러 백신")
    void getVaccinationsByUserId_multiplePetsAndVaccinations() {
        createAndSaveVaccination(testPet1, "P1_V1", LocalDate.now().minusDays(5), null, true);
        createAndSaveVaccination(testPet1, "P1_V2_Upcoming", LocalDate.now().plusDays(10), null, false);
        createAndSaveVaccination(testPet2, "P2_V1", LocalDate.now().minusDays(2), null, false);
        VaccinationDto.VaccinationListResponse response = vaccinationService.getVaccinationsByUserId(testOwner.getId(), null, null);
        assertThat(response.getVaccinations()).hasSize(3);
    }

    @Test
    @DisplayName("userId로 백신 목록 조회 - 필터링 (완료된 것, false)")
    void getVaccinationsByUserId_notCompleted() {
        createAndSaveVaccination(testPet1, "P1_V1", LocalDate.now().minusDays(5), null, true);
        createAndSaveVaccination(testPet1, "P1_V2_NotCompleted", LocalDate.now().plusDays(10), null, false);
        createAndSaveVaccination(testPet2, "P2_V1_NotCompleted", LocalDate.now().minusDays(2), null, false);
        VaccinationDto.VaccinationListResponse response = vaccinationService.getVaccinationsByUserId(testOwner.getId(), false, null);
        assertThat(response.getVaccinations()).hasSize(2);
        response.getVaccinations().forEach(v -> assertThat(v.isCompleted()).isFalse());
    }

    @Test
    @DisplayName("백신 기록 삭제 성공")
    void deleteVaccination_success() {
        VaccinationEntity v = createAndSaveVaccination(testPet1, "ToDelete", LocalDate.now(), null, false);
        vaccinationService.deleteVaccination(testPet1.getId(), v.getId());
        assertThat(vaccinationRepository.findById(v.getId())).isEmpty();
    }

    @Test
    @DisplayName("백신 완료 상태 변경 성공")
    void completeVaccination_success() {
        VaccinationEntity v = createAndSaveVaccination(testPet1, "ToComplete", LocalDate.now(), null, false);
        VaccinationDto.VaccinationResponse response = vaccinationService.completeVaccination(testPet1.getId(), v.getId(), true);
        assertThat(response.isCompleted()).isTrue();
        assertThat(vaccinationRepository.findById(v.getId()).get().isCompleted()).isTrue();
    }
}
