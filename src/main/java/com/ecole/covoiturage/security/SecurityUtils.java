package com.ecole.covoiturage.security;

import com.ecole.covoiturage.entity.Student;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;

public class SecurityUtils {

    private static final String USER_ID_KEY = "userId";
    private static final String USER_NAME_KEY = "userName";
    private static final String USER_EMAIL_KEY = "userEmail";

    /**
     * Stocke les informations de l'utilisateur en session (sans le mot de passe)
     */
    public static void setAuthenticatedStudent(Student student) {
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null && student != null) {
            session.setAttribute(USER_ID_KEY, student.getId());
            session.setAttribute(USER_NAME_KEY, student.getName());
            session.setAttribute(USER_EMAIL_KEY, student.getEmail());
        }
    }

    /**
     * Recupere les informations de l'utilisateur connecte (sans mot de passe)
     */
    public static Student getAuthenticatedStudent() {
        VaadinSession session = VaadinSession.getCurrent();
        if (session == null) {
            return null;
        }

        Long id = (Long) session.getAttribute(USER_ID_KEY);
        String name = (String) session.getAttribute(USER_NAME_KEY);
        String email = (String) session.getAttribute(USER_EMAIL_KEY);

        if (id == null || name == null || email == null) {
            return null;
        }

        // Retourne un Student sans mot de passe
        return new Student(id, name, email);
    }

    public static boolean isAuthenticated() {
        VaadinSession session = VaadinSession.getCurrent();
        return session != null && session.getAttribute(USER_ID_KEY) != null;
    }

    public static void logout() {
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            session.close();
        }
        UI ui = UI.getCurrent();
        if (ui != null) {
            // Recharger la page pour mettre a jour le MainLayout
            ui.getPage().setLocation("/login");
        }
    }

    /**
     * Recupere l'ID de l'utilisateur connecte
     */
    public static Long getAuthenticatedUserId() {
        VaadinSession session = VaadinSession.getCurrent();
        return session != null ? (Long) session.getAttribute(USER_ID_KEY) : null;
    }
}
