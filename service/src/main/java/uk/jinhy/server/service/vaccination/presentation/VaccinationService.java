package uk.jinhy.server.service.vaccination.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.jinhy.server.api.vaccination.presentation.VaccinationDto;
import uk.jinhy.server.service.domain.PetEntity;
import uk.jinhy.server.service.domain.VaccinationEntity;
import uk.jinhy.server.service.pet.presentation.PetRepository;

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
        // Pet 존재 확인
        if (!petRepository.existsById(petId)) {
            throw new RuntimeException("Pet not found with id: " + petId);
        }

        List<VaccinationEntity> vaccinations;

        if (completed != null && completed) {
            // 완료된 백신만 조회
            vaccinations = vaccinationRepository.findByPetIdAndIsCompleted(petId, true);
        }
        if (upcoming != null && upcoming) {
            // 예정된 백신만 조회
            vaccinations = vaccinationRepository.findByPetIdAndVaccinationDateAfter(petId, LocalDate.now());
        } else {
            // 모든 백신 조회
            vaccinations = vaccinationRepository.findByPetId(petId);
        }

        List<VaccinationDto.VaccinationResponse> vaccinationResponses = vaccinations.stream()
            .map(VaccinationMapper.INSTANCE::fromEntity)
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
        List<VaccinationEntity> vaccinations;

        if (completed != null && completed) {
            vaccinations = vaccinationRepository.findByPetOwnerIdAndIsCompleted(userId, true);
        }
        if (upcoming != null && upcoming) {
            vaccinations = vaccinationRepository.findByPetOwnerIdAndVaccinationDateAfter(userId, LocalDate.now());
        } else {
            vaccinations = vaccinationRepository.findByPetOwnerId(userId);
        }

        List<VaccinationDto.VaccinationResponse> vaccinationResponses = vaccinations.stream()
            .map(VaccinationMapper.INSTANCE::fromEntity)
            .collect(Collectors.toList());

        return VaccinationDto.VaccinationListResponse.builder()
            .vaccinations(vaccinationResponses)
            .total(vaccinationResponses.size())
            .build();
    }
}
