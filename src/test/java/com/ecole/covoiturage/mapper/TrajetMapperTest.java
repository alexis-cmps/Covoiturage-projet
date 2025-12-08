package com.ecole.covoiturage.mapper;

import com.ecole.covoiturage.dto.TrajetDTO;
import com.ecole.covoiturage.entity.Student;
import com.ecole.covoiturage.entity.Trajet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests du Mapper Trajet")
class TrajetMapperTest {

    private TrajetMapper mapper;
    private StudentMapper studentMapper;

    @BeforeEach
    void setUp() {
        studentMapper = new StudentMapper();
        mapper = new TrajetMapper(studentMapper);
    }

    @Test
    @DisplayName("Doit convertir une entite Trajet en DTO")
    void shouldConvertEntityToDTO() {
        // given
        Student conducteur = new Student();
        conducteur.setId(1L);
        conducteur.setName("Alice");
        conducteur.setEmail("alice@etu.fr");

        LocalDateTime dateHeure = LocalDateTime.now().plusDays(1);

        Trajet trajet = new Trajet();
        trajet.setId(1L);
        trajet.setDepart("Paris");
        trajet.setDestination("Lyon");
        trajet.setDateHeure(dateHeure);
        trajet.setNbPlaces(3);
        trajet.setConducteur(conducteur);

        // when
        TrajetDTO dto = mapper.toDTO(trajet);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getDepart()).isEqualTo("Paris");
        assertThat(dto.getDestination()).isEqualTo("Lyon");
        assertThat(dto.getDateHeure()).isEqualTo(dateHeure);
        assertThat(dto.getNbPlaces()).isEqualTo(3);
        assertThat(dto.getConducteur()).isNotNull();
        assertThat(dto.getConducteur().getName()).isEqualTo("Alice");
    }

    @Test
    @DisplayName("Doit convertir un DTO en entite Trajet")
    void shouldConvertDTOToEntity() {
        // given
        LocalDateTime dateHeure = LocalDateTime.now().plusDays(2);

        TrajetDTO dto = TrajetDTO.builder()
                .id(1L)
                .depart("Marseille")
                .destination("Nice")
                .dateHeure(dateHeure)
                .nbPlaces(4)
                .build();

        // when
        Trajet trajet = mapper.toEntity(dto);

        // then
        assertThat(trajet).isNotNull();
        assertThat(trajet.getId()).isEqualTo(1L);
        assertThat(trajet.getDepart()).isEqualTo("Marseille");
        assertThat(trajet.getDestination()).isEqualTo("Nice");
        assertThat(trajet.getDateHeure()).isEqualTo(dateHeure);
        assertThat(trajet.getNbPlaces()).isEqualTo(4);
    }

    @Test
    @DisplayName("Doit retourner null si entite est null")
    void shouldReturnNullWhenEntityIsNull() {
        // when
        TrajetDTO dto = mapper.toDTO(null);

        // then
        assertThat(dto).isNull();
    }

    @Test
    @DisplayName("Doit retourner null si DTO est null")
    void shouldReturnNullWhenDTOIsNull() {
        // when
        Trajet trajet = mapper.toEntity(null);

        // then
        assertThat(trajet).isNull();
    }

    @Test
    @DisplayName("Doit gerer un trajet sans conducteur")
    void shouldHandleTrajetWithoutConducteur() {
        // given
        Trajet trajet = new Trajet();
        trajet.setId(1L);
        trajet.setDepart("Lille");
        trajet.setDestination("Toulouse");
        trajet.setConducteur(null);

        // when
        TrajetDTO dto = mapper.toDTO(trajet);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getConducteur()).isNull();
    }
}

