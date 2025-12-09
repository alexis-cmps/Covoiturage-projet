package com.ecole.covoiturage.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class StudentDTO {

    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String name;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    public StudentDTO() {
    }

    public StudentDTO(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Builder pattern
    public static StudentDTOBuilder builder() {
        return new StudentDTOBuilder();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static class StudentDTOBuilder {
        private Long id;
        private String name;
        private String email;

        public StudentDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public StudentDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public StudentDTOBuilder email(String email) {
            this.email = email;
            return this;
        }

        public StudentDTO build() {
            return new StudentDTO(id, name, email);
        }
    }
}
