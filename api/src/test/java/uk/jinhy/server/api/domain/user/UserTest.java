package uk.jinhy.server.api.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.jinhy.server.api.domain.pet.Pet;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @DisplayName("회원은 자신의 이름을 수정할 수 있다.")
    @Test
    void 회원_이름_수정() {
        //given
        User sut = User.builder()
            .username("test")
            .email("test@test.com")
            .build();

        String newName = "newName";

        //when
        sut.updateUsername(newName);

        //then
        assertThat(sut.getUsername()).isEqualTo(newName);
    }

    @DisplayName("회원은 자신의 반려동물을 한 마리씩 등록할 수 있다.")
    @Test
    void 반려동물_한_마리_등록() {
        //given
        User sut = User.builder()
            .username("testUser")
            .email("test@test.com")
            .build();

        Pet pet = Pet.builder()
            .owner(sut)
            .name("testPet")
            .build();

        //when
        List<Pet> enrolledPets = sut.enrollPet(pet);

        //then
        assertThat(enrolledPets)
            .containsOnlyOnce(pet);
    }

    @DisplayName("회원은 자신의 반려동물을 여러 마리씩 등록할 수 있다.")
    @Test
    void 반려동물_여러_마리_등록() {
        //given
        User sut = User.builder()
            .username("testUser")
            .email("test@test.com")
            .build();

        Pet pet1 = Pet.builder()
            .owner(sut)
            .name("testPet1")
            .build();

        Pet pet2 = Pet.builder()
            .owner(sut)
            .name("testPet2")
            .build();

        List<Pet> pets = List.of(pet1, pet2);

        //when
        List<Pet> enrolledPets = sut.enrollPet(pets);

        //then
        assertThat(enrolledPets)
            .containsExactlyInAnyOrder(pet1, pet2);
    }

    @DisplayName("회원은 등록된 반려동물을 삭제할 수 있다.")
    @Test
    void 반려동물_삭제() {
        //given
        User sut = User.builder()
            .username("testUser")
            .email("test@test.com")
            .build();

        Pet pet = Pet.builder()
            .owner(sut)
            .name("testPet")
            .build();

        sut.getPets().add(pet);

        //when
        List<Pet> enrolledPets = sut.removePet(pet);

        //then
        assertThat(enrolledPets)
            .doesNotContain(pet);
    }

    @DisplayName("회원이 등록되지 않은 반려동물을 삭제할 시 예외가 발생한다.")
    @Test
    void 존재하지_않는_반려동물_삭제() {
        //given
        User sut = User.builder()
            .username("testUser")
            .email("test@test.com")
            .build();

        Pet pet = Pet.builder()
            .owner(sut)
            .name("testPet")
            .build();

        //then & when
        assertThatThrownBy(() -> sut.removePet(pet))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Pet does not exist");
    }

}
