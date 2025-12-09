package com.ecole.covoiturage.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateReservation;

    private String statut; // PENDING, CONFIRMED, CANCELLED

    @ManyToOne
    @JoinColumn(name = "trajet_id")
    private Trajet trajet;

    @ManyToOne
    @JoinColumn(name = "passager_id")
    private Student passager;

    public Reservation() {
    }

    public Reservation(Long id, LocalDateTime dateReservation, String statut, Trajet trajet, Student passager) {
        this.id = id;
        this.dateReservation = dateReservation;
        this.statut = statut;
        this.trajet = trajet;
        this.passager = passager;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public LocalDateTime getDateReservation() {
        return dateReservation;
    }

    public String getStatut() {
        return statut;
    }

    public Trajet getTrajet() {
        return trajet;
    }

    public Student getPassager() {
        return passager;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setDateReservation(LocalDateTime dateReservation) {
        this.dateReservation = dateReservation;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
    }

    public void setPassager(Student passager) {
        this.passager = passager;
    }
}
