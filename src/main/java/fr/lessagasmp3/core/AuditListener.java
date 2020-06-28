package fr.lessagasmp3.core;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditListener implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("System");
    }

}
