package uk.jinhy.server.service.hospital;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.jinhy.server.service.domain.HospitalEntity;
import uk.jinhy.server.service.domain.HospitalReservationEntity;
import uk.jinhy.server.service.domain.PetFactory;
import uk.jinhy.server.service.domain.UserFactory;

import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

class HospitalEntityTest {
    @DisplayName("병원에 예약을 추가할 수 있다.")
    @Test
    void 병원_예약_추가() {
        // given
        HospitalEntity sut = HospitalEntity.builder()
            .name("테스트 동물병원")
            .address("서울시 테스트구")
            .rating(4.5)
            .build();

        HospitalReservationEntity reservation = HospitalReservationEntity.builder()
            .user(UserFactory.create().build())
            .pet(PetFactory.create().build())
            .hospital(sut)
            .reservationDateTime(LocalDateTime.now())
            .status(HospitalReservationEntity.ReservationStatus.PENDING)
            .build();

        // when
        HospitalReservationEntity result = sut.addReservation(reservation);

        // then
        assertThat(result).isEqualTo(reservation);
        assertThat(sut.getReservations()).contains(reservation);
    }
}

