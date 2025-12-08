package com.ecole.covoiturage.service;

import com.ecole.covoiturage.entity.Student;
import com.ecole.covoiturage.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests du Service Student")
class StudentServiceTest {

    @Mock
    private StudentRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private StudentService service;

    @BeforeEach
    void setUp() {
        service = new StudentService(repository, passwordEncoder);
    }

    @Test
    @DisplayName("Doit retourner tous les etudiants")
    void shouldReturnAllStudents() {
        // given
        when(repository.findAll()).thenReturn(List.of(
                new Student(1L, "Alexis", "alexis@etu.fr"),
                new Student(2L, "Marie", "marie@etu.fr")
        ));

        // when
        List<Student> result = service.findAll();

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Alexis");
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Doit trouver un etudiant par ID")
    void shouldFindStudentById() {
        // given
        Student student = new Student(1L, "Alexis", "alexis@etu.fr");
        when(repository.findById(1L)).thenReturn(Optional.of(student));

        // when
        Optional<Student> result = service.findById(1L);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Alexis");
    }

    @Test
    @DisplayName("Doit retourner vide si etudiant non trouve")
    void shouldReturnEmptyWhenStudentNotFound() {
        // given
        when(repository.findById(999L)).thenReturn(Optional.empty());

        // when
        Optional<Student> result = service.findById(999L);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Doit sauvegarder un etudiant")
    void shouldSaveStudent() {
        // given
        Student student = new Student();
        student.setName("Test");
        student.setEmail("test@etu.fr");
        when(repository.save(any(Student.class))).thenReturn(student);

        // when
        Student result = service.save(student);

        // then
        assertThat(result.getName()).isEqualTo("Test");
        verify(repository, times(1)).save(student);
    }

    @Test
    @DisplayName("Doit supprimer un etudiant")
    void shouldDeleteStudent() {
        // when
        service.delete(1L);

        // then
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Doit enregistrer un nouvel etudiant")
    void shouldRegisterNewStudent() {
        // given
        when(repository.findByEmail("new@etu.fr")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(repository.save(any(Student.class))).thenAnswer(inv -> {
            Student s = inv.getArgument(0);
            s.setId(1L);
            return s;
        });

        // when
        Student result = service.register("Nouveau", "new@etu.fr", "password123");

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Nouveau");
        assertThat(result.getEmail()).isEqualTo("new@etu.fr");
        verify(passwordEncoder, times(1)).encode("password123");
    }

    @Test
    @DisplayName("Doit refuser un email deja existant")
    void shouldRejectDuplicateEmail() {
        // given
        when(repository.findByEmail("existing@etu.fr")).thenReturn(Optional.of(new Student()));

        // then
        assertThatThrownBy(() -> service.register("Test", "existing@etu.fr", "password123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("existe deja");
    }

    @Test
    @DisplayName("Doit refuser un nom vide")
    void shouldRejectEmptyName() {
        assertThatThrownBy(() -> service.register("", "test@etu.fr", "password123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nom");
    }

    @Test
    @DisplayName("Doit refuser un email invalide")
    void shouldRejectInvalidEmail() {
        assertThatThrownBy(() -> service.register("Test", "invalid-email", "password123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("email");
    }

    @Test
    @DisplayName("Doit refuser un mot de passe trop court")
    void shouldRejectShortPassword() {
        assertThatThrownBy(() -> service.register("Test", "test@etu.fr", "123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("mot de passe");
    }
}
