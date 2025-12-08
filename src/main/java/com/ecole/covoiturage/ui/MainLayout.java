package com.ecole.covoiturage.ui;

import com.ecole.covoiturage.entity.Student;
import com.ecole.covoiturage.security.SecurityUtils;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoUtility;

/*
Note pour moi : on a implementer DTO, DAO et l'architceture juste pour l'instaant c'est pas visible sur l'ui
faire l'authentification la prochaine fois, vérifie que tout fonctionne bien avant de passer à la suite
 */

public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Covoiturage Étudiant");
        logo.addClassNames(
            LumoUtility.FontSize.LARGE,
            LumoUtility.Margin.NONE
        );

        DrawerToggle toggle = new DrawerToggle();

        HorizontalLayout leftSection = new HorizontalLayout(toggle, logo);
        leftSection.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        leftSection.setSpacing(true);

        // Section droite avec info utilisateur et bouton connexion/deconnexion
        HorizontalLayout rightSection = new HorizontalLayout();
        rightSection.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        rightSection.setSpacing(true);

        Student currentUser = SecurityUtils.getAuthenticatedStudent();
        if (currentUser != null) {
            // Utilisateur connecte : afficher son nom et bouton deconnexion
            Span userName = new Span(currentUser.getName());
            userName.getStyle()
                .set("color", "white")
                .set("margin-right", "1rem")
                .set("font-weight", "500");

            Button logoutButton = new Button("Deconnexion", VaadinIcon.SIGN_OUT.create());
            logoutButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
            logoutButton.getStyle()
                .set("background", "rgba(255,255,255,0.2)")
                .set("color", "white")
                .set("border", "1px solid rgba(255,255,255,0.3)");

            logoutButton.addClickListener(e -> SecurityUtils.logout());

            rightSection.add(userName, logoutButton);
        } else {
            // Utilisateur non connecte : afficher bouton connexion
            Button loginButton = new Button("Se connecter", VaadinIcon.SIGN_IN.create());
            loginButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
            loginButton.getStyle()
                .set("background", "rgba(255,255,255,0.2)")
                .set("color", "white")
                .set("border", "1px solid rgba(255,255,255,0.3)");

            loginButton.addClickListener(e -> loginButton.getUI().ifPresent(ui -> ui.navigate("login")));

            rightSection.add(loginButton);
        }

        HorizontalLayout header = new HorizontalLayout(leftSection, rightSection);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.addClassNames(
            LumoUtility.Padding.Vertical.NONE,
            LumoUtility.Padding.Horizontal.MEDIUM
        );
        header.getStyle()
            .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
            .set("color", "white")
            .set("box-shadow", "0 2px 8px rgba(0,0,0,0.1)");

        addToNavbar(header);
    }

    private void createDrawer() {
        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Accueil", HomeView.class, VaadinIcon.HOME.create()));

        Student currentUser = SecurityUtils.getAuthenticatedStudent();

        if (currentUser != null) {
            // Utilisateur connecte : acces complet
            nav.addItem(new SideNavItem("Proposer un trajet", TrajetView.class, VaadinIcon.CAR.create()));
            nav.addItem(new SideNavItem("Rechercher", HomeView.class, VaadinIcon.SEARCH.create()));
            nav.addItem(new SideNavItem("Mes reservations", HomeView.class, VaadinIcon.CALENDAR.create()));
            nav.addItem(new SideNavItem("Mon profil", HomeView.class, VaadinIcon.USER.create()));
        } else {
            // Utilisateur non connecte : acces limite
            nav.addItem(new SideNavItem("Rechercher", HomeView.class, VaadinIcon.SEARCH.create()));
            nav.addItem(new SideNavItem("Mon profil", LoginView.class, VaadinIcon.USER.create()));
        }

        addToDrawer(nav);
    }
}
