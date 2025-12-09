package com.ecole.covoiturage.ui;

import com.ecole.covoiturage.entity.Student;
import com.ecole.covoiturage.service.StudentService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.server.VaadinSession;

import java.util.List;
import java.util.Map;

@Route("auth")
@AnonymousAllowed
@CssImport("./styles/auth-view.css")
public class ModernAuthView extends VerticalLayout implements BeforeEnterObserver {

    private final StudentService studentService;
    private boolean isLoginMode = true;
    private VerticalLayout formContainer;
    private VerticalLayout mainCard;

    public ModernAuthView(StudentService studentService) {
        this.studentService = studentService;
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        addClassName("auth-container");

        getStyle()
                .set("background", "linear-gradient(135deg, #f0fdf4 0%, #d1fae5 50%, #a7f3d0 100%)")
                .set("position", "relative")
                .set("overflow-y", "auto")
                .set("padding", "16px");

        createBackgroundDecorations();
        createAuthForm();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        QueryParameters params = event.getLocation().getQueryParameters();
        Map<String, List<String>> parameters = params.getParameters();

        if (parameters.containsKey("mode")) {
            String mode = parameters.get("mode").get(0);
            if ("register".equals(mode)) {
                isLoginMode = false;
            } else {
                isLoginMode = true;
            }
            // Mettre à jour le formulaire après changement de mode
            if (formContainer != null) {
                updateForm();
            }
        }
    }

    private void createBackgroundDecorations() {
        for (int i = 0; i < 3; i++) {
            Div circle = new Div();
            circle.getStyle()
                    .set("position", "absolute")
                    .set("border-radius", "50%")
                    .set("opacity", "0.1");

            if (i == 0) {
                circle.getStyle()
                        .set("width", "300px")
                        .set("height", "300px")
                        .set("top", "-100px")
                        .set("right", "-50px")
                        .set("background", "#10b981");
            } else if (i == 1) {
                circle.getStyle()
                        .set("width", "200px")
                        .set("height", "200px")
                        .set("bottom", "-50px")
                        .set("left", "-75px")
                        .set("background", "#059669");
            } else {
                circle.getStyle()
                        .set("width", "150px")
                        .set("height", "150px")
                        .set("top", "50%")
                        .set("right", "10%")
                        .set("background", "#34d399");
            }
            add(circle);
        }
    }

    private void createAuthForm() {
        mainCard = new VerticalLayout();
        mainCard.addClassName("auth-card");
        mainCard.setWidth("100%");
        mainCard.setMaxWidth("460px");
        mainCard.setPadding(true);
        mainCard.setSpacing(true);

        mainCard.getStyle()
                .set("background", "white")
                .set("border-radius", "24px")
                .set("box-shadow", "0 20px 60px rgba(0, 0, 0, 0.15)")
                .set("padding", "40px 32px")
                .set("position", "relative")
                .set("z-index", "1");

        createHeader(mainCard);

        formContainer = new VerticalLayout();
        formContainer.setPadding(false);
        formContainer.setSpacing(true);
        formContainer.setWidthFull();

        updateForm();

        mainCard.add(formContainer);
        add(mainCard);
    }

    private void createHeader(VerticalLayout container) {
        RouterLink homeLink = new RouterLink("", ModernMainView.class);
        HorizontalLayout backLink = new HorizontalLayout();
        backLink.setAlignItems(Alignment.CENTER);
        backLink.setSpacing(true);

        Span arrow = new Span("←");
        arrow.getStyle().set("font-size", "18px").set("color", "#6b7280");

        Span backText = new Span("Retour à l'accueil");
        backText.getStyle()
                .set("color", "#6b7280")
                .set("font-size", "14px")
                .set("font-weight", "500");

        backLink.add(arrow, backText);
        homeLink.add(backLink);
        homeLink.getStyle()
                .set("text-decoration", "none")
                .set("margin-bottom", "24px")
                .set("transition", "all 0.2s ease");

        VerticalLayout logoSection = new VerticalLayout();
        logoSection.setAlignItems(Alignment.CENTER);
        logoSection.setPadding(false);
        logoSection.setSpacing(false);

        HorizontalLayout logoContainer = new HorizontalLayout();
        logoContainer.setAlignItems(Alignment.CENTER);
        logoContainer.setJustifyContentMode(JustifyContentMode.CENTER);
        logoContainer.setSpacing(true);

        Div iconWrapper = new Div();
        iconWrapper.getStyle()
                .set("background", "linear-gradient(135deg, #10b981 0%, #059669 100%)")
                .set("border-radius", "16px")
                .set("padding", "12px 16px")
                .set("box-shadow", "0 4px 12px rgba(16, 185, 129, 0.25)");

        Span icon = new Span("🚗");
        icon.getStyle().set("font-size", "32px");
        iconWrapper.add(icon);

        Span logoText = new Span("CovoitÉcole");
        logoText.getStyle()
                .set("font-size", "28px")
                .set("font-weight", "800")
                .set("background", "linear-gradient(135deg, #10b981 0%, #059669 100%)")
                .set("-webkit-background-clip", "text")
                .set("-webkit-text-fill-color", "transparent")
                .set("background-clip", "text");

        logoContainer.add(iconWrapper, logoText);
        logoSection.add(logoContainer);

        container.add(homeLink, logoSection);
    }

