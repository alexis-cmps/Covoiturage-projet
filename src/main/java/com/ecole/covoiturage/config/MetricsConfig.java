package com.ecole.covoiturage.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public Counter trajetCreatedCounter(MeterRegistry registry) {
        return Counter.builder("trajets.created.total")
                .description("Nombre total de trajets créés")
                .register(registry);
    }

    @Bean
    public Counter reservationCreatedCounter(MeterRegistry registry) {
        return Counter.builder("reservations.created.total")
                .description("Nombre total de réservations créées")
                .register(registry);
    }

    @Bean
    public Counter userRegisteredCounter(MeterRegistry registry) {
        return Counter.builder("users.registered.total")
                .description("Nombre total d'utilisateurs inscrits")
                .register(registry);
    }

    @Bean
    public Timer trajetSearchTimer(MeterRegistry registry) {
        return Timer.builder("trajets.search.duration")
                .description("Temps de recherche de trajets")
                .register(registry);
    }
}
