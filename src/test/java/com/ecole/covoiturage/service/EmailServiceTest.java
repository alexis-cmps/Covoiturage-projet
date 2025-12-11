package com.ecole.covoiturage.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    private EmailService emailService;

    private static final String FROM_EMAIL = "test@gmail.com";

    @BeforeEach
    void setUp() {
        emailService = new EmailService(mailSender);
        ReflectionTestUtils.setField(emailService, "fromEmail", FROM_EMAIL);
    }

    @Test
    void sendWelcomeEmail_shouldSendEmailWithCorrectContent() {
        // Given
        String toEmail = "nouvelutilisateur@test.com";
        String userName = "Jean Dupont";

        // When
        emailService.sendWelcomeEmail(toEmail, userName);

        // Then
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertEquals(FROM_EMAIL, sentMessage.getFrom());
        assertArrayEquals(new String[]{toEmail}, sentMessage.getTo());
        assertEquals("Bienvenue sur Covoiturage Ecole !", sentMessage.getSubject());
        assertTrue(sentMessage.getText().contains("Bonjour Jean Dupont"));
        assertTrue(sentMessage.getText().contains("Votre compte a ete cree avec succes"));
    }

    @Test
    void sendWelcomeEmail_shouldContainAllRequiredInformation() {
        // Given
        String toEmail = "marie.martin@ecole.fr";
        String userName = "Marie Martin";

        // When
        emailService.sendWelcomeEmail(toEmail, userName);

        // Then
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());

        String emailText = messageCaptor.getValue().getText();
        assertTrue(emailText.contains("Proposer des trajets"));
        assertTrue(emailText.contains("Rejoindre des trajets"));
        assertTrue(emailText.contains("Gerer vos reservations"));
        assertTrue(emailText.contains("L'equipe Covoiturage Ecole"));
    }

    @Test
    void sendWelcomeEmail_shouldCallMailSenderOnce() {
        // Given
        String toEmail = "test@test.com";
        String userName = "Test User";

        // When
        emailService.sendWelcomeEmail(toEmail, userName);

        // Then
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendWelcomeEmail_shouldHandleSpecialCharactersInName() {
        // Given
        String toEmail = "user@test.com";
        String userName = "François Müller";

        // When
        emailService.sendWelcomeEmail(toEmail, userName);

        // Then
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());

        assertTrue(messageCaptor.getValue().getText().contains("François Müller"));
    }

    @Test
    void sendWelcomeEmail_whenMailSenderThrowsException_shouldPropagateException() {
        // Given
        String toEmail = "test@test.com";
        String userName = "Test User";
        doThrow(new RuntimeException("SMTP Error")).when(mailSender).send(any(SimpleMailMessage.class));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            emailService.sendWelcomeEmail(toEmail, userName);
        });
    }
}

