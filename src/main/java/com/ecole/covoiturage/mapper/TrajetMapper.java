package com.ecole.covoiturage.mapper;

import com.ecole.covoiturage.dto.TrajetDTO;
import com.ecole.covoiturage.entity.Trajet;
import org.springframework.stereotype.Component;

@Component
public class TrajetMapper {

    private final StudentMapper studentMapper;

    public TrajetMapper(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    public TrajetDTO toDTO(Trajet trajet) {
        if (trajet == null) {
            return null;
        }

        int nbReservations = trajet.getReservations() != null ? trajet.getReservations().size() : 0;

        return TrajetDTO.builder()
                .id(trajet.getId())
                .depart(trajet.getDepart())
                .destination(trajet.getDestination())
                .dateHeure(trajet.getDateHeure())
                .nbPlaces(trajet.getNbPlaces())
                .conducteur(studentMapper.toDTO(trajet.getConducteur()))
                .nbReservations(nbReservations)
                .build();
    }

    public Trajet toEntity(TrajetDTO dto) {
        if (dto == null) {
            return null;
        }
        Trajet trajet = new Trajet();
        trajet.setId(dto.getId());
        trajet.setDepart(dto.getDepart());
        trajet.setDestination(dto.getDestination());
        trajet.setDateHeure(dto.getDateHeure());
        trajet.setNbPlaces(dto.getNbPlaces());
        if (dto.getConducteur() != null) {
            trajet.setConducteur(studentMapper.toEntity(dto.getConducteur()));
        }
        return trajet;
    }
}