    private void updateForm() {
        formContainer.removeAll();

        Span titleIcon = new Span(isLoginMode ? "🔐" : "✨");
        titleIcon.getStyle().set("font-size", "40px").set("text-align", "center");

        H2 title = new H2(isLoginMode ? "Connexion" : "Créer un compte");
        title.getStyle()
                .set("margin", "16px 0 8px")
                .set("font-size", "28px")
                .set("font-weight", "800")
                .set("color", "#111827")
                .set("text-align", "center");

        Paragraph subtitle = new Paragraph(
                isLoginMode ? "Bon retour parmi nous !" : "Rejoignez la communauté CovoitÉcole");
        subtitle.getStyle()
                .set("color", "#6b7280")
                .set("margin", "0 0 32px 0")
                .set("text-align", "center")
                .set("font-size", "15px");

        formContainer.add(titleIcon, title, subtitle);

        if (isLoginMode) {
            createLoginForm();
        } else {
            createRegisterForm();
        }

        createToggleLink();
    }

    private void createLoginForm() {
        EmailField email = new EmailField("Email");
        email.setWidthFull();
        email.setPlaceholder("votre.email@ecole.fr");
        email.setPrefixComponent(new Span("✉️"));
        styleFormField(email);

        PasswordField password = new PasswordField("Mot de passe");
        password.setWidthFull();
        password.setPlaceholder("Entrez votre mot de passe");
        password.setPrefixComponent(new Span("🔒"));
        styleFormField(password);

        Anchor forgotPassword = new Anchor("#", "Mot de passe oublié ?");
        forgotPassword.getStyle()
                .set("color", "#10b981")
                .set("font-size", "14px")
                .set("text-decoration", "none")
                .set("font-weight", "600")
                .set("margin-bottom", "16px")
                .set("align-self", "flex-end");

        Button loginButton = new Button("Se connecter");
        loginButton.setWidthFull();
        loginButton.addClassName("primary-button");
        loginButton.getStyle()
                .set("background", "linear-gradient(135deg, #10b981 0%, #059669 100%)")
                .set("color", "white")
                .set("border", "none")
                .set("padding", "14px")
                .set("border-radius", "12px")
                .set("font-weight", "700")
                .set("font-size", "16px")
                .set("margin-top", "8px")
                .set("cursor", "pointer")
                .set("box-shadow", "0 4px 12px rgba(16, 185, 129, 0.3)")
                .set("transition", "all 0.3s ease");

        loginButton.addClickListener(e -> {
            if (email.getValue().isEmpty() || password.getValue().isEmpty()) {
                showErrorNotification("Veuillez remplir tous les champs");
            } else if (!email.getValue().contains("@")) {
                showErrorNotification("Veuillez entrer une adresse email valide");
            } else {
                try {
                    var studentOpt = studentService.authenticate(email.getValue(), password.getValue());

                    if (studentOpt.isPresent()) {
                        // Stocker l'étudiant en session
                        VaadinSession.getCurrent().setAttribute("currentStudent", studentOpt.get());
                        showSuccessNotification("Connexion réussie ! Bienvenue 🎉");
                        getUI().ifPresent(ui -> ui.navigate(ModernDashboardView.class));
                    } else {
                        showErrorNotification("Email ou mot de passe incorrect");
                    }
                } catch (Exception ex) {
                    showErrorNotification("Erreur lors de la connexion: " + ex.getMessage());
                }
            }
        });

        formContainer.add(email, password, forgotPassword, loginButton);
    }

