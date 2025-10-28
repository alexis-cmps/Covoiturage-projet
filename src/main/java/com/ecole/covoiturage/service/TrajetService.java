package com.ecole.covoiturage.service;

import com.ecole.covoiturage.entity.Trajet;
import com.ecole.covoiturage.repository.TrajetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrajetService {

    private final TrajetRepository repository;

    public TrajetService(TrajetRepository repository) {
        this.repository = repository;
    }

    public List<Trajet> findAll() {
        return repository.findAll();
    }

    public Optional<Trajet> findById(Long id) {
        return repository.findById(id);
    }

    public Trajet save(Trajet trajet) {
        return repository.save(trajet);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
