package com.ecole.covoiturage.mapper;

import com.ecole.covoiturage.dto.ReservationDTO;
import com.ecole.covoiturage.dto.StudentDTO;
import com.ecole.covoiturage.dto.TrajetDTO;
import com.ecole.covoiturage.entity.Reservation;
import com.ecole.covoiturage.entity.Student;
import com.ecole.covoiturage.entity.Trajet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests du Mapper Reservation")
class ReservationMapperTest {

    private ReservationMapper mapper;
    private TrajetMapper trajetMapper;
    private StudentMapper studentMapper;

    @BeforeEach
    void setUp() {
        studentMapper = new StudentMapper();
        trajetMapper = new TrajetMapper(studentMapper);
        mapper = new ReservationMapper(trajetMapper, studentMapper);
    }

    @Test
    @DisplayName("Doit convertir une entite Reservation en DTO")
    void shouldConvertEntityToDTO() {
        // given
        Student conducteur = new Student();
        conducteur.setId(1L);
        conducteur.setName("Paul");
        conducteur.setEmail("paul@etu.fr");

        Student passager = new Student();
        passager.setId(2L);
        passager.setName("Marie");
        passager.setEmail("marie@etu.fr");

        Trajet trajet = new Trajet();
        trajet.setId(1L);
        trajet.setDepart("Nantes");
        trajet.setDestination("Bordeaux");
        trajet.setDateHeure(LocalDateTime.now().plusDays(2));
        trajet.setNbPlaces(2);
        trajet.setConducteur(conducteur);

        LocalDateTime dateReservation = LocalDateTime.now();

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setDateReservation(dateReservation);
        reservation.setStatut("PENDING");
        reservation.setTrajet(trajet);
        reservation.setPassager(passager);

        // when
        ReservationDTO dto = mapper.toDTO(reservation);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getStatut()).isEqualTo("PENDING");
        assertThat(dto.getDateReservation()).isEqualTo(dateReservation);
        assertThat(dto.getTrajet()).isNotNull();
        assertThat(dto.getTrajet().getDepart()).isEqualTo("Nantes");
        assertThat(dto.getPassager()).isNotNull();
        assertThat(dto.getPassager().getName()).isEqualTo("Marie");
    }

    @Test
    @DisplayName("Doit convertir un DTO en entite Reservation")
    void shouldConvertDTOToEntity() {
        // given
        StudentDTO passagerDTO = StudentDTO.builder()
                .id(2L)
                .name("Jean")
                .email("jean@etu.fr")
                .build();

        TrajetDTO trajetDTO = TrajetDTO.builder()
                .id(1L)
                .depart("Lyon")
                .destination("Paris")
                .dateHeure(LocalDateTime.now().plusDays(3))
                .nbPlaces(3)
                .build();

        LocalDateTime dateReservation = LocalDateTime.now();

        ReservationDTO dto = ReservationDTO.builder()
                .id(1L)
                .dateReservation(dateReservation)
                .statut("CONFIRMED")
                .trajet(trajetDTO)
                .passager(passagerDTO)
                .build();

        // when
        Reservation reservation = mapper.toEntity(dto);

        // then
        assertThat(reservation).isNotNull();
        assertThat(reservation.getId()).isEqualTo(1L);
        assertThat(reservation.getStatut()).isEqualTo("CONFIRMED");
        assertThat(reservation.getTrajet()).isNotNull();
        assertThat(reservation.getPassager()).isNotNull();
    }

    @Test
    @DisplayName("Doit retourner null si entite est null")
    void shouldReturnNullWhenEntityIsNull() {
        // when
        ReservationDTO dto = mapper.toDTO(null);

        // then
        assertThat(dto).isNull();
    }

    @Test
    @DisplayName("Doit retourner null si DTO est null")
    void shouldReturnNullWhenDTOIsNull() {
        // when
        Reservation reservation = mapper.toEntity(null);

        // then
        assertThat(reservation).isNull();
    }

    @Test
    @DisplayName("Doit gerer une reservation sans trajet")
    void shouldHandleReservationWithoutTrajet() {
        // given
        Student passager = new Student();
        passager.setId(1L);
        passager.setName("Test");
        passager.setEmail("test@etu.fr");

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setStatut("PENDING");
        reservation.setTrajet(null);
        reservation.setPassager(passager);

        // when
        ReservationDTO dto = mapper.toDTO(reservation);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getTrajet()).isNull();
    }

    @Test
    @DisplayName("Doit gerer une reservation sans passager")
    void shouldHandleReservationWithoutPassager() {
        // given
        Trajet trajet = new Trajet();
        trajet.setId(1L);
        trajet.setDepart("Test");
        trajet.setDestination("Test2");

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setStatut("PENDING");
        reservation.setTrajet(trajet);
        reservation.setPassager(null);

        // when
        ReservationDTO dto = mapper.toDTO(reservation);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getPassager()).isNull();
    }
}

