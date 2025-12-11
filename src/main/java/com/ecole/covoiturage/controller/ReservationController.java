package com.ecole.covoiturage.controller;

import com.ecole.covoiturage.dto.ReservationDTO;
import com.ecole.covoiturage.entity.Reservation;
import com.ecole.covoiturage.entity.Student;
import com.ecole.covoiturage.entity.Trajet;
import com.ecole.covoiturage.mapper.ReservationMapper;
import com.ecole.covoiturage.security.SecurityUtils;
import com.ecole.covoiturage.service.ReservationService;
import com.ecole.covoiturage.service.StudentService;
import com.ecole.covoiturage.service.TrajetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final StudentService studentService;
    private final TrajetService trajetService;
    private final ReservationMapper reservationMapper;

    public ReservationController(ReservationService reservationService,
                                  StudentService studentService,
                                  TrajetService trajetService,
                                  ReservationMapper reservationMapper) {
        this.reservationService = reservationService;
        this.studentService = studentService;
        this.trajetService = trajetService;
        this.reservationMapper = reservationMapper;
    }

    /**
     * Recupere le passager authentifie depuis la base de donnees
     */
    private Student getAuthenticatedPassager() {
        Long userId = SecurityUtils.getAuthenticatedUserId();
        if (userId == null) {
            return null;
        }
        return studentService.findById(userId).orElse(null);
    }

    @GetMapping
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        List<ReservationDTO> reservations = reservationService.findAll().stream()
                .map(reservationMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getReservationById(@PathVariable Long id) {
        return reservationService.findById(id)
                .map(reservation -> ResponseEntity.ok((Object) reservationMapper.toDTO(reservation)))
                .orElseGet(() -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "Reservation non trouvee");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body((Object) error);
                });
    }

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestParam Long trajetId) {
        Student passager = getAuthenticatedPassager();
        if (passager == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Vous devez etre connecte pour faire une reservation");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        // Verifier que le trajet existe
        return trajetService.findById(trajetId)
                .map(trajet -> {
                    // Verifier que le passager n'est pas le conducteur
                    if (trajet.getConducteur().getId().equals(passager.getId())) {
                        Map<String, String> error = new HashMap<>();
                        error.put("error", "Vous ne pouvez pas reserver votre propre trajet");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((Object) error);
                    }

                    Reservation reservation = new Reservation();
                    reservation.setTrajet(trajet);
                    reservation.setPassager(passager);
                    reservation.setDateReservation(LocalDateTime.now());
                    reservation.setStatut("PENDING");

                    Reservation savedReservation = reservationService.save(reservation);
                    return ResponseEntity.status(HttpStatus.CREATED).body((Object) reservationMapper.toDTO(savedReservation));
                })
                .orElseGet(() -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "Trajet non trouve");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<?> confirmReservation(@PathVariable Long id) {
        Student conducteur = getAuthenticatedPassager();
        if (conducteur == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Vous devez etre connecte");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        return reservationService.findById(id)
                .map(reservation -> {
                    // Seul le conducteur du trajet peut confirmer
                    if (!reservation.getTrajet().getConducteur().getId().equals(conducteur.getId())) {
                        Map<String, String> error = new HashMap<>();
                        error.put("error", "Seul le conducteur peut confirmer cette reservation");
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body((Object) error);
                    }

                    reservation.setStatut("CONFIRMED");
                    Reservation updatedReservation = reservationService.save(reservation);
                    return ResponseEntity.ok((Object) reservationMapper.toDTO(updatedReservation));
                })
                .orElseGet(() -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "Reservation non trouvee");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelReservation(@PathVariable Long id) {
        Student user = getAuthenticatedPassager();
        if (user == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Vous devez etre connecte");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        return reservationService.findById(id)
                .map(reservation -> {
                    // Le passager ou le conducteur peut annuler
                    boolean isPassager = reservation.getPassager().getId().equals(user.getId());
                    boolean isConducteur = reservation.getTrajet().getConducteur().getId().equals(user.getId());

                    if (!isPassager && !isConducteur) {
                        Map<String, String> error = new HashMap<>();
                        error.put("error", "Vous n'etes pas autorise a annuler cette reservation");
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body((Object) error);
                    }

                    reservation.setStatut("CANCELLED");
                    Reservation updatedReservation = reservationService.save(reservation);
                    return ResponseEntity.ok((Object) reservationMapper.toDTO(updatedReservation));
                })
                .orElseGet(() -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "Reservation non trouvee");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id) {
        Student user = getAuthenticatedPassager();
        if (user == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Vous devez etre connecte");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        return reservationService.findById(id)
                .map(reservation -> {
                    // Seul le passager peut supprimer sa reservation
                    if (!reservation.getPassager().getId().equals(user.getId())) {
                        Map<String, String> error = new HashMap<>();
                        error.put("error", "Vous n'etes pas autorise a supprimer cette reservation");
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body((Object) error);
                    }

                    reservationService.delete(id);
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Reservation supprimee avec succes");
                    return ResponseEntity.ok((Object) response);
                })
                .orElseGet(() -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "Reservation non trouvee");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }
}

