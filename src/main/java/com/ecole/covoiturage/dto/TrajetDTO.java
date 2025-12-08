package com.ecole.covoiturage.dto;

import lombok.*;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}

