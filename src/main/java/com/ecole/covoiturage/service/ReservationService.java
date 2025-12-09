package com.ecole.covoiturage.service;

import com.ecole.covoiturage.entity.Reservation;
import com.ecole.covoiturage.repository.ReservationRepository;
import io.micrometer.core.instrument.Counter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository repository;
    private final Counter reservationCreatedCounter;

    public ReservationService(ReservationRepository repository,
            Counter reservationCreatedCounter) {
        this.repository = repository;
        this.reservationCreatedCounter = reservationCreatedCounter;
    }

    public List<Reservation> findAll() {
        return repository.findAll();
    }

    public Optional<Reservation> findById(Long id) {
        return repository.findById(id);
    }

    public Reservation save(Reservation reservation) {
        Reservation saved = repository.save(reservation);

        // Incrémenter le compteur de réservations créées
        reservationCreatedCounter.increment();

        return saved;
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