    private void createRegisterForm() {
        TextField fullName = new TextField("Nom complet");
        fullName.setWidthFull();
        fullName.setPlaceholder("Jean Dupont");
        fullName.setPrefixComponent(new Span("👤"));
        styleFormField(fullName);

        EmailField email = new EmailField("Email");
        email.setWidthFull();
        email.setPlaceholder("votre.email@ecole.fr");
        email.setPrefixComponent(new Span("✉️"));
        styleFormField(email);

        PasswordField password = new PasswordField("Mot de passe");
        password.setWidthFull();
        password.setPlaceholder("Au moins 6 caractères");
        password.setPrefixComponent(new Span("🔒"));
        styleFormField(password);

        Div passwordStrength = new Div();
        passwordStrength.setText("La force du mot de passe sera affichée ici");
        passwordStrength.getStyle()
                .set("font-size", "12px")
                .set("color", "#9ca3af")
                .set("margin-top", "-8px")
                .set("margin-bottom", "16px");

        PasswordField confirmPassword = new PasswordField("Confirmer le mot de passe");
        confirmPassword.setWidthFull();
        confirmPassword.setPlaceholder("Répétez votre mot de passe");
        confirmPassword.setPrefixComponent(new Span("🔐"));
        styleFormField(confirmPassword);

        Button registerButton = new Button("Créer mon compte");
        registerButton.setWidthFull();
        registerButton.addClassName("primary-button");
        registerButton.getStyle()
                .set("background", "linear-gradient(135deg, #10b981 0%, #059669 100%)")
                .set("color", "white")
                .set("border", "none")
                .set("padding", "14px")
                .set("border-radius", "12px")
                .set("font-weight", "700")
                .set("font-size", "16px")
                .set("margin-top", "12px")
                .set("cursor", "pointer")
                .set("box-shadow", "0 4px 12px rgba(16, 185, 129, 0.3)")
                .set("transition", "all 0.3s ease");

        registerButton.addClickListener(e -> {
            if (fullName.getValue().isEmpty() || email.getValue().isEmpty() ||
                    password.getValue().isEmpty() || confirmPassword.getValue().isEmpty()) {
                showErrorNotification("Veuillez remplir tous les champs");
            } else if (!email.getValue().contains("@")) {
                showErrorNotification("Veuillez entrer une adresse email valide");
            } else if (password.getValue().length() < 6) {
                showErrorNotification("Le mot de passe doit contenir au moins 6 caractères");
            } else if (!password.getValue().equals(confirmPassword.getValue())) {
                showErrorNotification("Les mots de passe ne correspondent pas");
            } else {
                try {
                    studentService.register(fullName.getValue(), email.getValue(), password.getValue());
                    showSuccessNotification("Inscription réussie ! Vous pouvez maintenant vous connecter 🎉");
                    setLoginMode(true);
                    updateForm();
                } catch (Exception ex) {
                    showErrorNotification("Erreur lors de l'inscription: " + ex.getMessage());
                }
            }
        });

        formContainer.add(fullName, email, password, passwordStrength, confirmPassword, registerButton);
    }

    private void styleFormField(Object field) {
        if (field instanceof TextField) {
            ((TextField) field).getStyle()
                    .set("margin-bottom", "16px");
        } else if (field instanceof EmailField) {
            ((EmailField) field).getStyle()
                    .set("margin-bottom", "16px");
        } else if (field instanceof PasswordField) {
            ((PasswordField) field).getStyle()
                    .set("margin-bottom", "8px");
        }
    }

    private void createToggleLink() {
        VerticalLayout toggleSection = new VerticalLayout();
        toggleSection.setAlignItems(Alignment.CENTER);
        toggleSection.setPadding(false);
        toggleSection.setSpacing(false);
        toggleSection.getStyle().set("margin-top", "24px");

        HorizontalLayout toggleLine = new HorizontalLayout();
        toggleLine.setAlignItems(Alignment.CENTER);
        toggleLine.setJustifyContentMode(JustifyContentMode.CENTER);
        toggleLine.setSpacing(true);

        Span toggleText = new Span(
                isLoginMode ? "Vous n'avez pas de compte ? " : "Vous avez déjà un compte ? ");
        toggleText.getStyle()
                .set("color", "#6b7280")
                .set("font-size", "14px");

        Button toggleLink = new Button(
                isLoginMode ? "Inscrivez-vous" : "Connectez-vous");
        toggleLink.getStyle()
                .set("background", "transparent")
                .set("color", "#10b981")
                .set("border", "none")
                .set("font-weight", "700")
                .set("padding", "0")
                .set("font-size", "14px")
                .set("cursor", "pointer");

        toggleLink.addClickListener(e -> {
            if (isLoginMode) {
                getUI().ifPresent(ui -> ui.navigate("auth?mode=register"));
            } else {
                getUI().ifPresent(ui -> ui.navigate("auth"));
            }
        });

        toggleLine.add(toggleText, toggleLink);
        toggleSection.add(toggleLine);
        formContainer.add(toggleSection);
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

    public void setLoginMode(boolean loginMode) {
        this.isLoginMode = loginMode;
    }
}
