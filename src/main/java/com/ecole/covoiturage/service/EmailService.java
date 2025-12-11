package com.ecole.covoiturage.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Envoie un email de bienvenue lors de la creation d'un compte
     */
    public void sendWelcomeEmail(String toEmail, String userName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Bienvenue sur Covoiturage Ecole !");
        message.setText(
            "Bonjour " + userName + ",\n\n" +
            "Votre compte a ete cree avec succes sur la plateforme Covoiturage Ecole !\n\n" +
            "Vous pouvez maintenant :\n" +
            "- Proposer des trajets en tant que conducteur\n" +
            "- Rejoindre des trajets en tant que passager\n" +
            "- Gerer vos reservations\n\n" +
            "Bonne route et a bientot !\n\n" +
            "L'equipe Covoiturage Ecole"
        );
        mailSender.send(message);
    }
}

