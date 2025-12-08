package com.ecole.covoiturage.ui;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route(value = "", layout = MainLayout.class)
@AnonymousAllowed
public class HomeView extends VerticalLayout {

    public HomeView() {
        setSpacing(true);
        setPadding(true);
        setAlignItems(Alignment.CENTER);

        H1 title = new H1("Bienvenue sur la plateforme de covoiturage étudiant");
        title.addClassNames(LumoUtility.TextAlignment.CENTER, LumoUtility.Margin.Bottom.MEDIUM);

        Paragraph description = new Paragraph("Voyagez ensemble, économisez et protégez l'environnement !");
        description.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.FontSize.LARGE);

        HorizontalLayout cards = new HorizontalLayout();
        cards.setWidthFull();
        cards.setJustifyContentMode(JustifyContentMode.CENTER);
        cards.add(
            createFeatureCard(VaadinIcon.CAR, "Trajets disponibles", "Trouvez un trajet près de chez vous", "#667eea"),
            createFeatureCard(VaadinIcon.USERS, "Communauté", "Rejoignez des milliers d'étudiants", "#764ba2"),
            createFeatureCard(VaadinIcon.MONEY, "Économique", "Partagez les frais de transport", "#f093fb")
        );

        add(title, description, cards);
    }

    private Div createFeatureCard(VaadinIcon iconType, String title, String description, String color) {
        Div card = new Div();
        card.setWidth("300px");
        card.getStyle()
            .set("border-radius", "12px")
            .set("padding", "2rem")
            .set("background", "white")
            .set("box-shadow", "0 4px 12px rgba(0,0,0,0.1)")
            .set("text-align", "center")
            .set("border-top", "4px solid " + color)
            .set("transition", "transform 0.3s ease");

        Icon icon = iconType.create();
        icon.setSize("48px");
        icon.setColor(color);

        H3 cardTitle = new H3(title);
        cardTitle.getStyle().set("color", color).set("margin", "1rem 0");

        Paragraph cardDescription = new Paragraph(description);
        cardDescription.addClassNames(LumoUtility.TextColor.SECONDARY);

        card.add(icon, cardTitle, cardDescription);
        return card;
    }
}
