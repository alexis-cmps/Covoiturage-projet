package com.ecole.covoiturage.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    // Un étudiant peut être conducteur de plusieurs trajets
    @OneToMany(mappedBy = "conducteur", cascade = CascadeType.ALL)
    private List<Trajet> trajets;

    // Un étudiant peut avoir plusieurs réservations (en tant que passager)
    @OneToMany(mappedBy = "passager", cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    public Student(Long id, String name, String email) { this.id = id; this.name = name; this.email = email; }
}

