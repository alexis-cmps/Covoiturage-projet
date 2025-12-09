package com.ecole.covoiturage.ui;

import com.ecole.covoiturage.entity.Student;
import com.ecole.covoiturage.security.SecurityUtils;
import com.ecole.covoiturage.service.StudentService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Route("old-login")
@AnonymousAllowed
public class LoginView extends VerticalLayout {

    private final StudentService studentService;

    @Autowired
    public LoginView(StudentService studentService) {
        this.studentService = studentService;

        setSizeFull();
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        getStyle().set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)");

        Div card = new Div();
        card.setWidth("400px");
        card.getStyle()
                .set("background", "white")
                .set("border-radius", "16px")
                .set("padding", "2.5rem")
                .set("box-shadow", "0 8px 32px rgba(0,0,0,0.2)");

        H2 title = new H2("Connexion");
        title.getStyle().set("text-align", "center").set("margin-top", "0");

        // Formulaire de connexion
        LoginForm loginForm = new LoginForm();
        loginForm.setForgotPasswordButtonVisible(true);

        loginForm.addLoginListener(event -> {
            Optional<Student> studentOpt = studentService.authenticate(event.getUsername(), event.getPassword());
            if (studentOpt.isPresent()) {
                SecurityUtils.setAuthenticatedStudent(studentOpt.get());
                Notification.show("Bienvenue, " + studentOpt.get().getName() + " !");
                // Recharger la page pour mettre a jour le MainLayout
                UI.getCurrent().getPage().setLocation("/");
            } else {
                loginForm.setError(true);
            }
        });

        loginForm.addForgotPasswordListener(event -> Notification
                .show("Veuillez contacter l'administrateur pour reinitialiser votre mot de passe."));

        // Lien vers l'inscription
        Div registerSection = new Div();
        registerSection.getStyle().set("text-align", "center").set("margin-top", "1rem");

        Span registerText = new Span("Pas encore de compte ? ");
        RouterLink registerLink = new RouterLink("Creer un compte", RegisterView.class);
        registerLink.getStyle().set("color", "#667eea").set("font-weight", "bold");

        registerSection.add(registerText, registerLink);

        // Bouton retour a l'accueil
        Button homeButton = new Button("Retour a l'accueil", VaadinIcon.HOME.create());
        homeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        homeButton.getStyle().set("margin-top", "1rem");
        homeButton.addClickListener(e -> UI.getCurrent().navigate(""));

        card.add(title, loginForm, registerSection, homeButton);
        add(card);
    }
}
