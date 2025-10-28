package com.ecole.covoiturage.ui;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

// Cette annotation rend la page accessible à http://localhost:8080
@Route("")
public class HelloView extends VerticalLayout {

    public HelloView() {
        add(new H1("Bienvenue sur la plateforme de covoiturage étudiant 🚗"));
        add(new RouterLink("Voir les étudiants", StudentView.class));

    }

}
