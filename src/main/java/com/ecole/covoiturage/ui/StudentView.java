package com.ecole.covoiturage.ui;

import com.ecole.covoiturage.entity.Student;
import com.ecole.covoiturage.service.StudentService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("students")
public class StudentView extends VerticalLayout {

    private final StudentService studentService;
    private final Grid<Student> grid = new Grid<>(Student.class);
    private final TextField nameField = new TextField("Nom");
    private final EmailField emailField = new EmailField("Email");
    private final Button addButton = new Button("Ajouter");

    @Autowired
    public StudentView(StudentService studentService) {
        this.studentService = studentService;

        setSpacing(true);
        setPadding(true);
        setSizeFull();

        add(buildForm(), grid);
        configureGrid();
        refreshGrid();
    }

    private HorizontalLayout buildForm() {
        addButton.addClickListener(e -> addStudent());
        return new HorizontalLayout(nameField, emailField, addButton);
    }

    private void configureGrid() {
        grid.setColumns("id", "name", "email");
        grid.setSizeFull();
    }

    private void refreshGrid() {
        grid.setItems(studentService.findAll());
    }

    private void addStudent() {
        if (nameField.isEmpty() || emailField.isEmpty()) {
            Notification.show("Nom et email obligatoires");
            return;
        }

        Student s = new Student();
        s.setName(nameField.getValue());
        s.setEmail(emailField.getValue());
        studentService.save(s);

        Notification.show("Étudiant ajouté !");
        nameField.clear();
        emailField.clear();
        refreshGrid();
    }
}
