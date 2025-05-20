package uk.jinhy.server.service.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.jinhy.server.service.user.domain.UserEntity;
import uk.jinhy.server.service.pet.domain.PetEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "hospital_reservations")
public class HospitalReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private PetEntity pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private HospitalEntity hospital;

    @Column(nullable = false)
    private LocalDateTime reservationDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @Builder
    public HospitalReservationEntity(UserEntity user, PetEntity pet, HospitalEntity hospital,
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
