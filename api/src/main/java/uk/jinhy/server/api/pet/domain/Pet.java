package uk.jinhy.server.api.pet.domain;

import lombok.Builder;
import lombok.Getter;
import uk.jinhy.server.api.user.domain.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Pet {

    private Long id;
    private User owner;
    private String name;
    private LocalDate birthDate;
    private List<HealthRecord> healthRecords;

    @Builder
    private Pet(User owner, String name, LocalDate birthDate) {
        this.owner = owner;
        this.name = name;
        this.birthDate = birthDate;
        this.healthRecords = new ArrayList<>();
    }

}
