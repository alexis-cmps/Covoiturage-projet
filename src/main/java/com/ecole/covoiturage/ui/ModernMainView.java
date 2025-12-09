package com.ecole.covoiturage.ui;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("")
@AnonymousAllowed
@CssImport("./styles/main-view.css")
public class ModernMainView extends VerticalLayout {

    public ModernMainView() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        getStyle().set("background", "linear-gradient(135deg, #ffffff 0%, #f8fafc 100%)");

        // En-tête (Header)
        add(createHeader());

        // Section Hero
        add(createHeroSection());

        // Section des avantages
        add(createBenefitsSection());

        // Section "Comment ça marche"
        add(createHowItWorksSection());

        // Section Statistiques
        add(createStatsSection());

        // Section CTA finale
        add(createFinalCTASection());

        // Footer
        add(createFooter());
    }

    private HorizontalLayout createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setPadding(true);
        header.setAlignItems(Alignment.CENTER);
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);

        header.getStyle()
                .set("background-color", "white")
                .set("box-shadow", "0 2px 10px rgba(0,0,0,0.1)")
                .set("padding", "16px 32px")
                .set("margin-bottom", "0");

        // Logo simple
        HorizontalLayout logo = new HorizontalLayout();
        logo.setAlignItems(Alignment.CENTER);
        logo.setSpacing(true);

        Span logoIcon = new Span("🚗");
        logoIcon.getStyle()
                .set("font-size", "32px")
                .set("background-color", "#10b981")
                .set("color", "white")
                .set("padding", "8px 12px")
                .set("border-radius", "12px");

        Span logoText = new Span("CovoitÉcole");
        logoText.getStyle()
                .set("font-size", "28px")
                .set("font-weight", "bold")
                .set("color", "#10b981");

        logo.add(logoIcon, logoText);

        // Navigation simple
        HorizontalLayout nav = new HorizontalLayout();
        nav.setAlignItems(Alignment.CENTER);
        nav.setSpacing(true);

        Button accueil = new Button("Accueil");
        accueil.getStyle()
                .set("background-color", "transparent")
                .set("color", "#6b7280")
                .set("border", "none")
                .set("padding", "12px 16px")
                .set("font-weight", "600");
        accueil.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(ModernMainView.class)));

        Button rechercher = new Button("Rechercher");
        rechercher.getStyle()
                .set("background-color", "transparent")
                .set("color", "#6b7280")
                .set("border", "none")
                .set("padding", "12px 16px")
                .set("font-weight", "600");
        rechercher.addClickListener(
                e -> Notification.show("Redirection vers la recherche", 2000, Notification.Position.MIDDLE));

        Button connexion = new Button("Connexion");
        connexion.getStyle()
                .set("background-color", "#10b981")
                .set("color", "white")
                .set("border", "none")
                .set("padding", "12px 24px")
                .set("border-radius", "8px")
                .set("font-weight", "600");
        connexion.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(ModernAuthView.class)));

        nav.add(accueil, rechercher, connexion);

        header.add(logo, nav);
        return header;
    }

    private VerticalLayout createHeroSection() {
        VerticalLayout hero = new VerticalLayout();
        hero.setWidthFull();
        hero.setAlignItems(Alignment.CENTER);
        hero.setPadding(true);
        hero.setSpacing(true);

        hero.getStyle()
                .set("padding", "80px 32px")
                .set("text-align", "center")
                .set("background-color", "#f8fafc");

        // Badge écologique simple
        Div badge = new Div();
        badge.add(new Span("🍃 Covoiturage écologique et économique"));
        badge.getStyle()
                .set("background-color", "#d1fae5")
                .set("color", "#065f46")
                .set("padding", "8px 20px")
                .set("border-radius", "20px")
                .set("font-size", "14px")
                .set("font-weight", "500")
                .set("display", "inline-block")
                .set("margin-bottom", "24px");

        // Titre principal simplifié
        H1 title = new H1("Partagez vos trajets entre étudiants");
        title.getStyle()
                .set("font-size", "3.5rem")
                .set("font-weight", "800")
                .set("line-height", "1.1")
                .set("margin", "0 0 24px 0")
                .set("color", "#10b981")
                .set("max-width", "800px");

        // Description
        Paragraph description = new Paragraph(
                "Économisez sur vos déplacements, réduisez votre empreinte carbone et créez des liens avec d'autres étudiants de votre école.");
        description.getStyle()
                .set("font-size", "18px")
                .set("color", "#6b7280")
                .set("max-width", "600px")
                .set("margin", "0 auto 40px")
                .set("line-height", "1.6");

        // Boutons d'action
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        buttons.setJustifyContentMode(JustifyContentMode.CENTER);

        Button trouverTrajet = new Button("Trouver un trajet");
        trouverTrajet.getStyle()
                .set("background-color", "#10b981")
                .set("color", "white")
                .set("border", "none")
                .set("padding", "16px 32px")
                .set("border-radius", "8px")
                .set("font-size", "16px")
                .set("font-weight", "600");
        trouverTrajet.addClickListener(
                e -> Notification.show("Redirection vers la recherche de trajets", 3000, Notification.Position.MIDDLE));

        Button sinscrire = new Button("S'inscrire");
        sinscrire.getStyle()
                .set("background-color", "white")
                .set("color", "#374151")
                .set("border", "2px solid #d1d5db")
                .set("padding", "16px 32px")
                .set("border-radius", "8px")
                .set("font-size", "16px")
                .set("font-weight", "600");
        sinscrire.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("auth?mode=register")));

        buttons.add(trouverTrajet, sinscrire);

        hero.add(badge, title, description, buttons);
        return hero;
    }

    private VerticalLayout createHowItWorksSection() {
        VerticalLayout section = new VerticalLayout();
        section.setWidthFull();
        section.setAlignItems(Alignment.CENTER);
        section.setPadding(true);
        section.setSpacing(true);

        section.getStyle()
                .set("padding", "60px 32px")
                .set("background-color", "#f8fafc");

        // Titre de section
        H2 title = new H2("Comment ça marche ?");
        title.getStyle()
                .set("font-size", "2.5rem")
                .set("font-weight", "800")
                .set("margin", "0 0 16px 0")
                .set("text-align", "center")
                .set("color", "#111827");

        Paragraph subtitle = new Paragraph("Trois étapes simples pour commencer");
        subtitle.getStyle()
                .set("color", "#6b7280")
                .set("font-size", "18px")
                .set("margin", "0 0 48px 0")
                .set("text-align", "center");

        // Étapes
        HorizontalLayout steps = new HorizontalLayout();
        steps.setWidthFull();
        steps.setJustifyContentMode(JustifyContentMode.CENTER);
        steps.setSpacing(true);

        // Étape 1
        steps.add(createStep("1", "Créez votre compte", "Inscrivez-vous gratuitement avec votre email étudiant"));

        // Étape 2
        steps.add(createStep("2", "Recherchez ou proposez", "Trouvez un trajet ou proposez le vôtre"));

        // Étape 3
        steps.add(createStep("3", "Voyagez ensemble", "Partagez votre trajet et économisez"));

        section.add(title, subtitle, steps);
        return section;
    }

    private VerticalLayout createStep(String number, String title, String description) {
        VerticalLayout step = new VerticalLayout();
        step.setAlignItems(Alignment.CENTER);
        step.setPadding(true);
        step.setSpacing(true);

        step.getStyle()
                .set("text-align", "center")
                .set("max-width", "300px")
                .set("min-width", "250px");

        // Numéro en cercle simple
        Div numberCircle = new Div();
        numberCircle.add(new Span(number));
        numberCircle.getStyle()
                .set("width", "60px")
                .set("height", "60px")
                .set("border-radius", "50%")
                .set("background-color", "#10b981")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("color", "white")
                .set("font-size", "24px")
                .set("font-weight", "bold")
                .set("margin-bottom", "16px");

        // Titre de l'étape
        H3 stepTitle = new H3(title);
        stepTitle.getStyle()
                .set("font-size", "18px")
                .set("font-weight", "700")
                .set("margin", "0 0 8px 0")
                .set("color", "#111827");

        // Description
        Paragraph stepDesc = new Paragraph(description);
        stepDesc.getStyle()
                .set("color", "#6b7280")
                .set("margin", "0")
                .set("line-height", "1.6")
                .set("font-size", "14px");

        step.add(numberCircle, stepTitle, stepDesc);
        return step;
    }

    private HorizontalLayout createStatsSection() {
        HorizontalLayout stats = new HorizontalLayout();
        stats.setWidthFull();
        stats.setJustifyContentMode(JustifyContentMode.CENTER);
        stats.setSpacing(true);
        stats.setPadding(true);

        stats.getStyle()
                .set("padding", "60px 32px")
                .set("background-color", "white");

        stats.add(
                createStatItem("500+", "Étudiants inscrits"),
                createStatItem("1200+", "Trajets partagés"),
                createStatItem("-40%", "CO₂ économisé"));

        return stats;
    }

    private VerticalLayout createStatItem(String number, String label) {
        VerticalLayout item = new VerticalLayout();
        item.setAlignItems(Alignment.CENTER);
        item.setPadding(true);
        item.setSpacing(false);

        item.getStyle()
                .set("text-align", "center")
                .set("min-width", "200px");

        H2 numberEl = new H2(number);
        numberEl.getStyle()
                .set("margin", "0 0 8px 0")
                .set("font-size", "3rem")
                .set("font-weight", "800")
                .set("color", "#10b981");

        Paragraph labelEl = new Paragraph(label);
        labelEl.getStyle()
                .set("margin", "0")
                .set("color", "#6b7280")
                .set("font-size", "16px");

        item.add(numberEl, labelEl);
        return item;
    }

    private HorizontalLayout createBenefitsSection() {
        HorizontalLayout benefits = new HorizontalLayout();
        benefits.setWidthFull();
        benefits.setJustifyContentMode(JustifyContentMode.CENTER);
        benefits.setSpacing(true);
        benefits.setPadding(true);

        benefits.getStyle()
                .set("padding", "60px 32px")
                .set("background-color", "white");

        // Carte 1: Économisez
        benefits.add(createBenefitCard(
                "📉",
                "Économisez de l'argent",
                "Partagez les frais de carburant et réduisez vos dépenses de transport jusqu'à 70%"));

        // Carte 2: Environnement
        benefits.add(createBenefitCard(
                "🍃",
                "Protégez l'environnement",
                "Réduisez votre empreinte carbone en partageant votre véhicule avec d'autres étudiants"));

        // Carte 3: Créez des liens
        benefits.add(createBenefitCard(
                "👥",
                "Créez des liens",
                "Rencontrez d'autres étudiants et élargissez votre réseau social pendant vos trajets"));

        return benefits;
    }

    private VerticalLayout createBenefitCard(String icon, String title, String description) {
        VerticalLayout card = new VerticalLayout();
        card.setAlignItems(Alignment.CENTER);
        card.setPadding(true);
        card.setSpacing(true);
        card.addClassName("benefit-card");

        card.getStyle()
                .set("background-color", "#f9fafb")
                .set("border", "1px solid #e5e7eb")
                .set("border-radius", "12px")
                .set("padding", "32px 24px")
                .set("max-width", "350px")
                .set("min-width", "300px")
                .set("text-align", "center")
                .set("box-shadow", "0 4px 6px rgba(0, 0, 0, 0.05)");

        // Icône simple
        Span iconSpan = new Span(icon);
        iconSpan.getStyle()
                .set("font-size", "48px")
                .set("margin-bottom", "16px");

        H3 cardTitle = new H3(title);
        cardTitle.getStyle()
                .set("font-size", "20px")
                .set("font-weight", "700")
                .set("margin", "0 0 12px 0")
                .set("color", "#111827");

        Paragraph cardDesc = new Paragraph(description);
        cardDesc.getStyle()
                .set("font-size", "15px")
                .set("color", "#6b7280")
                .set("margin", "0")
                .set("line-height", "1.6")
                .set("text-align", "center");

        card.add(iconSpan, cardTitle, cardDesc);
        return card;
    }

    private VerticalLayout createFinalCTASection() {
        VerticalLayout cta = new VerticalLayout();
        cta.setWidthFull();
        cta.setAlignItems(Alignment.CENTER);
        cta.setPadding(true);
        cta.setSpacing(true);

        cta.getStyle()
                .set("background-color", "#10b981")
                .set("color", "white")
                .set("padding", "60px 32px")
                .set("text-align", "center");

        H2 ctaTitle = new H2("Prêt à commencer votre premier trajet ?");
        ctaTitle.getStyle()
                .set("font-size", "2.5rem")
                .set("font-weight", "800")
                .set("margin", "0 0 24px 0")
                .set("line-height", "1.2")
                .set("color", "white");

        Paragraph ctaDescription = new Paragraph(
                "Rejoignez la communauté CovoitÉcole et participez à une mobilité plus durable et économique");
        ctaDescription.getStyle()
                .set("font-size", "18px")
                .set("margin", "0 0 32px 0")
                .set("line-height", "1.6")
                .set("color", "rgba(255,255,255,0.9)")
                .set("max-width", "600px");

        Button ctaButton = new Button("Créer mon compte");
        ctaButton.getStyle()
                .set("background-color", "white")
                .set("color", "#10b981")
                .set("border", "none")
                .set("padding", "16px 32px")
                .set("border-radius", "8px")
                .set("font-size", "16px")
                .set("font-weight", "700");
        ctaButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("auth?mode=register")));

        cta.add(ctaTitle, ctaDescription, ctaButton);
        return cta;
    }

    private VerticalLayout createFooter() {
        VerticalLayout footer = new VerticalLayout();
        footer.setWidthFull();
        footer.setAlignItems(Alignment.CENTER);
        footer.setPadding(true);

        footer.getStyle()
                .set("background-color", "#f8fafc")
                .set("border-top", "1px solid #e5e7eb")
                .set("padding", "32px")
                .set("text-align", "center");

        Paragraph p = new Paragraph(
                "© " + java.time.Year.now().getValue() + " CovoitÉcole - Plateforme de covoiturage étudiant");
        p.getStyle()
                .set("color", "#6b7280")
                .set("margin", "0")
                .set("font-size", "14px");

        footer.add(p);
        return footer;
    }

}
