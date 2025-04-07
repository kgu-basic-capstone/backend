package uk.jinhy.server.service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "username"),
    @UniqueConstraint(columnNames = "email")
})
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PetEntity> pets = new ArrayList<>();

    public void updateUsername(String username) {
        this.username = username;
    }

    public List<PetEntity> enrollPet(PetEntity pet) {
        pets.add(pet);
        return List.copyOf(pets);
    }

    public List<PetEntity> enrollPet(List<PetEntity> newPets) {
        pets.addAll(newPets);
        return List.copyOf(pets);
    }

    public List<PetEntity> removePet(PetEntity pet) {
        if (pets.contains(pet)) {
            pets.remove(pet);
            return List.copyOf(pets);
        }
        throw new IllegalArgumentException("Pet does not exist");
    }
}
