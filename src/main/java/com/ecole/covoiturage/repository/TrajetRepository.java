package com.ecole.covoiturage.repository;

import com.ecole.covoiturage.entity.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrajetRepository extends JpaRepository<Trajet, Long> {
    List<Trajet> findByDepartContainingIgnoreCaseAndDestinationContainingIgnoreCase(String depart, String destination);
    List<Trajet> findByConducteurId(Long conducteurId);
}