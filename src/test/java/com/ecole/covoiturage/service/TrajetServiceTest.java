package com.ecole.covoiturage.service;

import com.ecole.covoiturage.entity.Student;
import com.ecole.covoiturage.entity.Trajet;
import com.ecole.covoiturage.repository.TrajetRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests du Service Trajet")
class TrajetServiceTest {

    @Mock
    private TrajetRepository repository;

    @Mock
    private Counter trajetCreationCounter;

    @Mock
    private Timer trajetSearchTimer;

    private TrajetService service;

    private Student conducteur;
    private Trajet trajet;

    @BeforeEach
    void setUp() {
        service = new TrajetService(repository, trajetCreationCounter, trajetSearchTimer);

        conducteur = new Student();
        conducteur.setId(1L);
        conducteur.setName("Alice");
        conducteur.setEmail("alice@etu.fr");

        trajet = new Trajet();
        trajet.setDepart("Paris");
        trajet.setDestination("Lyon");
        trajet.setDateHeure(LocalDateTime.now().plusDays(1));
        trajet.setNbPlaces(3);
        trajet.setConducteur(conducteur);
    }

    @Test
    @DisplayName("Doit retourner tous les trajets")
    void shouldReturnAllTrajets() {
        // given
        when(repository.findAll()).thenReturn(List.of(trajet));

        // when
        List<Trajet> result = service.findAll();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDepart()).isEqualTo("Paris");
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Doit trouver un trajet par ID")
    void shouldFindTrajetById() {
        // given
        trajet.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(trajet));

        // when
        Optional<Trajet> result = service.findById(1L);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getDestination()).isEqualTo("Lyon");
    }

    @Test
    @DisplayName("Doit retourner vide si trajet non trouve")
    void shouldReturnEmptyWhenTrajetNotFound() {
        // given
        when(repository.findById(999L)).thenReturn(Optional.empty());

        // when
        Optional<Trajet> result = service.findById(999L);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Doit sauvegarder un trajet valide")
    void shouldSaveValidTrajet() {
        // given
        when(repository.save(any(Trajet.class))).thenReturn(trajet);

        // when
        Trajet result = service.save(trajet);

        // then
        assertThat(result.getDepart()).isEqualTo("Paris");
        verify(repository, times(1)).save(trajet);
    }

    @Test
    @DisplayName("Doit supprimer un trajet")
    void shouldDeleteTrajet() {
        // when
        service.delete(1L);

        // then
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Doit trouver des trajets par depart et destination")
    void shouldFindByDepartAndDestination() {
        // given
        when(repository.findByDepartContainingIgnoreCaseAndDestinationContainingIgnoreCase("Paris", "Lyon"))
                .thenReturn(List.of(trajet));

        // when
        List<Trajet> result = service.findByDepartAndDestination("Paris", "Lyon");

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDepart()).isEqualTo("Paris");
    }

    @Test
    @DisplayName("Doit refuser un trajet sans depart")
    void shouldRejectTrajetWithoutDepart() {
        // given
        trajet.setDepart(null);

        // then
        assertThatThrownBy(() -> service.save(trajet))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("depart");
    }

    @Test
    @DisplayName("Doit refuser un trajet sans destination")
    void shouldRejectTrajetWithoutDestination() {
        // given
        trajet.setDestination("");

        // then
        assertThatThrownBy(() -> service.save(trajet))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("destination");
    }

    @Test
    @DisplayName("Doit refuser un trajet avec date passee")
    void shouldRejectTrajetWithPastDate() {
        // given
        trajet.setDateHeure(LocalDateTime.now().minusDays(1));

        // then
        assertThatThrownBy(() -> service.save(trajet))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("date");
    }

    @Test
    @DisplayName("Doit refuser un trajet avec nombre de places invalide")
    void shouldRejectTrajetWithInvalidPlaces() {
        // given
        trajet.setNbPlaces(0);

        // then
        assertThatThrownBy(() -> service.save(trajet))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("places");
    }

    @Test
    @DisplayName("Doit refuser un trajet sans conducteur")
    void shouldRejectTrajetWithoutConducteur() {
        // given
        trajet.setConducteur(null);

        // then
        assertThatThrownBy(() -> service.save(trajet))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("conducteur");
    }

    @Test
    @DisplayName("Doit retourner liste vide si aucun trajet trouve")
    void shouldReturnEmptyListWhenNoTrajetFound() {
        // given
        when(repository.findByDepartContainingIgnoreCaseAndDestinationContainingIgnoreCase("Inexistant", "Ville"))
                .thenReturn(List.of());

        // when
        List<Trajet> result = service.findByDepartAndDestination("Inexistant", "Ville");

        // then
        assertThat(result).isEmpty();
    }
}
