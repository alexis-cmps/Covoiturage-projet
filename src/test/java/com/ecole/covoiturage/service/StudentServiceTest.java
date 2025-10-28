package com.ecole.covoiturage.service;

import com.ecole.covoiturage.entity.Student;
import com.ecole.covoiturage.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class StudentServiceTest {

    private final StudentRepository repository = Mockito.mock(StudentRepository.class);
    private final StudentService service = new StudentService(repository);

    @Test
    void shouldReturnAllStudents() {
        Mockito.when(repository.findAll()).thenReturn(List.of(
                new Student(1L, "Alexis", "alexis@etu.fr"),
                new Student(2L, "Marie", "marie@etu.fr")
        ));

        List<Student> result = service.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Alexis");
    }
}
