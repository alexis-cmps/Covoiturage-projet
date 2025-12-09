package com.ecole.covoiturage.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class TrajetDTO {

    private Long id;

    @NotBlank(message = "Le lieu de départ est obligatoire")
    private String depart;

    @NotBlank(message = "La destination est obligatoire")
    private String destination;

    @NotNull(message = "La date et l'heure sont obligatoires")
    @Future(message = "La date doit être dans le futur")
    private LocalDateTime dateHeure;

    @Min(value = 1, message = "Le nombre de places doit être au moins 1")
    @Max(value = 8, message = "Le nombre de places ne peut pas dépasser 8")
    private int nbPlaces;

    private StudentDTO conducteur;

    private Integer nbReservations;

    public TrajetDTO() {
    }

    public TrajetDTO(Long id, String depart, String destination, LocalDateTime dateHeure, int nbPlaces,
            StudentDTO conducteur, Integer nbReservations) {
        this.id = id;
        this.depart = depart;
        this.destination = destination;
        this.dateHeure = dateHeure;
        this.nbPlaces = nbPlaces;
        this.conducteur = conducteur;
        this.nbReservations = nbReservations;
    }

    // Builder pattern
    public static TrajetDTOBuilder builder() {
        return new TrajetDTOBuilder();
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

    public StudentDTO getConducteur() {
        return conducteur;
    }

    public Integer getNbReservations() {
        return nbReservations;
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

    public void setConducteur(StudentDTO conducteur) {
        this.conducteur = conducteur;
    }

    public void setNbReservations(Integer nbReservations) {
        this.nbReservations = nbReservations;
    }

    public static class TrajetDTOBuilder {
        private Long id;
        private String depart;
        private String destination;
        private LocalDateTime dateHeure;
        private int nbPlaces;
        private StudentDTO conducteur;
        private Integer nbReservations;

        public TrajetDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TrajetDTOBuilder depart(String depart) {
            this.depart = depart;
            return this;
        }

        public TrajetDTOBuilder destination(String destination) {
            this.destination = destination;
            return this;
        }

        public TrajetDTOBuilder dateHeure(LocalDateTime dateHeure) {
            this.dateHeure = dateHeure;
            return this;
        }

        public TrajetDTOBuilder nbPlaces(int nbPlaces) {
            this.nbPlaces = nbPlaces;
            return this;
        }

        public TrajetDTOBuilder conducteur(StudentDTO conducteur) {
            this.conducteur = conducteur;
            return this;
        }

        public TrajetDTOBuilder nbReservations(Integer nbReservations) {
            this.nbReservations = nbReservations;
            return this;
        }

        public TrajetDTO build() {
            return new TrajetDTO(id, depart, destination, dateHeure, nbPlaces, conducteur, nbReservations);
        }
    }
}
