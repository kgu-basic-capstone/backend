package uk.jinhy.server.service.vaccination.presentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import uk.jinhy.server.api.vaccination.presentation.VaccinationDto;
import uk.jinhy.server.service.pet.domain.PetEntity;
import uk.jinhy.server.service.pet.domain.PetRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class VaccinationServiceTest {
    @Mock
    private VaccinationRepository vaccinationRepository;

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private VaccinationService vaccinationService;

    @Test
    @DisplayName("백신 추가 성공")
    void addVaccination_success() {
        Long petId = 1L;
        PetEntity pet = PetEntity.builder().id(petId).build();

        VaccinationDto.VaccinationRequest request = VaccinationDto.VaccinationRequest.builder()
            .vaccineName("코로나")
            .vaccinationDate(LocalDate.now())
            .nextVaccinationDate(LocalDate.now().plusMonths(12))
            .build();

        when(petRepository.findById(any())).thenReturn(Optional.of(pet));
        when(vaccinationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        VaccinationDto.VaccinationResponse response = vaccinationService.addVaccination(petId, request);

        assertNotNull(response);
        assertEquals("코로나", response.getVaccineName());
        assertFalse(response.isCompleted());
    }

    @Test
    @DisplayName("백신 추가 실패 - 반려동물 없음")
    void addVaccination_petNotFound() {
        Long petId = 999L;

        VaccinationDto.VaccinationRequest request = VaccinationDto.VaccinationRequest.builder()
            .vaccineName("코로나")
            .vaccinationDate(LocalDate.now())
            .nextVaccinationDate(LocalDate.now().plusMonths(12))
            .build();

        when(petRepository.findById(petId)).thenReturn(Optional.empty());

        RuntimeException e = assertThrows(RuntimeException.class, () ->
            vaccinationService.addVaccination(petId, request));

        assertEquals("Pet not found with id: 999", e.getMessage());
    }

}
