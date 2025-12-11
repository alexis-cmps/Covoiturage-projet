package com.ecole.covoiturage.controller;

import com.ecole.covoiturage.dto.LoginRequestDTO;
import com.ecole.covoiturage.dto.StudentDTO;
import com.ecole.covoiturage.dto.StudentRegistrationDTO;
import com.ecole.covoiturage.entity.Student;
import com.ecole.covoiturage.mapper.StudentMapper;
import com.ecole.covoiturage.service.EmailService;
import com.ecole.covoiturage.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final StudentService studentService;
    private final StudentMapper studentMapper;
    private final EmailService emailService;

    public AuthController(StudentService studentService, StudentMapper studentMapper, EmailService emailService) {
        this.studentService = studentService;
        this.studentMapper = studentMapper;
        this.emailService = emailService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody StudentRegistrationDTO registrationDTO) {
        try {
            Student student = studentService.register(
                    registrationDTO.getName(),
                    registrationDTO.getEmail(),
                    registrationDTO.getPassword()
            );
            StudentDTO studentDTO = studentMapper.toDTO(student);

            // Envoyer l'email de bienvenue
            try {
                emailService.sendWelcomeEmail(student.getEmail(), student.getName());
            } catch (Exception e) {
                // Log l'erreur mais ne bloque pas l'inscription
                System.err.println("Erreur lors de l'envoi de l'email de bienvenue: " + e.getMessage());
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Inscription réussie");
            response.put("student", studentDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        Optional<Student> studentOpt = studentService.authenticate(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        if (studentOpt.isPresent()) {
            StudentDTO studentDTO = studentMapper.toDTO(studentOpt.get());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Connexion réussie");
            response.put("student", studentDTO);

            return ResponseEntity.ok(response);
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Email ou mot de passe incorrect");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Déconnexion réussie");
        return ResponseEntity.ok(response);
    }
}

