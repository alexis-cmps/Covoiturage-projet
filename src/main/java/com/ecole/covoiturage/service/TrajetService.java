package com.ecole.covoiturage.service;

import com.ecole.covoiturage.entity.Trajet;
import com.ecole.covoiturage.repository.TrajetRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TrajetService {

    private final TrajetRepository repository;
    private final Counter trajetCreatedCounter;
    private final Timer trajetSearchTimer;

    public TrajetService(TrajetRepository repository,
            Counter trajetCreatedCounter,
            Timer trajetSearchTimer) {
        this.repository = repository;
        this.trajetCreatedCounter = trajetCreatedCounter;
        this.trajetSearchTimer = trajetSearchTimer;
    }

    public List<Trajet> findAll() {
        return repository.findAll();
    }

    public Optional<Trajet> findById(Long id) {
        return repository.findById(id);
    }

    public Trajet save(Trajet trajet) {
        // Validation cote serveur
        validateTrajet(trajet);
        Trajet saved = repository.save(trajet);

        // Incrémenter le compteur de trajets créés
        trajetCreatedCounter.increment();

        return saved;
    }

    private void validateTrajet(Trajet trajet) {
        if (trajet.getDepart() == null || trajet.getDepart().trim().isEmpty()) {
            throw new IllegalArgumentException("Le lieu de depart est obligatoire");
        }
        if (trajet.getDestination() == null || trajet.getDestination().trim().isEmpty()) {
            throw new IllegalArgumentException("La destination est obligatoire");
        }
        if (trajet.getDateHeure() == null) {
            throw new IllegalArgumentException("La date et l'heure sont obligatoires");
        }
        if (trajet.getDateHeure().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La date doit etre dans le futur");
        }
        if (trajet.getNbPlaces() < 1 || trajet.getNbPlaces() > 8) {
            throw new IllegalArgumentException("Le nombre de places doit etre entre 1 et 8");
        }
        if (trajet.getConducteur() == null) {
            throw new IllegalArgumentException("Le conducteur est obligatoire");
        }
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<Trajet> findByDepartAndDestination(String depart, String destination) {
        // Mesurer le temps de recherche
        return trajetSearchTimer.record(() -> repository
                .findByDepartContainingIgnoreCaseAndDestinationContainingIgnoreCase(depart, destination));
    }

    public List<Trajet> findByConducteur(Long conducteurId) {
        return repository.findByConducteurId(conducteurId);
    }
}
