package com.ecole.covoiturage.repository;

import com.ecole.covoiturage.entity.Reservation;
import com.ecole.covoiturage.entity.Student;
import com.ecole.covoiturage.entity.Trajet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Tests du Repository Reservation")
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TrajetRepository trajetRepository;

    @Autowired
    private StudentRepository studentRepository;

    private Student conducteur;
    private Student passager;
    private Trajet trajet;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
        trajetRepository.deleteAll();
        studentRepository.deleteAll();

        conducteur = new Student();
        conducteur.setName("Paul");
        conducteur.setEmail("paul@etu.fr");
        conducteur.setPassword("password123");
        conducteur = studentRepository.save(conducteur);

        passager = new Student();
        passager.setName("Marie");
        passager.setEmail("marie@etu.fr");
        passager.setPassword("password456");
        passager = studentRepository.save(passager);

        trajet = new Trajet();
        trajet.setDepart("Nantes");
        trajet.setDestination("Bordeaux");
        trajet.setDateHeure(LocalDateTime.now().plusDays(2));
        trajet.setNbPlaces(2);
        trajet.setConducteur(conducteur);
        trajet = trajetRepository.save(trajet);

        reservation = new Reservation();
        reservation.setDateReservation(LocalDateTime.now());
        reservation.setStatut("PENDING");
        reservation.setTrajet(trajet);
        reservation.setPassager(passager);
    }

    @Test
    @DisplayName("Doit sauvegarder et recuperer une reservation")
    void shouldSaveAndFindReservation() {
        // when
        Reservation saved = reservationRepository.save(reservation);
        List<Reservation> results = reservationRepository.findAll();

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getPassager().getName()).isEqualTo("Marie");
    }

    @Test
    @DisplayName("Doit trouver une reservation par ID")
    void shouldFindReservationById() {
        // given
        Reservation saved = reservationRepository.save(reservation);

        // when
        Optional<Reservation> found = reservationRepository.findById(saved.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getStatut()).isEqualTo("PENDING");
    }

    @Test
    @DisplayName("Doit mettre a jour le statut d une reservation")
    void shouldUpdateReservationStatus() {
        // given
        Reservation saved = reservationRepository.save(reservation);

        // when
        saved.setStatut("CONFIRMED");
        Reservation updated = reservationRepository.save(saved);

        // then
        assertThat(updated.getStatut()).isEqualTo("CONFIRMED");
    }

    @Test
    @DisplayName("Doit supprimer une reservation")
    void shouldDeleteReservation() {
        // given
        Reservation saved = reservationRepository.save(reservation);

        // when
        reservationRepository.deleteById(saved.getId());

        // then
        assertThat(reservationRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("Doit verifier la relation reservation-trajet")
    void shouldVerifyReservationTrajetRelation() {
        // given
        Reservation saved = reservationRepository.save(reservation);

        // when
        Optional<Reservation> found = reservationRepository.findById(saved.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getTrajet()).isNotNull();
        assertThat(found.get().getTrajet().getDepart()).isEqualTo("Nantes");
        assertThat(found.get().getTrajet().getConducteur().getName()).isEqualTo("Paul");
    }

    @Test
    @DisplayName("Doit verifier la relation reservation-passager")
    void shouldVerifyReservationPassagerRelation() {
        // given
        Reservation saved = reservationRepository.save(reservation);

        // when
        Optional<Reservation> found = reservationRepository.findById(saved.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getPassager()).isNotNull();
        assertThat(found.get().getPassager().getEmail()).isEqualTo("marie@etu.fr");
    }

    @Test
    @DisplayName("Doit sauvegarder plusieurs reservations pour un trajet")
    void shouldSaveMultipleReservationsForTrajet() {
        // given
        reservationRepository.save(reservation);

        Student autrePassager = new Student();
        autrePassager.setName("Jean");
        autrePassager.setEmail("jean@etu.fr");
        autrePassager.setPassword("pass");
        autrePassager = studentRepository.save(autrePassager);

        Reservation reservation2 = new Reservation();
        reservation2.setDateReservation(LocalDateTime.now());
        reservation2.setStatut("PENDING");
        reservation2.setTrajet(trajet);
        reservation2.setPassager(autrePassager);
        reservationRepository.save(reservation2);

        // when
        List<Reservation> results = reservationRepository.findAll();

        // then
        assertThat(results).hasSize(2);
    }

    @Test
    @DisplayName("Doit annuler une reservation")
    void shouldCancelReservation() {
        // given
        Reservation saved = reservationRepository.save(reservation);

        // when
        saved.setStatut("CANCELLED");
        Reservation cancelled = reservationRepository.save(saved);

        // then
        assertThat(cancelled.getStatut()).isEqualTo("CANCELLED");
    }

    @Test
    @DisplayName("Doit conserver la date de reservation")
    void shouldPreserveReservationDate() {
        // given
        LocalDateTime now = LocalDateTime.now();
        reservation.setDateReservation(now);
        Reservation saved = reservationRepository.save(reservation);

        // when
        Optional<Reservation> found = reservationRepository.findById(saved.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getDateReservation()).isNotNull();
    }
}
