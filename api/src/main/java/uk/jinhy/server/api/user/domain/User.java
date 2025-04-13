package uk.jinhy.server.api.user.domain;

import lombok.Builder;
import lombok.Getter;
import uk.jinhy.server.api.domain.Pet;

import java.util.ArrayList;
import java.util.List;

@Getter
public class User {

    private Long id;
    private String username;
    private String email;
    private List<Pet> pets;

    @Builder
    private User(String username, String email) {
        this.username = username;
        this.email = email;
        this.pets = new ArrayList<>();
    }

    public void updateUsername(String username) {
        this.username = username;
    }

    public List<Pet> enrollPet(Pet pet) {
        pets.add(pet);
        return List.copyOf(pets);
    }

    public List<Pet> enrollPet(List<Pet> pet) {
        pets.addAll(pet);
        return List.copyOf(pets);
    }

    public List<Pet> removePet(Pet pet) {
        if (pets.contains(pet)) {
            pets.remove(pet);
            return  List.copyOf(pets);
        }

        throw new IllegalArgumentException("Pet does not exist");
    }
}
