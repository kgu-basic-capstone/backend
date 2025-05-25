package uk.jinhy.server.service.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class HospitalReservationEntityTest {
    @DisplayName("예약 상태를 변경할 수 있다.")
    @Test
    void 예약_상태_변경() {
        // given
        HospitalReservationEntity sut = HospitalReservationEntity.builder()
            .user(UserFactory.create().build())
            .pet(PetFactory.create().build())
            .hospital(HospitalEntity.builder().name("테스트 병원").build())
            .reservationDateTime(LocalDateTime.now())
            .status(HospitalReservationEntity.ReservationStatus.PENDING)
            .build();

        // when
        sut.changeStatus(HospitalReservationEntity.ReservationStatus.CONFIRMED);

        // then
        assertThat(sut.getStatus()).isEqualTo(HospitalReservationEntity.ReservationStatus.CONFIRMED);
    }
}
