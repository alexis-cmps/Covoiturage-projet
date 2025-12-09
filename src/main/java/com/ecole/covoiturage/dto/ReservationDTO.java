package com.ecole.covoiturage.dto;

import java.time.LocalDateTime;

public class ReservationDTO {

    private Long id;
    private TrajetDTO trajet;
    private StudentDTO passager;
    private LocalDateTime dateReservation;
    private String statut;

    public ReservationDTO() {
    }

    public ReservationDTO(Long id, TrajetDTO trajet, StudentDTO passager, LocalDateTime dateReservation,
            String statut) {
        this.id = id;
        this.trajet = trajet;
        this.passager = passager;
        this.dateReservation = dateReservation;
        this.statut = statut;
    }

    // Builder pattern
    public static ReservationDTOBuilder builder() {
        return new ReservationDTOBuilder();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public TrajetDTO getTrajet() {
        return trajet;
    }

    public StudentDTO getPassager() {
        return passager;
    }

    public LocalDateTime getDateReservation() {
        return dateReservation;
    }

    public String getStatut() {
        return statut;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setTrajet(TrajetDTO trajet) {
        this.trajet = trajet;
    }

    public void setPassager(StudentDTO passager) {
        this.passager = passager;
    }

    public void setDateReservation(LocalDateTime dateReservation) {
        this.dateReservation = dateReservation;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public static class ReservationDTOBuilder {
        private Long id;
        private TrajetDTO trajet;
        private StudentDTO passager;
        private LocalDateTime dateReservation;
        private String statut;

        public ReservationDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ReservationDTOBuilder trajet(TrajetDTO trajet) {
            this.trajet = trajet;
            return this;
        }

        public ReservationDTOBuilder passager(StudentDTO passager) {
            this.passager = passager;
            return this;
        }

        public ReservationDTOBuilder dateReservation(LocalDateTime dateReservation) {
            this.dateReservation = dateReservation;
            return this;
        }

        public ReservationDTOBuilder statut(String statut) {
            this.statut = statut;
            return this;
        }

        public ReservationDTO build() {
            return new ReservationDTO(id, trajet, passager, dateReservation, statut);
        }
    }
}
