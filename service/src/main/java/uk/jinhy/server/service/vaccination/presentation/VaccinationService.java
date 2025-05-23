package uk.jinhy.server.service.vaccination.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.jinhy.server.api.vaccination.presentation.VaccinationDto;
import uk.jinhy.server.service.domain.VaccinationEntity;
import uk.jinhy.server.service.pet.domain.PetEntity;
import uk.jinhy.server.service.pet.domain.PetRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VaccinationService {
    private final VaccinationRepository vaccinationRepository;
    private final PetRepository petRepository;

    @Transactional
    public VaccinationDto.VaccinationResponse addVaccination(Long petId, VaccinationDto.VaccinationRequest request) {
        PetEntity pet = petRepository.findById(petId)
            .orElseThrow(() -> new RuntimeException("Pet not found with id: " + petId));

        VaccinationEntity vaccination = VaccinationEntity.builder()
            .pet(pet)
            .vaccineName(request.getVaccineName())
            .vaccinationDate(request.getVaccinationDate())
            .nextVaccinationDate(request.getNextVaccinationDate())
            .isCompleted(false)
            .build();

        VaccinationEntity savedVaccination = vaccinationRepository.save(vaccination);
        return VaccinationMapper.INSTANCE.fromEntity(savedVaccination);
    }

    public VaccinationDto.VaccinationListResponse getVaccinations(Long petId, Boolean completed, Boolean upcoming) {
        // Pet 존재 확인( 404 Not Found )
        if (!petRepository.existsById(petId)) {
            throw new PetNotFoundException(petId);
        }

        List<VaccinationEntity> filteredVaccinations = vaccinationRepository.findByPetId(petId);

        if (completed != null) {
            filteredVaccinations = filteredVaccinations.stream()
                .filter(v -> v.isCompleted() == completed) // VaccinationEntity의 isCompleted() 사용
                .collect(Collectors.toList());
        }

        if (upcoming != null && upcoming) {
            filteredVaccinations = filteredVaccinations.stream()
                .filter(v -> v.getNextVaccinationDate() != null &&
                    v.getNextVaccinationDate().isAfter(LocalDate.now().minusDays(1)) && // 오늘 날짜도 포함하기 위해 minusDays(1) 사용 (또는 .now()로 변경하여 내일부터)
                    !v.isCompleted()) // VaccinationEntity의 isCompleted() 사용
                .collect(Collectors.toList());
        }

        List<VaccinationDto.VaccinationResponse> vaccinationResponses = filteredVaccinations.stream()
            .map(VaccinationMapper.INSTANCE::fromEntity) // VaccinationMapper가 올바르게 설정되어 있다고 가정
            .collect(Collectors.toList());

        return VaccinationDto.VaccinationListResponse.builder()
            .vaccinations(vaccinationResponses)
            .total(vaccinationResponses.size())
            .build();
    }

    @Transactional
    public void deleteVaccination(Long petId, Long vaccinationId) {
        VaccinationEntity vaccination = vaccinationRepository.findById(vaccinationId)
            .orElseThrow(() -> new RuntimeException("Vaccination not found with id: " + vaccinationId));

        if (!vaccination.getPet().getId().equals(petId)) {
            throw new RuntimeException("Vaccination does not belong to the specified pet");
        }

        vaccinationRepository.delete(vaccination);
    }

    @Transactional
    public VaccinationDto.VaccinationResponse completeVaccination(Long petId, Long vaccinationId, boolean completed) {
        VaccinationEntity vaccination = vaccinationRepository.findById(vaccinationId)
            .orElseThrow(() -> new RuntimeException("Vaccination not found with id: " + vaccinationId));

        if (!vaccination.getPet().getId().equals(petId)) {
            throw new RuntimeException("Vaccination does not belong to the specified pet");
        }

        vaccination.setIsCompleted(completed);
        VaccinationEntity updatedVaccination = vaccinationRepository.save(vaccination);
        return VaccinationMapper.INSTANCE.fromEntity(updatedVaccination);
    }

    // 특정 사용자의 모든 반려동물 백신 조회
    public VaccinationDto.VaccinationListResponse getVaccinationsByUserId(Long userId, Boolean completed, Boolean upcoming) {
        List<VaccinationEntity> filteredVaccinations = vaccinationRepository.findByPetOwnerId(userId);

        if (completed != null) {
            filteredVaccinations = filteredVaccinations.stream()
                .filter(v -> v.isCompleted() == completed)
                .collect(Collectors.toList());
        }

        if (upcoming != null && upcoming) {
            filteredVaccinations = filteredVaccinations.stream()
                .filter(v -> v.getNextVaccinationDate() != null &&
                    v.getNextVaccinationDate().isAfter(LocalDate.now().minusDays(1)) &&
                    !v.isCompleted())
                .collect(Collectors.toList());
        }

        List<VaccinationDto.VaccinationResponse> vaccinationResponses = filteredVaccinations.stream()
            .map(VaccinationMapper.INSTANCE::fromEntity)
            .collect(Collectors.toList());

        return VaccinationDto.VaccinationListResponse.builder()
            .vaccinations(vaccinationResponses)
            .total(vaccinationResponses.size())
            .build();
    }
}
