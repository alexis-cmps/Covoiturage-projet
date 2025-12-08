package com.ecole.covoiturage.mapper;

import com.ecole.covoiturage.dto.StudentDTO;
import com.ecole.covoiturage.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests du Mapper Student")
class StudentMapperTest {

    private StudentMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new StudentMapper();
    }

    @Test
    @DisplayName("Doit convertir une entite Student en DTO")
    void shouldConvertEntityToDTO() {
        // given
        Student student = new Student();
        student.setId(1L);
        student.setName("Alexis");
        student.setEmail("alexis@etu.fr");
        student.setPassword("secret");

        // when
        StudentDTO dto = mapper.toDTO(student);

        // then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Alexis");
        assertThat(dto.getEmail()).isEqualTo("alexis@etu.fr");
    }

    @Test
    @DisplayName("Doit convertir un DTO en entite Student")
    void shouldConvertDTOToEntity() {
        // given
        StudentDTO dto = StudentDTO.builder()
                .id(1L)
                .name("Marie")
                .email("marie@etu.fr")
                .build();

        // when
        Student student = mapper.toEntity(dto);

        // then
        assertThat(student).isNotNull();
        assertThat(student.getId()).isEqualTo(1L);
        assertThat(student.getName()).isEqualTo("Marie");
        assertThat(student.getEmail()).isEqualTo("marie@etu.fr");
    }

    @Test
    @DisplayName("Doit retourner null si entite est null")
    void shouldReturnNullWhenEntityIsNull() {
        // when
        StudentDTO dto = mapper.toDTO(null);

        // then
        assertThat(dto).isNull();
    }

    @Test
    @DisplayName("Doit retourner null si DTO est null")
    void shouldReturnNullWhenDTOIsNull() {
        // when
        Student student = mapper.toEntity(null);

        // then
        assertThat(student).isNull();
    }
}

