package com.ecole.covoiturage.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    // Un etudiant peut etre conducteur de plusieurs trajets
    @JsonIgnore
    @OneToMany(mappedBy = "conducteur", cascade = CascadeType.ALL)
    private List<Trajet> trajets;

    // Un etudiant peut avoir plusieurs reservations (en tant que passager)
    @JsonIgnore
    @OneToMany(mappedBy = "passager", cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    public Student() {
    }

    public Student(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Student(Long id, String name, String email, String password, List<Trajet> trajets,
            List<Reservation> reservations) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.trajets = trajets;
        this.reservations = reservations;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<Trajet> getTrajets() {
        return trajets;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTrajets(List<Trajet> trajets) {
        this.trajets = trajets;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
