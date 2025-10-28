package com.ecole.covoiturage.repository;

import com.ecole.covoiturage.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}

