package com.ecole.covoiturage;

import com.ecole.covoiturage.entity.Reservation;
import com.ecole.covoiturage.entity.Student;
import com.ecole.covoiturage.entity.Trajet;
import com.ecole.covoiturage.repository.ReservationRepository;
import com.ecole.covoiturage.repository.StudentRepository;
import com.ecole.covoiturage.repository.TrajetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Tests d'integration de la base de donnees")
class DatabaseIntegrationTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TrajetRepository trajetRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
        trajetRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Test
    @DisplayName("Doit creer un flux complet : etudiant -> trajet -> reservation")
    void shouldCreateCompleteFlow() {
        // Etape 1: Creer un conducteur
        Student conducteur = new Student();
        conducteur.setName("Paul Conducteur");
        conducteur.setEmail("paul@etu.fr");
        conducteur.setPassword("password123");
        conducteur = studentRepository.save(conducteur);

        assertThat(conducteur.getId()).isNotNull();

        // Etape 2: Creer un passager
        Student passager = new Student();
        passager.setName("Marie Passager");
        passager.setEmail("marie@etu.fr");
        passager.setPassword("password456");
        passager = studentRepository.save(passager);

        assertThat(passager.getId()).isNotNull();

        // Etape 3: Le conducteur cree un trajet
        Trajet trajet = new Trajet();
        trajet.setDepart("Paris");
        trajet.setDestination("Lyon");
        trajet.setDateHeure(LocalDateTime.now().plusDays(3));
        trajet.setNbPlaces(3);
        trajet.setConducteur(conducteur);
        trajet = trajetRepository.save(trajet);

        assertThat(trajet.getId()).isNotNull();
        assertThat(trajet.getConducteur().getName()).isEqualTo("Paul Conducteur");

        // Etape 4: Le passager reserve une place
        Reservation reservation = new Reservation();
        reservation.setDateReservation(LocalDateTime.now());
        reservation.setStatut("PENDING");
        reservation.setTrajet(trajet);
        reservation.setPassager(passager);
        reservation = reservationRepository.save(reservation);

        assertThat(reservation.getId()).isNotNull();

        // Verifications finales
        assertThat(studentRepository.findAll()).hasSize(2);
        assertThat(trajetRepository.findAll()).hasSize(1);
        assertThat(reservationRepository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("Doit gerer plusieurs reservations pour un trajet")
    void shouldHandleMultipleReservationsForTrajet() {
        // Creer conducteur
        Student conducteur = new Student();
        conducteur.setName("Conducteur");
        conducteur.setEmail("conducteur@etu.fr");
        conducteur.setPassword("pass");
        conducteur = studentRepository.save(conducteur);

        // Creer trajet
        Trajet trajet = new Trajet();
        trajet.setDepart("Nantes");
        trajet.setDestination("Bordeaux");
        trajet.setDateHeure(LocalDateTime.now().plusDays(5));
        trajet.setNbPlaces(4);
        trajet.setConducteur(conducteur);
        trajet = trajetRepository.save(trajet);

        // Creer 3 passagers et leurs reservations
        for (int i = 1; i <= 3; i++) {
            Student passager = new Student();
            passager.setName("Passager " + i);
            passager.setEmail("passager" + i + "@etu.fr");
            passager.setPassword("pass" + i);
            passager = studentRepository.save(passager);

            Reservation reservation = new Reservation();
            reservation.setDateReservation(LocalDateTime.now());
            reservation.setStatut("CONFIRMED");
            reservation.setTrajet(trajet);
            reservation.setPassager(passager);
            reservationRepository.save(reservation);
        }

        // Verifications
        assertThat(studentRepository.findAll()).hasSize(4); // 1 conducteur + 3 passagers
        assertThat(reservationRepository.findAll()).hasSize(3);
    }

    @Test
    @DisplayName("Doit gerer plusieurs trajets pour un conducteur")
    void shouldHandleMultipleTrajetsForConducteur() {
        // Creer conducteur
        Student conducteur = new Student();
        conducteur.setName("Super Conducteur");
        conducteur.setEmail("super@etu.fr");
        conducteur.setPassword("pass");
        conducteur = studentRepository.save(conducteur);

        // Creer 5 trajets
        for (int i = 1; i <= 5; i++) {
            Trajet trajet = new Trajet();
            trajet.setDepart("Ville " + i);
            trajet.setDestination("Destination " + i);
            trajet.setDateHeure(LocalDateTime.now().plusDays(i));
            trajet.setNbPlaces(i);
            trajet.setConducteur(conducteur);
            trajetRepository.save(trajet);
        }

        // Verifications
        List<Trajet> trajets = trajetRepository.findByConducteurId(conducteur.getId());
        assertThat(trajets).hasSize(5);
    }

    @Test
    @DisplayName("Doit rechercher des trajets par depart et destination")
    void shouldSearchTrajetsByDepartAndDestination() {
        // Creer conducteur
        Student conducteur = new Student();
        conducteur.setName("Conducteur");
        conducteur.setEmail("cond@etu.fr");
        conducteur.setPassword("pass");
        conducteur = studentRepository.save(conducteur);

        // Creer plusieurs trajets
        String[][] routes = {
            {"Paris", "Lyon"},
            {"Paris", "Marseille"},
            {"Lyon", "Paris"},
            {"Nantes", "Bordeaux"}
        };

        for (String[] route : routes) {
            Trajet trajet = new Trajet();
            trajet.setDepart(route[0]);
            trajet.setDestination(route[1]);
            trajet.setDateHeure(LocalDateTime.now().plusDays(1));
            trajet.setNbPlaces(3);
            trajet.setConducteur(conducteur);
            trajetRepository.save(trajet);
        }

        // Rechercher Paris -> Lyon
        List<Trajet> parisLyon = trajetRepository
            .findByDepartContainingIgnoreCaseAndDestinationContainingIgnoreCase("paris", "lyon");
        assertThat(parisLyon).hasSize(1);

        // Rechercher tous les departs de Paris
        List<Trajet> departParis = trajetRepository
            .findByDepartContainingIgnoreCaseAndDestinationContainingIgnoreCase("paris", "");
        assertThat(departParis).hasSize(2);
    }

    @Test
    @DisplayName("Doit confirmer puis annuler une reservation")
    void shouldConfirmThenCancelReservation() {
        // Setup
        Student conducteur = new Student();
        conducteur.setName("Conducteur");
        conducteur.setEmail("cond@etu.fr");
        conducteur.setPassword("pass");
        conducteur = studentRepository.save(conducteur);

        Student passager = new Student();
        passager.setName("Passager");
        passager.setEmail("pass@etu.fr");
        passager.setPassword("pass");
        passager = studentRepository.save(passager);

        Trajet trajet = new Trajet();
        trajet.setDepart("A");
        trajet.setDestination("B");
        trajet.setDateHeure(LocalDateTime.now().plusDays(1));
        trajet.setNbPlaces(2);
        trajet.setConducteur(conducteur);
        trajet = trajetRepository.save(trajet);

        // Creer reservation en PENDING
        Reservation reservation = new Reservation();
        reservation.setDateReservation(LocalDateTime.now());
        reservation.setStatut("PENDING");
        reservation.setTrajet(trajet);
        reservation.setPassager(passager);
        reservation = reservationRepository.save(reservation);

        assertThat(reservation.getStatut()).isEqualTo("PENDING");

        // Confirmer
        reservation.setStatut("CONFIRMED");
        reservation = reservationRepository.save(reservation);
        assertThat(reservation.getStatut()).isEqualTo("CONFIRMED");

        // Annuler
        reservation.setStatut("CANCELLED");
        reservation = reservationRepository.save(reservation);
        assertThat(reservation.getStatut()).isEqualTo("CANCELLED");
    }

    @Test
    @DisplayName("Doit trouver un etudiant par email")
    void shouldFindStudentByEmail() {
        // Creer etudiant
        Student student = new Student();
        student.setName("Test");
        student.setEmail("unique@etu.fr");
        student.setPassword("pass");
        studentRepository.save(student);

        // Rechercher par email
        var found = studentRepository.findByEmail("unique@etu.fr");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test");

        // Email inexistant
        var notFound = studentRepository.findByEmail("inexistant@etu.fr");
        assertThat(notFound).isEmpty();
    }

    @Test
    @DisplayName("Doit verifier l'integrite des relations apres suppression")
    void shouldVerifyRelationsIntegrityAfterDeletion() {
        // Setup
        Student conducteur = new Student();
        conducteur.setName("Conducteur");
        conducteur.setEmail("cond@etu.fr");
        conducteur.setPassword("pass");
        conducteur = studentRepository.save(conducteur);

        Trajet trajet = new Trajet();
        trajet.setDepart("X");
        trajet.setDestination("Y");
        trajet.setDateHeure(LocalDateTime.now().plusDays(1));
        trajet.setNbPlaces(2);
        trajet.setConducteur(conducteur);
        trajet = trajetRepository.save(trajet);

        // Supprimer le trajet
        trajetRepository.deleteById(trajet.getId());

        // Verifier que le trajet est supprime mais le conducteur existe toujours
        assertThat(trajetRepository.findAll()).isEmpty();
        assertThat(studentRepository.findById(conducteur.getId())).isPresent();
    }
}

