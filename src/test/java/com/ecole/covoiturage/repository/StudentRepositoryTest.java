package com.ecole.covoiturage.repository;

import com.ecole.covoiturage.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@DisplayName("Tests du Repository Student")
class StudentRepositoryTest {

    @Autowired
    private StudentRepository repository;

    private Student student;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        student = new Student();
        student.setName("Alexis");
        student.setEmail("alexis@miage.fr");
        student.setPassword("password123");
    }

    @Test
    @DisplayName("Doit sauvegarder et recuperer un etudiant")
    void shouldSaveAndRetrieveStudent() {
        // when
        Student saved = repository.save(student);
        List<Student> results = repository.findAll();

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(results).hasSize(1);
        assertThat(results).extracting(Student::getEmail)
                .contains("alexis@miage.fr");
    }

    @Test
    @DisplayName("Doit trouver un etudiant par email")
    void shouldFindStudentByEmail() {
        // given
        repository.save(student);

        // when
        Optional<Student> found = repository.findByEmail("alexis@miage.fr");

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Alexis");
    }

    @Test
    @DisplayName("Doit retourner vide si email non trouve")
    void shouldReturnEmptyWhenEmailNotFound() {
        // when
        Optional<Student> found = repository.findByEmail("inconnu@miage.fr");

        // then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Doit mettre a jour un etudiant")
    void shouldUpdateStudent() {
        // given
        Student saved = repository.save(student);

        // when
        saved.setName("Alexis Updated");
        Student updated = repository.save(saved);

        // then
        assertThat(updated.getName()).isEqualTo("Alexis Updated");
        assertThat(repository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("Doit supprimer un etudiant")
    void shouldDeleteStudent() {
        // given
        Student saved = repository.save(student);

        // when
        repository.deleteById(saved.getId());

        // then
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("Doit trouver un etudiant par ID")
    void shouldFindStudentById() {
        // given
        Student saved = repository.save(student);

        // when
        Optional<Student> found = repository.findById(saved.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("alexis@miage.fr");
    }

    @Test
    @DisplayName("Doit sauvegarder plusieurs etudiants")
    void shouldSaveMultipleStudents() {
        // given
        Student student2 = new Student();
        student2.setName("Marie");
        student2.setEmail("marie@miage.fr");
        student2.setPassword("password456");

        // when
        repository.save(student);
        repository.save(student2);

        // then
        assertThat(repository.findAll()).hasSize(2);
    }

    @Test
    @DisplayName("Doit refuser un email en double")
    void shouldRejectDuplicateEmail() {
        // given
        repository.save(student);

        Student duplicate = new Student();
        duplicate.setName("Autre");
        duplicate.setEmail("alexis@miage.fr"); // meme email
        duplicate.setPassword("pass");

        // then
        assertThatThrownBy(() -> {
            repository.save(duplicate);
            repository.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }
}
