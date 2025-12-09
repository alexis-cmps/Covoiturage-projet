package com.ecole.covoiturage.ui;

import com.ecole.covoiturage.entity.Student;
import com.ecole.covoiturage.entity.Trajet;
import com.ecole.covoiturage.entity.Reservation;
import com.ecole.covoiturage.service.TrajetService;
import com.ecole.covoiturage.service.ReservationService;
import com.ecole.covoiturage.service.StudentService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.server.VaadinSession;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Route("dashboard")
@CssImport("./styles/dashboard-view.css")
public class ModernDashboardView extends VerticalLayout implements BeforeEnterObserver {

    private final TrajetService trajetService;
    private final ReservationService reservationService;
    private final StudentService studentService;

    private Student currentStudent;
    private VerticalLayout contentArea;
    private Tab searchTab, bookTab, proposeTab, myTripsTab, myBookingsTab;

    public ModernDashboardView(TrajetService trajetService,
            ReservationService reservationService,
            StudentService studentService) {
        this.trajetService = trajetService;
        this.reservationService = reservationService;
        this.studentService = studentService;

        setSizeFull();
        setPadding(false);
        setSpacing(false);
        addClassName("dashboard-container");

        getStyle()
                .set("background", "linear-gradient(135deg, #f0fdf4 0%, #d1fae5 100%)")
                .set("min-height", "100vh");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Récupérer l'utilisateur connecté depuis la session
        currentStudent = (Student) VaadinSession.getCurrent().getAttribute("currentStudent");

        if (currentStudent == null) {
            // Rediriger vers la page de connexion si non connecté
            event.rerouteTo("auth");
            return;
        }

        buildDashboard();
    }

    private void buildDashboard() {
        removeAll();

        // Header
        add(createHeader());

        // Navigation tabs
        Tabs tabs = createNavigationTabs();
        add(tabs);

        // Content area
        contentArea = new VerticalLayout();
        contentArea.setSizeFull();
        contentArea.setPadding(true);
        contentArea.setSpacing(true);
        contentArea.addClassName("content-area");

        add(contentArea);

        // Afficher le contenu de l'onglet par défaut
        showSearchView();
    }

    private Component createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setPadding(true);
        header.setSpacing(true);
        header.addClassName("dashboard-header");
        header.getStyle()
                .set("background", "white")
                .set("box-shadow", "0 2px 8px rgba(0,0,0,0.1)")
                .set("padding", "16px 24px");

        // Logo et titre
        HorizontalLayout logoSection = new HorizontalLayout();
        logoSection.setAlignItems(Alignment.CENTER);
        logoSection.setSpacing(true);

        Span logo = new Span("🚗");
        logo.getStyle().set("font-size", "32px");

        H2 title = new H2("CovoitÉcole");
        title.getStyle()
                .set("margin", "0")
                .set("color", "#10b981")
                .set("font-weight", "800");

        logoSection.add(logo, title);

        // Section utilisateur
        HorizontalLayout userSection = new HorizontalLayout();
        userSection.setAlignItems(Alignment.CENTER);
        userSection.setSpacing(true);

        Span userIcon = new Span("👤");
        userIcon.getStyle().set("font-size", "24px");

        Span userName = new Span("Bonjour, " + currentStudent.getName());
        userName.getStyle()
                .set("font-weight", "600")
                .set("color", "#374151");

        Button logoutButton = new Button("Déconnexion");
        logoutButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
        logoutButton.addClickListener(e -> {
            // Nettoyer la session
            VaadinSession.getCurrent().setAttribute("currentStudent", null);
            VaadinSession.getCurrent().close();
            // Rediriger vers la page d'authentification
            getUI().ifPresent(ui -> ui.navigate(ModernAuthView.class));
        });

        userSection.add(userIcon, userName, logoutButton);

