package uk.jinhy.server.api.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

class HospitalTest {

    @DisplayName("병원에 예약을 추가할 수 있다.")
    @Test
    void 병원_예약_추가() {
        // given
        Hospital sut = Hospital.builder()
            .name("테스트 동물병원")
            .address("서울시 테스트구")
            .rating(4.5)
            .build();

        HospitalReservation reservation = HospitalReservation.builder()
            .user(UserFactory.create().build())
            .pet(PetFactory.create().build())
            .hospital(sut)
            .reservationDateTime(LocalDateTime.now())
            .status(HospitalReservation.ReservationStatus.PENDING)
            .build();

        // when
        HospitalReservation result = sut.addReservation(reservation);

        // then
        assertThat(result).isEqualTo(reservation);
        assertThat(sut.getReservations()).contains(reservation);
    }
}
