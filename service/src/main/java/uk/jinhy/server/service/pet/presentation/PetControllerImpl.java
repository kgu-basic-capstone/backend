package uk.jinhy.server.service.pet.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uk.jinhy.server.api.pet.presentation.PetController;
import uk.jinhy.server.api.pet.presentation.PetDto.HealthRecordRequest;
import uk.jinhy.server.api.pet.presentation.PetDto.HealthRecordResponse;
import uk.jinhy.server.api.pet.presentation.PetDto.PetCreateRequest;
import uk.jinhy.server.api.pet.presentation.PetDto.PetDetailResponse;
import uk.jinhy.server.api.pet.presentation.PetDto.PetListResponse;
import uk.jinhy.server.api.pet.presentation.PetDto.PetDetailResponse.OwnerInfo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PetControllerImpl implements PetController {

    @Override
    public ResponseEntity<PetDetailResponse> createPet(PetCreateRequest request) {
        PetDetailResponse mockResponse = PetDetailResponse.builder()
            .id(1L)
            .name(request.getName())
            .birthDate(request.getBirthDate())
            .owner(OwnerInfo.builder()
                .id(1L)
                .username("testUser")
                .email("test@example.com")
                .build())
            .healthRecords(Arrays.asList(
                HealthRecordResponse.builder()
                    .id(1L)
                    .checkDate(LocalDateTime.now().minusDays(30))
                    .weight(5.2)
                    .notes("첫 건강 검진")
                    .build()
            ))
            .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(mockResponse);
    }

    @Override
    public ResponseEntity<PetListResponse> getPets() {
        List<PetDetailResponse> mockPets = Arrays.asList(
            PetDetailResponse.builder()
                .id(1L)
                .name("멍멍이")
                .birthDate(LocalDate.now().minusYears(2))
                .owner(OwnerInfo.builder()
                    .id(1L)
                    .username("testUser")
                    .email("test@example.com")
                    .build())
                .build(),
            PetDetailResponse.builder()
                .id(2L)
                .name("야옹이")
                .birthDate(LocalDate.now().minusYears(1))
                .owner(OwnerInfo.builder()
                    .id(1L)
                    .username("testUser")
                    .email("test@example.com")
                    .build())
                .build()
        );

        PetListResponse response = PetListResponse.builder()
            .pets(mockPets)
            .total(mockPets.size())
            .build();

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<PetDetailResponse> getPet(Long petId) {
        PetDetailResponse mockResponse = PetDetailResponse.builder()
            .id(petId)
            .name("멍멍이")
            .birthDate(LocalDate.now().minusYears(2))
            .owner(OwnerInfo.builder()
                .id(1L)
                .username("testUser")
                .email("test@example.com")
                .build())
            .healthRecords(Arrays.asList(
                HealthRecordResponse.builder()
                    .id(1L)
                    .checkDate(LocalDateTime.now().minusDays(30))
                    .weight(5.2)
                    .notes("첫 건강 검진")
                    .build(),
                HealthRecordResponse.builder()
                    .id(2L)
                    .checkDate(LocalDateTime.now().minusDays(15))
                    .weight(5.5)
                    .notes("체중 증가 확인")
                    .build(),
                HealthRecordResponse.builder()
                    .id(3L)
                    .checkDate(LocalDateTime.now().minusDays(1))
                    .weight(5.8)
                    .notes("건강한 상태")
                    .build()
            ))
            .build();

        return ResponseEntity.ok(mockResponse);
    }

    @Override
    public ResponseEntity<Void> deletePet(Long petId) {
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<HealthRecordResponse> addHealthRecord(Long petId, HealthRecordRequest request) {
        HealthRecordResponse mockResponse = HealthRecordResponse.builder()
            .id(1L)
            .checkDate(request.getCheckDate())
            .weight(request.getWeight())
            .notes(request.getNotes())
            .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(mockResponse);
    }

    @Override
    public ResponseEntity<List<HealthRecordResponse>> getHealthRecords(Long petId, LocalDateTime since) {
        List<HealthRecordResponse> mockRecords = Arrays.asList(
            HealthRecordResponse.builder()
                .id(1L)
                .checkDate(LocalDateTime.now().minusDays(30))
                .weight(5.2)
                .notes("첫 건강 검진")
                .build(),
            HealthRecordResponse.builder()
                .id(2L)
                .checkDate(LocalDateTime.now().minusDays(15))
                .weight(5.5)
                .notes("체중 증가 확인")
                .build(),
            HealthRecordResponse.builder()
                .id(3L)
                .checkDate(LocalDateTime.now().minusDays(1))
                .weight(5.8)
                .notes("건강한 상태")
                .build()
        );

        if (since != null) {
            mockRecords = mockRecords.stream()
                .filter(record -> record.getCheckDate().isAfter(since))
                .collect(Collectors.toList());
        }

        return ResponseEntity.ok(mockRecords);
    }

    @Override
    public ResponseEntity<Void> deleteHealthRecord(Long petId, Long recordId) {
        return ResponseEntity.noContent().build();
    }
}
