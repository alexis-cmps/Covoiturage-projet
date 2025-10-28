package com.ecole.covoiturage.repository;

import com.ecole.covoiturage.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