        header.add(logoSection, userSection);
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);

        return header;
    }

    private Tabs createNavigationTabs() {
        searchTab = new Tab(new Icon(VaadinIcon.SEARCH), new Span("Rechercher un trajet"));
        bookTab = new Tab(new Icon(VaadinIcon.CAR), new Span("Trajets disponibles"));
        proposeTab = new Tab(new Icon(VaadinIcon.PLUS_CIRCLE), new Span("Proposer un trajet"));
        myTripsTab = new Tab(new Icon(VaadinIcon.ROAD), new Span("Mes trajets proposés"));
        myBookingsTab = new Tab(new Icon(VaadinIcon.TICKET), new Span("Mes réservations"));

        Tabs tabs = new Tabs(searchTab, bookTab, proposeTab, myTripsTab, myBookingsTab);
        tabs.setWidthFull();
        tabs.addClassName("navigation-tabs");
        tabs.getStyle()
                .set("background", "white")
                .set("box-shadow", "0 2px 4px rgba(0,0,0,0.05)");

        tabs.addSelectedChangeListener(event -> {
            Tab selectedTab = event.getSelectedTab();
            if (selectedTab == searchTab) {
                showSearchView();
            } else if (selectedTab == bookTab) {
                showAvailableTripsView();
            } else if (selectedTab == proposeTab) {
                showProposeView();
            } else if (selectedTab == myTripsTab) {
                showMyTripsView();
            } else if (selectedTab == myBookingsTab) {
                showMyBookingsView();
            }
        });

        return tabs;
    }

    private void showSearchView() {
        contentArea.removeAll();

        VerticalLayout searchLayout = new VerticalLayout();
        searchLayout.setWidthFull();
        searchLayout.setPadding(false);
        searchLayout.setSpacing(true);

        // Card de recherche
        VerticalLayout searchCard = new VerticalLayout();
        searchCard.addClassName("card");
        searchCard.getStyle()
                .set("background", "white")
                .set("border-radius", "16px")
                .set("padding", "32px")
                .set("box-shadow", "0 4px 12px rgba(0,0,0,0.1)");

        H3 searchTitle = new H3("🔍 Rechercher un trajet");
        searchTitle.getStyle().set("margin-top", "0").set("color", "#10b981");

        TextField destinationField = new TextField("Destination");
        destinationField.setWidthFull();
        destinationField.setPlaceholder("Ex: Paris, École, Aéroport...");
        destinationField.setPrefixComponent(new Icon(VaadinIcon.MAP_MARKER));

        Button searchButton = new Button("Rechercher");
        searchButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        searchButton.setIcon(new Icon(VaadinIcon.SEARCH));
        searchButton.getStyle()
                .set("background", "linear-gradient(135deg, #10b981 0%, #059669 100%)")
                .set("border", "none");

        VerticalLayout resultsLayout = new VerticalLayout();
        resultsLayout.setWidthFull();
        resultsLayout.setPadding(false);

        searchButton.addClickListener(e -> {
            String destination = destinationField.getValue();
            if (destination != null && !destination.trim().isEmpty()) {
                List<Trajet> results = trajetService.findAll().stream()
                        .filter(t -> t.getDestination().toLowerCase().contains(destination.toLowerCase()) ||
                                t.getDepart().toLowerCase().contains(destination.toLowerCase()))
                        .collect(Collectors.toList());
                displaySearchResults(resultsLayout, results);
            } else {
                displaySearchResults(resultsLayout, trajetService.findAll());
            }
        });

        searchCard.add(searchTitle, destinationField, searchButton);
        searchLayout.add(searchCard, resultsLayout);

        contentArea.add(searchLayout);
    }

    private void displaySearchResults(VerticalLayout resultsLayout, List<Trajet> results) {
        resultsLayout.removeAll();

        if (results.isEmpty()) {
            Paragraph noResults = new Paragraph("Aucun trajet trouvé. Essayez une autre recherche.");
            noResults.getStyle().set("color", "#6b7280").set("text-align", "center");
            resultsLayout.add(noResults);
            return;
        }

        H4 resultsTitle = new H4("Résultats (" + results.size() + " trajet" + (results.size() > 1 ? "s" : "") + ")");
        resultsTitle.getStyle().set("color", "#374151");
        resultsLayout.add(resultsTitle);

        for (Trajet trajet : results) {
            resultsLayout.add(createTrajetCard(trajet, true));
        }
    }

    private void showAvailableTripsView() {
        contentArea.removeAll();

        VerticalLayout tripsLayout = new VerticalLayout();
        tripsLayout.setWidthFull();
        tripsLayout.setPadding(false);

        H3 title = new H3("🚗 Trajets disponibles");
        title.getStyle().set("color", "#10b981");

        List<Trajet> availableTrips = trajetService.findAll().stream()
                .filter(t -> !t.getConducteur().getId().equals(currentStudent.getId()))
                .filter(t -> t.getDateHeure().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());

        if (availableTrips.isEmpty()) {
            Paragraph noTrips = new Paragraph("Aucun trajet disponible pour le moment.");
            noTrips.getStyle().set("color", "#6b7280");
            tripsLayout.add(title, noTrips);
        } else {
            tripsLayout.add(title);
            for (Trajet trajet : availableTrips) {
                tripsLayout.add(createTrajetCard(trajet, true));
            }
        }

        contentArea.add(tripsLayout);
    }

    private void showProposeView() {
        contentArea.removeAll();

        VerticalLayout proposeLayout = new VerticalLayout();
        proposeLayout.setWidthFull();
        proposeLayout.setMaxWidth("800px");
        proposeLayout.getStyle().set("margin", "0 auto");

        VerticalLayout proposeCard = new VerticalLayout();
        proposeCard.addClassName("card");
        proposeCard.getStyle()
                .set("background", "white")
                .set("border-radius", "16px")
                .set("padding", "32px")
                .set("box-shadow", "0 4px 12px rgba(0,0,0,0.1)");

        H3 title = new H3("➕ Proposer un nouveau trajet");
        title.getStyle().set("margin-top", "0").set("color", "#10b981");

        TextField departureField = new TextField("Adresse de départ");
        departureField.setWidthFull();
        departureField.setPlaceholder("Ex: 123 Rue de la Paix, Paris");
        departureField.setPrefixComponent(new Icon(VaadinIcon.MAP_MARKER));

        TextField destinationField = new TextField("Destination");
        destinationField.setWidthFull();
        destinationField.setPlaceholder("Ex: École Centrale, Lyon");
        destinationField.setPrefixComponent(new Icon(VaadinIcon.FLAG));

        DateTimePicker departurePicker = new DateTimePicker("Date et heure de départ");
        departurePicker.setWidthFull();

        IntegerField seatsField = new IntegerField("Nombre de places");
        seatsField.setWidthFull();
        seatsField.setValue(1);
        seatsField.setMin(1);
        seatsField.setMax(8);
        seatsField.setStepButtonsVisible(true);

        Button submitButton = new Button("Créer le trajet");
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        submitButton.setIcon(new Icon(VaadinIcon.CHECK));
        submitButton.getStyle()
                .set("background", "linear-gradient(135deg, #10b981 0%, #059669 100%)")
                .set("border", "none")
                .set("width", "100%")
                .set("margin-top", "16px");

        submitButton.addClickListener(e -> {
            if (departureField.isEmpty() || destinationField.isEmpty() || departurePicker.isEmpty()) {
                showErrorNotification("Veuillez remplir tous les champs obligatoires");
                return;
            }

            try {
                Trajet trajet = new Trajet();
                trajet.setDepart(departureField.getValue());
                trajet.setDestination(destinationField.getValue());
                trajet.setDateHeure(departurePicker.getValue());
                trajet.setNbPlaces(seatsField.getValue());
                trajet.setConducteur(currentStudent);

                trajetService.save(trajet);

                showSuccessNotification("Trajet créé avec succès ! 🎉");

                // Réinitialiser le formulaire
                departureField.clear();
                destinationField.clear();
                departurePicker.clear();
                seatsField.setValue(1);

                // Rediriger vers "Mes trajets proposés"
                myTripsTab.setSelected(true);
                showMyTripsView();

            } catch (Exception ex) {
                showErrorNotification("Erreur lors de la création du trajet: " + ex.getMessage());
            }
        });

        proposeCard.add(title, departureField, destinationField, departurePicker, seatsField, submitButton);
        proposeLayout.add(proposeCard);

        contentArea.add(proposeLayout);
    }

    private void showMyTripsView() {
        contentArea.removeAll();

        VerticalLayout myTripsLayout = new VerticalLayout();
        myTripsLayout.setWidthFull();
        myTripsLayout.setPadding(false);

        H3 title = new H3("🛣️ Mes trajets proposés");
        title.getStyle().set("color", "#10b981");

        List<Trajet> myTrips = trajetService.findByConducteur(currentStudent.getId());

        if (myTrips.isEmpty()) {
            Paragraph noTrips = new Paragraph("Vous n'avez pas encore proposé de trajet.");
            noTrips.getStyle().set("color", "#6b7280");

            Button proposeButton = new Button("Proposer un trajet");
            proposeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            proposeButton.addClickListener(e -> {
                proposeTab.setSelected(true);
                showProposeView();
            });

            myTripsLayout.add(title, noTrips, proposeButton);
        } else {
            myTripsLayout.add(title);
            for (Trajet trajet : myTrips) {
                myTripsLayout.add(createTrajetCard(trajet, false));
            }
        }

        contentArea.add(myTripsLayout);
    }

    private void showMyBookingsView() {
        contentArea.removeAll();

        VerticalLayout bookingsLayout = new VerticalLayout();
        bookingsLayout.setWidthFull();
        bookingsLayout.setPadding(false);

        H3 title = new H3("🎫 Mes réservations");
        title.getStyle().set("color", "#10b981");

        List<Reservation> myBookings = reservationService.findAll().stream()
                .filter(r -> r.getPassager() != null && r.getPassager().getId().equals(currentStudent.getId()))
                .collect(Collectors.toList());

        if (myBookings.isEmpty()) {
            Paragraph noBookings = new Paragraph("Vous n'avez pas encore de réservation.");
            noBookings.getStyle().set("color", "#6b7280");

            Button searchButton = new Button("Rechercher un trajet");
            searchButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            searchButton.addClickListener(e -> {
                searchTab.setSelected(true);
                showSearchView();
            });

            bookingsLayout.add(title, noBookings, searchButton);
        } else {
            bookingsLayout.add(title);
            for (Reservation booking : myBookings) {
                bookingsLayout.add(createBookingCard(booking));
            }
        }

        contentArea.add(bookingsLayout);
    }

    private Component createTrajetCard(Trajet trajet, boolean showBookButton) {
        VerticalLayout card = new VerticalLayout();
        card.addClassName("trip-card");
        card.getStyle()
                .set("background", "white")
                .set("border-radius", "12px")
                .set("padding", "20px")
                .set("box-shadow", "0 2px 8px rgba(0,0,0,0.08)")
                .set("margin-bottom", "12px")
                .set("border-left", "4px solid #10b981");

        // En-tête avec conducteur
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        header.setAlignItems(Alignment.CENTER);

        HorizontalLayout driverInfo = new HorizontalLayout();
        driverInfo.setAlignItems(Alignment.CENTER);
        driverInfo.setSpacing(true);

        Span driverIcon = new Span("👤");
        driverIcon.getStyle().set("font-size", "20px");

        Span driverName = new Span("Conducteur: " + trajet.getConducteur().getName());
        driverName.getStyle().set("font-weight", "600").set("color", "#374151");

        driverInfo.add(driverIcon, driverName);
        header.add(driverInfo);

        // Informations du trajet
        VerticalLayout tripInfo = new VerticalLayout();
        tripInfo.setPadding(false);
        tripInfo.setSpacing(true);

        HorizontalLayout routeInfo = new HorizontalLayout();
        routeInfo.setWidthFull();
        routeInfo.setAlignItems(Alignment.START);

        VerticalLayout departureInfo = new VerticalLayout();
        departureInfo.setPadding(false);
        departureInfo.setSpacing(false);

        Span departureLabel = new Span("Départ");
        departureLabel.getStyle().set("font-size", "12px").set("color", "#6b7280");

        Span departureAddress = new Span(trajet.getDepart());
        departureAddress.getStyle().set("font-weight", "600").set("color", "#111827");

        departureInfo.add(departureLabel, departureAddress);

        Icon arrowIcon = new Icon(VaadinIcon.ARROW_RIGHT);
        arrowIcon.getStyle().set("color", "#10b981").set("margin", "0 16px");

        VerticalLayout destinationInfo = new VerticalLayout();
        destinationInfo.setPadding(false);
        destinationInfo.setSpacing(false);

        Span destinationLabel = new Span("Destination");
        destinationLabel.getStyle().set("font-size", "12px").set("color", "#6b7280");

        Span destinationAddress = new Span(trajet.getDestination());
        destinationAddress.getStyle().set("font-weight", "600").set("color", "#111827");

        destinationInfo.add(destinationLabel, destinationAddress);

        routeInfo.add(departureInfo, arrowIcon, destinationInfo);

        // Date et places
        HorizontalLayout metaInfo = new HorizontalLayout();
        metaInfo.setWidthFull();
        metaInfo.setJustifyContentMode(JustifyContentMode.BETWEEN);

        HorizontalLayout dateInfo = new HorizontalLayout();
        dateInfo.setAlignItems(Alignment.CENTER);
        dateInfo.setSpacing(true);

        Icon calendarIcon = new Icon(VaadinIcon.CALENDAR);
        calendarIcon.getStyle().set("color", "#10b981");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");
        Span dateText = new Span(trajet.getDateHeure().format(formatter));
        dateText.getStyle().set("color", "#374151");

        dateInfo.add(calendarIcon, dateText);

        HorizontalLayout seatsInfo = new HorizontalLayout();
        seatsInfo.setAlignItems(Alignment.CENTER);
        seatsInfo.setSpacing(true);

        Icon seatsIcon = new Icon(VaadinIcon.USERS);
        seatsIcon.getStyle().set("color", "#10b981");

        // Calculer les places disponibles
        long reservedSeats = reservationService.findAll().stream()
                .filter(r -> r.getTrajet().getId().equals(trajet.getId()))
                .filter(r -> !"CANCELLED".equals(r.getStatut()))
                .count();
        long availableSeats = trajet.getNbPlaces() - reservedSeats;

        Span seatsText = new Span(availableSeats + "/" + trajet.getNbPlaces() + " places");
        seatsText.getStyle().set("color", "#374151").set("font-weight", "600");

        seatsInfo.add(seatsIcon, seatsText);

        metaInfo.add(dateInfo, seatsInfo);

        tripInfo.add(routeInfo, metaInfo);

        card.add(header, tripInfo);

        // Bouton de réservation
        if (showBookButton && availableSeats > 0) {
            Button bookButton = new Button("Réserver ce trajet");
            bookButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            bookButton.setIcon(new Icon(VaadinIcon.CHECK_CIRCLE));
            bookButton.getStyle()
                    .set("background", "linear-gradient(135deg, #10b981 0%, #059669 100%)")
                    .set("border", "none")
                    .set("margin-top", "12px");

            bookButton.addClickListener(e -> {
                try {
                    Reservation reservation = new Reservation();
                    reservation.setTrajet(trajet);
                    reservation.setPassager(currentStudent);
                    reservation.setDateReservation(LocalDateTime.now());
                    reservation.setStatut("CONFIRMED");

                    reservationService.save(reservation);
                    showSuccessNotification("Réservation effectuée avec succès ! 🎉");

                    // Rafraîchir la vue
                    if (searchTab.isSelected()) {
                        showSearchView();
                    } else if (bookTab.isSelected()) {
                        showAvailableTripsView();
                    }
                } catch (Exception ex) {
                    showErrorNotification("Erreur lors de la réservation: " + ex.getMessage());
                }
            });

            card.add(bookButton);
        }

        return card;
    }

    private Component createBookingCard(Reservation booking) {
        VerticalLayout card = new VerticalLayout();
        card.addClassName("booking-card");
        card.getStyle()
                .set("background", "white")
                .set("border-radius", "12px")
                .set("padding", "20px")
                .set("box-shadow", "0 2px 8px rgba(0,0,0,0.08)")
                .set("margin-bottom", "12px")
                .set("border-left",
                        "CONFIRMED".equals(booking.getStatut()) ? "4px solid #10b981" : "4px solid #dc2626");

        // En-tête avec statut
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        header.setAlignItems(Alignment.CENTER);

        Span statusBadge = new Span("CONFIRMED".equals(booking.getStatut()) ? "✅ Confirmée" : "❌ Annulée");
        statusBadge.getStyle()
                .set("background", "CONFIRMED".equals(booking.getStatut()) ? "#d1fae5" : "#fee2e2")
                .set("color", "CONFIRMED".equals(booking.getStatut()) ? "#065f46" : "#991b1b")
                .set("padding", "6px 14px")
                .set("border-radius", "12px")
                .set("font-weight", "700")
                .set("font-size", "13px");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");
        Span bookedDate = new Span("Réservé le " + booking.getDateReservation().format(formatter));
        bookedDate.getStyle().set("color", "#6b7280").set("font-size", "13px");

        header.add(statusBadge, bookedDate);

        // Informations du trajet
        Div tripDetails = new Div();

        Paragraph route = new Paragraph();
        route.getStyle().set("margin", "12px 0");

        Span from = new Span(booking.getTrajet().getDepart());
        from.getStyle().set("font-weight", "600").set("color", "#111827");

        Icon arrow = new Icon(VaadinIcon.ARROW_RIGHT);
        arrow.getStyle().set("color", "#10b981").set("margin", "0 8px");

        Span to = new Span(booking.getTrajet().getDestination());
        to.getStyle().set("font-weight", "600").set("color", "#111827");

        route.add(from, arrow, to);

        Paragraph tripDate = new Paragraph("📅 " + booking.getTrajet().getDateHeure().format(formatter));
        tripDate.getStyle().set("color", "#374151").set("margin", "8px 0");

        Paragraph driver = new Paragraph("👤 Conducteur: " + booking.getTrajet().getConducteur().getName());
        driver.getStyle().set("color", "#374151").set("margin", "8px 0");

        tripDetails.add(route, tripDate, driver);

        card.add(header, tripDetails);

        // Bouton d'annulation si confirmée
        if ("CONFIRMED".equals(booking.getStatut())) {
            Button cancelButton = new Button("Annuler cette réservation");
            cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            cancelButton.setIcon(new Icon(VaadinIcon.CLOSE_CIRCLE));
            cancelButton.getStyle().set("margin-top", "12px");

            cancelButton.addClickListener(e -> {
                Dialog confirmDialog = new Dialog();
                confirmDialog.setHeaderTitle("Confirmer l'annulation");

                VerticalLayout dialogContent = new VerticalLayout();
                dialogContent.add(new Paragraph("Êtes-vous sûr de vouloir annuler cette réservation ?"));

                Button confirmBtn = new Button("Oui, annuler", event -> {
                    try {
                        booking.setStatut("CANCELLED");
                        reservationService.save(booking);
                        showSuccessNotification("Réservation annulée avec succès");
                        confirmDialog.close();
                        showMyBookingsView();
                    } catch (Exception ex) {
                        showErrorNotification("Erreur: " + ex.getMessage());
                    }
                });
                confirmBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

                Button cancelBtn = new Button("Non, garder", event -> confirmDialog.close());

                HorizontalLayout buttons = new HorizontalLayout(confirmBtn, cancelBtn);
                dialogContent.add(buttons);

                confirmDialog.add(dialogContent);
                confirmDialog.open();
            });

            card.add(cancelButton);
        }

        return card;
    }

    private void showSuccessNotification(String message) {
        Notification notification = new Notification(message, 3000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.open();
    }

    private void showErrorNotification(String message) {
        Notification notification = new Notification(message, 4000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.open();
    }
}
