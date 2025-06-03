package uk.jinhy.server.api.hospital.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Hospital {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private Double rating;
    private Boolean surgeryAvailable;
    private String operatingHours;
    private Double latitude;
    private Double longitude;
    private List<HospitalReservation> reservations;

    @Builder
    private Hospital(String name, String address, String phoneNumber,
                     Double rating, Boolean surgeryAvailable,
                     String operatingHours, Double latitude, Double longitude) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.rating = rating;
        this.surgeryAvailable = surgeryAvailable;
        this.operatingHours = operatingHours;
        this.latitude = latitude;
        this.longitude = longitude;
        this.reservations = new ArrayList<>();
    }

    public HospitalReservation addReservation(HospitalReservation reservation) {
        this.reservations.add(reservation);
        return reservation;
    }
}
