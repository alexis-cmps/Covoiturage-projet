package com.ecole.covoiturage.mapper;

import com.ecole.covoiturage.dto.ReservationDTO;
import com.ecole.covoiturage.entity.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    private final TrajetMapper trajetMapper;
    private final StudentMapper studentMapper;

    public ReservationMapper(TrajetMapper trajetMapper, StudentMapper studentMapper) {
        this.trajetMapper = trajetMapper;
        this.studentMapper = studentMapper;
    }

    public ReservationDTO toDTO(Reservation reservation) {
        if (reservation == null) {
            return null;
        }
        return ReservationDTO.builder()
                .id(reservation.getId())
                .trajet(trajetMapper.toDTO(reservation.getTrajet()))
                .passager(studentMapper.toDTO(reservation.getPassager()))
                .dateReservation(reservation.getDateReservation())
                .statut(reservation.getStatut())
                .build();
    }

    public Reservation toEntity(ReservationDTO dto) {
        if (dto == null) {
            return null;
        }
        Reservation reservation = new Reservation();
        reservation.setId(dto.getId());
        if (dto.getTrajet() != null) {
            reservation.setTrajet(trajetMapper.toEntity(dto.getTrajet()));
        }
        if (dto.getPassager() != null) {
            reservation.setPassager(studentMapper.toEntity(dto.getPassager()));
        }
        reservation.setDateReservation(dto.getDateReservation());
        reservation.setStatut(dto.getStatut());
        return reservation;
    }
}

