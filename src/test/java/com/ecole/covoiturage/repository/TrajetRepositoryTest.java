package com.ecole.covoiturage.repository;

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
@DisplayName("Tests du Repository Trajet")
class TrajetRepositoryTest {

    @Autowired
    private TrajetRepository trajetRepository;

    @Autowired
    private StudentRepository studentRepository;

    private Student conducteur;
    private Trajet trajet;

    @BeforeEach
    void setUp() {
        trajetRepository.deleteAll();
        studentRepository.deleteAll();

        conducteur = new Student();
        conducteur.setName("Alice");
        conducteur.setEmail("alice@etu.fr");
        conducteur.setPassword("password123");
        conducteur = studentRepository.save(conducteur);

        trajet = new Trajet();
        trajet.setDepart("Paris");
        trajet.setDestination("Lyon");
        trajet.setDateHeure(LocalDateTime.now().plusDays(1));
        trajet.setNbPlaces(3);
        trajet.setConducteur(conducteur);
    }

    @Test
    @DisplayName("Doit sauvegarder et recuperer un trajet")
    void shouldSaveAndFindTrajet() {
        // when
        Trajet saved = trajetRepository.save(trajet);
        List<Trajet> results = trajetRepository.findAll();

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getConducteur().getName()).isEqualTo("Alice");
    }

    @Test
    @DisplayName("Doit trouver un trajet par ID")
    void shouldFindTrajetById() {
        // given
        Trajet saved = trajetRepository.save(trajet);

        // when
        Optional<Trajet> found = trajetRepository.findById(saved.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getDepart()).isEqualTo("Paris");
        assertThat(found.get().getDestination()).isEqualTo("Lyon");
    }

    @Test
    @DisplayName("Doit trouver des trajets par depart et destination")
    void shouldFindByDepartAndDestination() {
        // given
        trajetRepository.save(trajet);

        Trajet trajet2 = new Trajet();
        trajet2.setDepart("Marseille");
        trajet2.setDestination("Nice");
        trajet2.setDateHeure(LocalDateTime.now().plusDays(2));
        trajet2.setNbPlaces(2);
        trajet2.setConducteur(conducteur);
        trajetRepository.save(trajet2);

        // when
        List<Trajet> results = trajetRepository
                .findByDepartContainingIgnoreCaseAndDestinationContainingIgnoreCase("paris", "lyon");

        // then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getDepart()).isEqualTo("Paris");
    }

    @Test
    @DisplayName("Doit trouver des trajets par conducteur ID")
    void shouldFindByConducteurId() {
        // given
        trajetRepository.save(trajet);

        Student autreConducteur = new Student();
        autreConducteur.setName("Bob");
        autreConducteur.setEmail("bob@etu.fr");
        autreConducteur.setPassword("pass");
        autreConducteur = studentRepository.save(autreConducteur);

        Trajet trajet2 = new Trajet();
        trajet2.setDepart("Lille");
        trajet2.setDestination("Toulouse");
        trajet2.setDateHeure(LocalDateTime.now().plusDays(3));
        trajet2.setNbPlaces(4);
        trajet2.setConducteur(autreConducteur);
        trajetRepository.save(trajet2);

        // when
        List<Trajet> trajetsAlice = trajetRepository.findByConducteurId(conducteur.getId());
        List<Trajet> trajetsBob = trajetRepository.findByConducteurId(autreConducteur.getId());

        // then
        assertThat(trajetsAlice).hasSize(1);
        assertThat(trajetsBob).hasSize(1);
        assertThat(trajetsAlice.get(0).getDepart()).isEqualTo("Paris");
        assertThat(trajetsBob.get(0).getDepart()).isEqualTo("Lille");
    }

    @Test
    @DisplayName("Doit mettre a jour un trajet")
    void shouldUpdateTrajet() {
        // given
        Trajet saved = trajetRepository.save(trajet);

        // when
        saved.setNbPlaces(5);
        saved.setDepart("Paris Gare de Lyon");
        Trajet updated = trajetRepository.save(saved);

        // then
        assertThat(updated.getNbPlaces()).isEqualTo(5);
        assertThat(updated.getDepart()).isEqualTo("Paris Gare de Lyon");
        assertThat(trajetRepository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("Doit supprimer un trajet")
    void shouldDeleteTrajet() {
        // given
        Trajet saved = trajetRepository.save(trajet);

        // when
        trajetRepository.deleteById(saved.getId());

        // then
        assertThat(trajetRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("Doit retourner liste vide si aucun trajet trouve")
    void shouldReturnEmptyListWhenNoTrajetFound() {
        // when
        List<Trajet> results = trajetRepository
                .findByDepartContainingIgnoreCaseAndDestinationContainingIgnoreCase("Inexistant", "Ville");

        // then
        assertThat(results).isEmpty();
    }

    @Test
    @DisplayName("Doit sauvegarder plusieurs trajets pour un conducteur")
    void shouldSaveMultipleTrajetsForConducteur() {
        // given
        trajetRepository.save(trajet);

        Trajet trajet2 = new Trajet();
        trajet2.setDepart("Lyon");
        trajet2.setDestination("Marseille");
        trajet2.setDateHeure(LocalDateTime.now().plusDays(5));
        trajet2.setNbPlaces(2);
        trajet2.setConducteur(conducteur);
        trajetRepository.save(trajet2);

        // when
        List<Trajet> trajets = trajetRepository.findByConducteurId(conducteur.getId());

        // then
        assertThat(trajets).hasSize(2);
    }
}
