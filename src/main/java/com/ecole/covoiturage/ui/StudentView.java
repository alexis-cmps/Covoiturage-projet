package com.ecole.covoiturage.ui;

import com.ecole.covoiturage.entity.Student;
import com.ecole.covoiturage.repository.StudentRepository;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.stereotype.Component;

@Route("students")
@Component
public class StudentView extends VerticalLayout {

    private final StudentRepository repo;

    public StudentView(StudentRepository repo) {
        this.repo = repo;

        Grid<Student> grid = new Grid<>(Student.class);
        grid.setItems(repo.findAll());

        TextField name = new TextField("Name");
        TextField email = new TextField("Email");

        Button save = new Button("Add", e -> {
            repo.save(new Student(null, name.getValue(), email.getValue()));
            grid.setItems(repo.findAll());
        });

        add(name, email, save, grid);


    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        boolean hasLink = getChildren()
                .anyMatch(c -> c instanceof RouterLink);
        if (!hasLink) {
            add(new RouterLink("Retour à l'accueil", HelloView.class));
        }
    }

}

