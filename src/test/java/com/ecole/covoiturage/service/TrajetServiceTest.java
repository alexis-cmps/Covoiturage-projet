package com.ecole.covoiturage.service;

import com.ecole.covoiturage.entity.Trajet;
import com.ecole.covoiturage.repository.TrajetRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TrajetServiceTest {

    private final TrajetRepository repository = Mockito.mock(TrajetRepository.class);
    private final TrajetService service = new TrajetService(repository);

    @Test
    void shouldReturnAllTrajets() {
        Trajet trajet1 = new Trajet(1L, "Paris", "Lyon", LocalDateTime.now(), 3, null, null);
        Trajet trajet2 = new Trajet(2L, "Nantes", "Bordeaux", LocalDateTime.now(), 2, null, null);

        Mockito.when(repository.findAll()).thenReturn(List.of(trajet1, trajet2));

        List<Trajet> result = service.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(1).getDestination()).isEqualTo("Bordeaux");
    }

    @Test
    void shouldFindTrajetById() {
        Trajet trajet = new Trajet(1L, "Paris", "Lyon", LocalDateTime.now(), 3, null, null);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(trajet));

        Optional<Trajet> result = service.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getDepart()).isEqualTo("Paris");
    }
}
