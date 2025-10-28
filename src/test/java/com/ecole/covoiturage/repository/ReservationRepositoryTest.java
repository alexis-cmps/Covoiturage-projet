package com.ecole.covoiturage.repository;

import com.ecole.covoiturage.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TrajetRepository trajetRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void shouldSaveAndFindReservation() {
        Student conducteur = new Student();
        conducteur.setName("Paul");
        conducteur.setEmail("paul@etu.fr");
        studentRepository.save(conducteur);

        Student passager = new Student();
        passager.setName("Marie");
        passager.setEmail("marie@etu.fr");
        studentRepository.save(passager);

        Trajet trajet = new Trajet();
        trajet.setDepart("Nantes");
        trajet.setDestination("Bordeaux");
        trajet.setDateHeure(LocalDateTime.now().plusDays(2));
        trajet.setNbPlaces(2);
        trajet.setConducteur(conducteur);
        trajetRepository.save(trajet);

        Reservation reservation = new Reservation();
        reservation.setDateReservation(LocalDateTime.now());
        reservation.setTrajet(trajet);
        reservation.setPassager(passager);
        reservationRepository.save(reservation);

        assertThat(reservationRepository.findAll()).hasSize(1);
        assertThat(reservationRepository.findAll().get(0).getPassager().getName())
                .isEqualTo("Marie");
    }
}
