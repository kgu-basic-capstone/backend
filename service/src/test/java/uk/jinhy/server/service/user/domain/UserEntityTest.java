package uk.jinhy.server.service.user.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.jinhy.server.service.domain.PetEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class UserEntityTest {

    private UserEntity userEntity;
    private static final String TEST_OAUTH2_USER_ID = "oauth2-user-123";
    private static final String TEST_USERNAME = "test-user";
    private static final String NEW_USERNAME = "new-username";

    @BeforeEach
    void setUp() {
        userEntity = UserEntity.builder()
            .oauth2UserId(TEST_OAUTH2_USER_ID)
            .username(TEST_USERNAME)
            .build();
    }

    @Test
    void builder_ShouldCreateUserWithCorrectFields() {
        // Then
        assertEquals(TEST_OAUTH2_USER_ID, userEntity.getOauth2UserId());
        assertEquals(TEST_USERNAME, userEntity.getUsername());
        assertNotNull(userEntity.getPets());
        assertTrue(userEntity.getPets().isEmpty());
    }

    @Test
    void updateUsername_ShouldChangeUsername() {
        // When
        userEntity.updateUsername(NEW_USERNAME);

        // Then
        assertEquals(NEW_USERNAME, userEntity.getUsername());
    }

    @Test
    void enrollPet_SinglePet_ShouldAddPetToList() {
        // Given
        PetEntity mockPet = mock(PetEntity.class);

        // When
        List<PetEntity> result = userEntity.enrollPet(mockPet);

        // Then
        assertEquals(1, result.size());
        assertTrue(result.contains(mockPet));
        assertEquals(1, userEntity.getPets().size());
        assertTrue(userEntity.getPets().contains(mockPet));
    }

    @Test
    void enrollPet_MultiplePets_ShouldAddAllPetsToList() {
        // Given
        PetEntity mockPet1 = mock(PetEntity.class);
        PetEntity mockPet2 = mock(PetEntity.class);
        List<PetEntity> petsToAdd = List.of(mockPet1, mockPet2);

        // When
        List<PetEntity> result = userEntity.enrollPet(petsToAdd);

        // Then
        assertEquals(2, result.size());
        assertTrue(result.contains(mockPet1));
        assertTrue(result.contains(mockPet2));
        assertEquals(2, userEntity.getPets().size());
        assertTrue(userEntity.getPets().contains(mockPet1));
        assertTrue(userEntity.getPets().contains(mockPet2));
    }

    @Test
    void removePet_ExistingPet_ShouldRemovePetFromList() {
        // Given
        PetEntity mockPet = mock(PetEntity.class);
        userEntity.enrollPet(mockPet);
        assertEquals(1, userEntity.getPets().size());

        // When
        List<PetEntity> result = userEntity.removePet(mockPet);

        // Then
        assertEquals(0, result.size());
        assertEquals(0, userEntity.getPets().size());
    }

    @Test
    void removePet_NonExistingPet_ShouldThrowException() {
        // Given
        PetEntity mockPet = mock(PetEntity.class);

        // When/Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userEntity.removePet(mockPet);
        });
        assertEquals("Pet does not exist", exception.getMessage());
    }
}
