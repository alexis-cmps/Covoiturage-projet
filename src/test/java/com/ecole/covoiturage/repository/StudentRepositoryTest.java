package com.ecole.covoiturage.repository;

import com.ecole.covoiturage.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository repository;

    @Test
    void shouldSaveAndRetrieveStudent() {
        // given
        Student s = new Student();
        s.setName("Alexis");
        s.setEmail("alexis@miage.fr");

        // when
        repository.save(s);
        List<Student> results = repository.findAll();

        // then
        assertThat(results).extracting(Student::getEmail)
                .contains("alexis@miage.fr");
    }
}
