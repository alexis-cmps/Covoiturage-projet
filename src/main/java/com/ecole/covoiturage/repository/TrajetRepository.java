package com.ecole.covoiturage.repository;

import com.ecole.covoiturage.entity.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrajetRepository extends JpaRepository<Trajet, Long> {
}