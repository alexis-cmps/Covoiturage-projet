package com.ecole.covoiturage.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDTO {

    private Long id;

    private TrajetDTO trajet;

    private StudentDTO passager;

    private LocalDateTime dateReservation;

    private String statut;
}

