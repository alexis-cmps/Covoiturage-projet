package com.ecole.covoiturage.service;

import com.ecole.covoiturage.entity.Reservation;
import com.ecole.covoiturage.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationServiceTest {

    private final ReservationRepository repository = Mockito.mock(ReservationRepository.class);
    private final ReservationService service = new ReservationService(repository);

    @Test
    void shouldReturnAllReservations() {
        Reservation r1 = new Reservation(1L, LocalDateTime.now(), null, null);
        Reservation r2 = new Reservation(2L, LocalDateTime.now(), null, null);

        Mockito.when(repository.findAll()).thenReturn(List.of(r1, r2));

        List<Reservation> result = service.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldFindReservationById() {
        Reservation reservation = new Reservation(1L, LocalDateTime.now(), null, null);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(reservation));

        Optional<Reservation> result = service.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
    }
}
