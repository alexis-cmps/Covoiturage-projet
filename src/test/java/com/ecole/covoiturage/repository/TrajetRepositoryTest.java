package com.ecole.covoiturage.repository;

import com.ecole.covoiturage.entity.Student;
import com.ecole.covoiturage.entity.Trajet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TrajetRepositoryTest {

    @Autowired
    private TrajetRepository trajetRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void shouldSaveAndFindTrajet() {
        Student conducteur = new Student();
        conducteur.setName("Alice");
        conducteur.setEmail("alice@etu.fr");
        studentRepository.save(conducteur);

        Trajet trajet = new Trajet();
        trajet.setDepart("Paris");
        trajet.setDestination("Lyon");
        trajet.setDateHeure(LocalDateTime.now().plusDays(1));
        trajet.setNbPlaces(3);
        trajet.setConducteur(conducteur);

        trajetRepository.save(trajet);

        List<Trajet> results = trajetRepository.findAll();

        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getConducteur().getName()).isEqualTo("Alice");
    }
}
