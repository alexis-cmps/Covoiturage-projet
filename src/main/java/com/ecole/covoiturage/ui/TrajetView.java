package com.ecole.covoiturage.ui;

import com.ecole.covoiturage.entity.Student;
import com.ecole.covoiturage.entity.Trajet;
import com.ecole.covoiturage.security.SecurityUtils;
import com.ecole.covoiturage.service.StudentService;
import com.ecole.covoiturage.service.TrajetService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Route(value = "trajets", layout = MainLayout.class)
public class TrajetView extends VerticalLayout implements BeforeEnterObserver {

    private final TrajetService trajetService;
    private final StudentService studentService;
    private final Grid<Trajet> grid = new Grid<>(Trajet.class, false);

    private final TextField departField = new TextField("Point de depart");
    private final TextField destinationField = new TextField("Destination");
    private final DateTimePicker dateHeurePicker = new DateTimePicker("Date et heure de depart");
    private final NumberField nbPlacesField = new NumberField("Nombre de places");
    private final Button saveButton = new Button("Publier le trajet");

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Verifier que l'utilisateur est connecte
        if (!SecurityUtils.isAuthenticated()) {
            Notification.show("Vous devez etre connecte pour proposer un trajet.");
            event.forwardTo("login");
        }
    }

    @Autowired
    public TrajetView(TrajetService trajetService, StudentService studentService) {
        this.trajetService = trajetService;
        this.studentService = studentService;

        add(new H2("Proposer un trajet 🚗"));

        nbPlacesField.setStep(1);
        nbPlacesField.setMin(1);
        nbPlacesField.setMax(10);

        HorizontalLayout formLayout = new HorizontalLayout(
                departField, destinationField, dateHeurePicker, nbPlacesField, saveButton
        );
        formLayout.setAlignItems(Alignment.END);
        formLayout.setWidthFull();
        formLayout.setFlexGrow(1, departField, destinationField, dateHeurePicker, nbPlacesField);

        saveButton.addClickListener(event -> saveTrajet());

        grid.addColumn(Trajet::getDepart).setHeader("Départ");
        grid.addColumn(Trajet::getDestination).setHeader("Destination");
        grid.addColumn(Trajet::getDateHeure).setHeader("Date & heure");
        grid.addColumn(Trajet::getNbPlaces).setHeader("Places");
        grid.addColumn(trajet -> trajet.getConducteur() != null ? trajet.getConducteur().getName() : "N/A")
                .setHeader("Conducteur");

        refreshGrid();

        add(formLayout, grid);
        setPadding(true);
        setSpacing(true);
    }

    private void saveTrajet() {
        // Verifier que l'utilisateur est connecte et recuperer depuis la DB
        Long userId = SecurityUtils.getAuthenticatedUserId();
        if (userId == null) {
            Notification.show("Vous devez etre connecte pour proposer un trajet.");
            UI.getCurrent().navigate("login");
            return;
        }

        Student conducteur = studentService.findById(userId).orElse(null);
        if (conducteur == null) {
            Notification.show("Erreur: utilisateur introuvable.");
            UI.getCurrent().navigate("login");
            return;
        }

        String depart = departField.getValue();
        String destination = destinationField.getValue();
        LocalDateTime dateHeure = dateHeurePicker.getValue();
        Double nbPlaces = nbPlacesField.getValue();

        if (depart.isEmpty() || destination.isEmpty() || dateHeure == null || nbPlaces == null) {
            Notification.show("Merci de remplir tous les champs !");
            return;
        }

        Trajet trajet = new Trajet();
        trajet.setDepart(depart);
        trajet.setDestination(destination);
        trajet.setDateHeure(dateHeure);
        trajet.setNbPlaces(nbPlaces.intValue());
        trajet.setConducteur(conducteur);

        try {
            trajetService.save(trajet);
            Notification.show("Trajet publie avec succes !");
            clearForm();
            refreshGrid();
        } catch (IllegalArgumentException e) {
            Notification.show("Erreur: " + e.getMessage());
        }
    }

    private void refreshGrid() {
        grid.setItems(trajetService.findAll());
    }

    private void clearForm() {
        departField.clear();
        destinationField.clear();
        dateHeurePicker.clear();
        nbPlacesField.clear();
    }
}
