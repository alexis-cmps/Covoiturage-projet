package com.ecole.covoiturage.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Trajet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String depart;
    private String destination;
    private LocalDateTime dateHeure;
    private int nbPlaces;

    @ManyToOne
    @JoinColumn(name = "conducteur_id")
    private Student conducteur;

    @JsonIgnore
    @OneToMany(mappedBy = "trajet", cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    public Trajet() {
    }

    public Trajet(Long id, String depart, String destination, LocalDateTime dateHeure, int nbPlaces, Student conducteur,
            List<Reservation> reservations) {
        this.id = id;
        this.depart = depart;
        this.destination = destination;
        this.dateHeure = dateHeure;
        this.nbPlaces = nbPlaces;
        this.conducteur = conducteur;
        this.reservations = reservations;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getDepart() {
        return depart;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    public int getNbPlaces() {
        return nbPlaces;
    }

    public Student getConducteur() {
        return conducteur;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDateHeure(LocalDateTime dateHeure) {
        this.dateHeure = dateHeure;
    }

    public void setNbPlaces(int nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    public void setConducteur(Student conducteur) {
        this.conducteur = conducteur;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
