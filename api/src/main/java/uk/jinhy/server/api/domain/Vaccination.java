package uk.jinhy.server.api.domain;

import lombok.Builder;
import lombok.Getter;
import uk.jinhy.server.api.pet.domain.Pet;

import java.time.LocalDate;

@Getter
public class Vaccination {
    private Long id;
    private Pet pet;
    private String vaccineName;
    private LocalDate vaccinationDate;
    private LocalDate nextVaccinationDate;
    private Boolean isCompleted;

    @Builder
    private Vaccination(Pet pet, String vaccineName,
                        LocalDate vaccinationDate,
                        LocalDate nextVaccinationDate,
                        Boolean isCompleted) {
        this.pet = pet;
        this.vaccineName = vaccineName;
        this.vaccinationDate = vaccinationDate;
        this.nextVaccinationDate = nextVaccinationDate;
        this.isCompleted = isCompleted;
    }
}
