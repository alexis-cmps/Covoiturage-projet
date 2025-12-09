package com.ecole.covoiturage.ui;

import com.ecole.covoiturage.service.StudentService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

@Route("old-register")
@AnonymousAllowed
public class RegisterView extends VerticalLayout {

    private final StudentService studentService;

    @Autowired
    public RegisterView(StudentService studentService) {
        this.studentService = studentService;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle().set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)");

        Div card = new Div();
        card.setWidth("450px");
        card.getStyle()
                .set("background", "white")
                .set("border-radius", "16px")
                .set("padding", "2.5rem")
                .set("box-shadow", "0 8px 32px rgba(0,0,0,0.2)");

        H2 title = new H2("Créer un compte");
        title.addClassNames(LumoUtility.Margin.Top.NONE, LumoUtility.TextAlignment.CENTER);

        TextField nameField = new TextField("Nom complet");
        nameField.setPrefixComponent(VaadinIcon.USER.create());
        nameField.setWidthFull();

        EmailField emailField = new EmailField("Email étudiant");
        emailField.setPrefixComponent(VaadinIcon.ENVELOPE.create());
        emailField.setWidthFull();

        PasswordField passwordField = new PasswordField("Mot de passe");
        passwordField.setPrefixComponent(VaadinIcon.LOCK.create());
        passwordField.setWidthFull();

        PasswordField confirmPasswordField = new PasswordField("Confirmer le mot de passe");
        confirmPasswordField.setPrefixComponent(VaadinIcon.LOCK.create());
        confirmPasswordField.setWidthFull();

        Button registerButton = new Button("S'inscrire", VaadinIcon.CHECK.create());
        registerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        registerButton.setWidthFull();

        FormLayout form = new FormLayout(nameField, emailField, passwordField, confirmPasswordField);
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        registerButton.addClickListener(event -> {
            if (!passwordField.getValue().equals(confirmPasswordField.getValue())) {
                Notification notification = Notification.show("Les mots de passe ne correspondent pas");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            try {
                studentService.register(
                        nameField.getValue(),
                        emailField.getValue(),
                        passwordField.getValue());
                Notification notification = Notification.show("Compte créé avec succès");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                getUI().ifPresent(ui -> ui.navigate("login"));
            } catch (IllegalArgumentException e) {
                Notification notification = Notification.show(e.getMessage());
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        Span loginLink = new Span("Déjà inscrit ? ");
        RouterLink link = new RouterLink("Se connecter", LoginView.class);
        link.getStyle().set("color", "#667eea");
        loginLink.add(link);
        loginLink.getStyle().set("text-align", "center").set("margin-top", "1rem");

        card.add(title, form, registerButton, loginLink);
        add(card);
    }
}
