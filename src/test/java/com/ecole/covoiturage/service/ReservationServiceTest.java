package com.ecole.covoiturage.service;

import com.ecole.covoiturage.entity.Reservation;
import com.ecole.covoiturage.entity.Student;
import com.ecole.covoiturage.entity.Trajet;
import com.ecole.covoiturage.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests du Service Reservation")
class ReservationServiceTest {

    @Mock
    private ReservationRepository repository;

    private ReservationService service;

    private Student conducteur;
    private Student passager;
    private Trajet trajet;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        service = new ReservationService(repository);

        conducteur = new Student();
        conducteur.setId(1L);
        conducteur.setName("Paul");
        conducteur.setEmail("paul@etu.fr");

        passager = new Student();
        passager.setId(2L);
        passager.setName("Marie");
        passager.setEmail("marie@etu.fr");

        trajet = new Trajet();
        trajet.setId(1L);
        trajet.setDepart("Nantes");
        trajet.setDestination("Bordeaux");
        trajet.setDateHeure(LocalDateTime.now().plusDays(2));
        trajet.setNbPlaces(2);
        trajet.setConducteur(conducteur);

        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setDateReservation(LocalDateTime.now());
        reservation.setStatut("PENDING");
        reservation.setTrajet(trajet);
        reservation.setPassager(passager);
    }

    @Test
    @DisplayName("Doit retourner toutes les reservations")
    void shouldReturnAllReservations() {
        // given
        when(repository.findAll()).thenReturn(List.of(reservation));

        // when
        List<Reservation> result = service.findAll();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatut()).isEqualTo("PENDING");
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Doit trouver une reservation par ID")
    void shouldFindReservationById() {
        // given
        when(repository.findById(1L)).thenReturn(Optional.of(reservation));

        // when
        Optional<Reservation> result = service.findById(1L);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getPassager().getName()).isEqualTo("Marie");
    }

    @Test
    @DisplayName("Doit retourner vide si reservation non trouvee")
    void shouldReturnEmptyWhenReservationNotFound() {
        // given
        when(repository.findById(999L)).thenReturn(Optional.empty());

        // when
        Optional<Reservation> result = service.findById(999L);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Doit sauvegarder une reservation")
    void shouldSaveReservation() {
        // given
        when(repository.save(any(Reservation.class))).thenReturn(reservation);

        // when
        Reservation result = service.save(reservation);

        // then
        assertThat(result.getStatut()).isEqualTo("PENDING");
        assertThat(result.getTrajet().getDepart()).isEqualTo("Nantes");
        verify(repository, times(1)).save(reservation);
    }

    @Test
    @DisplayName("Doit supprimer une reservation")
    void shouldDeleteReservation() {
        // when
        service.delete(1L);

        // then
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Doit retourner une liste vide si aucune reservation")
    void shouldReturnEmptyListWhenNoReservations() {
        // given
        when(repository.findAll()).thenReturn(List.of());

        // when
        List<Reservation> result = service.findAll();

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Doit sauvegarder une reservation avec statut CONFIRMED")
    void shouldSaveConfirmedReservation() {
        // given
        reservation.setStatut("CONFIRMED");
        when(repository.save(any(Reservation.class))).thenReturn(reservation);

        // when
        Reservation result = service.save(reservation);

        // then
        assertThat(result.getStatut()).isEqualTo("CONFIRMED");
    }

    @Test
    @DisplayName("Doit sauvegarder une reservation avec statut CANCELLED")
    void shouldSaveCancelledReservation() {
        // given
        reservation.setStatut("CANCELLED");
        when(repository.save(any(Reservation.class))).thenReturn(reservation);

        // when
        Reservation result = service.save(reservation);

        // then
        assertThat(result.getStatut()).isEqualTo("CANCELLED");
    }
}
