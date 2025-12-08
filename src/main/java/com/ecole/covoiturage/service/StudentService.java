package com.ecole.covoiturage.service;

import com.ecole.covoiturage.entity.Student;
import com.ecole.covoiturage.repository.StudentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository repository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(StudentRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Student> findAll() {
        return repository.findAll();
    }

    public Optional<Student> findById(Long id) {
        return repository.findById(id);
    }

    public Student save(Student student) {
        return repository.save(student);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Student register(String name, String email, String password) {
        // Validation cote serveur
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom est obligatoire");
        }
        if (name.length() < 2 || name.length() > 100) {
            throw new IllegalArgumentException("Le nom doit contenir entre 2 et 100 caracteres");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("L'email est obligatoire");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("L'email n'est pas valide");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caracteres");
        }
        if (repository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Un compte avec cet email existe deja");
        }

        Student student = new Student();
        student.setName(name.trim());
        student.setEmail(email.trim().toLowerCase());
        student.setPassword(passwordEncoder.encode(password));
        return repository.save(student);
    }

    public Optional<Student> authenticate(String email, String rawPassword) {
        Optional<Student> studentOpt = repository.findByEmail(email);
        if (studentOpt.isPresent() && passwordEncoder.matches(rawPassword, studentOpt.get().getPassword())) {
            return studentOpt;
        }
        return Optional.empty();
    }

    public Optional<Student> findByEmail(String email) {
        return repository.findByEmail(email);
    }
}
