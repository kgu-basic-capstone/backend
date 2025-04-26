package uk.jinhy.server.api.domain;

import lombok.Builder;
import lombok.Getter;
import uk.jinhy.server.api.user.domain.User;

import java.time.LocalDateTime;

@Getter
public class HospitalReservation {
    private Long id;
    private User user;
    private Pet pet;
    private Hospital hospital;
    private LocalDateTime reservationDateTime;
    private ReservationStatus status;

    @Builder
    private HospitalReservation(User user, Pet pet, Hospital hospital,
                                LocalDateTime reservationDateTime,
                                ReservationStatus status) {
        this.user = user;
        this.pet = pet;
        this.hospital = hospital;
        this.reservationDateTime = reservationDateTime;
        this.status = status;
    }

    public void changeStatus(ReservationStatus newStatus) {
        this.status = newStatus;
    }

    public enum ReservationStatus {
        PENDING, CONFIRMED, CANCELLED
    }
}
