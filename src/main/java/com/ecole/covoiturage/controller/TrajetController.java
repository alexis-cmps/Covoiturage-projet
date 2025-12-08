package com.ecole.covoiturage.controller;

import com.ecole.covoiturage.dto.TrajetDTO;
import com.ecole.covoiturage.entity.Student;
import com.ecole.covoiturage.entity.Trajet;
import com.ecole.covoiturage.mapper.TrajetMapper;
import com.ecole.covoiturage.security.SecurityUtils;
import com.ecole.covoiturage.service.StudentService;
import com.ecole.covoiturage.service.TrajetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trajets")
public class TrajetController {

    private final TrajetService trajetService;
    private final StudentService studentService;
    private final TrajetMapper trajetMapper;

    public TrajetController(TrajetService trajetService, TrajetMapper trajetMapper, StudentService studentService) {
        this.trajetService = trajetService;
        this.trajetMapper = trajetMapper;
        this.studentService = studentService;
    }

    /**
     * Recupere le conducteur authentifie depuis la base de donnees
     */
    private Student getAuthenticatedConducteur() {
        Long userId = SecurityUtils.getAuthenticatedUserId();
        if (userId == null) {
            return null;
        }
        return studentService.findById(userId).orElse(null);
    }

    @GetMapping
    public ResponseEntity<List<TrajetDTO>> getAllTrajets() {
        List<TrajetDTO> trajets = trajetService.findAll().stream()
                .map(trajetMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(trajets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTrajetById(@PathVariable Long id) {
        return trajetService.findById(id)
                .map(trajet -> ResponseEntity.ok((Object) trajetMapper.toDTO(trajet)))
                .orElseGet(() -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "Trajet non trouve");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body((Object) error);
                });
    }

    @PostMapping
    public ResponseEntity<?> createTrajet(@Valid @RequestBody TrajetDTO trajetDTO) {
        Student conducteur = getAuthenticatedConducteur();
        if (conducteur == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Vous devez etre connecte pour creer un trajet");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        Trajet trajet = trajetMapper.toEntity(trajetDTO);
        trajet.setConducteur(conducteur);
        Trajet savedTrajet = trajetService.save(trajet);

        return ResponseEntity.status(HttpStatus.CREATED).body(trajetMapper.toDTO(savedTrajet));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTrajet(@PathVariable Long id, @Valid @RequestBody TrajetDTO trajetDTO) {
        Student conducteur = getAuthenticatedConducteur();
        if (conducteur == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Vous devez etre connecte");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        return trajetService.findById(id)
                .map(existingTrajet -> {
                    if (!existingTrajet.getConducteur().getId().equals(conducteur.getId())) {
                        Map<String, String> error = new HashMap<>();
                        error.put("error", "Vous n'etes pas autorise a modifier ce trajet");
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body((Object) error);
                    }

                    existingTrajet.setDepart(trajetDTO.getDepart());
                    existingTrajet.setDestination(trajetDTO.getDestination());
                    existingTrajet.setDateHeure(trajetDTO.getDateHeure());

                    Trajet updatedTrajet = trajetService.save(existingTrajet);
                    return ResponseEntity.ok((Object) trajetMapper.toDTO(updatedTrajet));
                })
                .orElseGet(() -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "Trajet non trouve");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTrajet(@PathVariable Long id) {
        Student conducteur = getAuthenticatedConducteur();
        if (conducteur == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Vous devez etre connecte");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        return trajetService.findById(id)
                .map(trajet -> {
                    if (!trajet.getConducteur().getId().equals(conducteur.getId())) {
                        Map<String, String> error = new HashMap<>();
                        error.put("error", "Vous n'etes pas autorise a supprimer ce trajet");
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body((Object) error);
                    }

                    trajetService.delete(id);
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Trajet supprime avec succes");
                    return ResponseEntity.ok((Object) response);
                })
                .orElseGet(() -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "Trajet non trouve");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }

    @GetMapping("/search")
    public ResponseEntity<List<TrajetDTO>> searchTrajets(
            @RequestParam(required = false) String depart,
            @RequestParam(required = false) String destination) {

        List<Trajet> trajets;
        if (depart != null && destination != null) {
            trajets = trajetService.findByDepartAndDestination(depart, destination);
        } else {
            trajets = trajetService.findAll();
        }

        List<TrajetDTO> trajetDTOs = trajets.stream()
                .map(trajetMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(trajetDTOs);
    }
}
