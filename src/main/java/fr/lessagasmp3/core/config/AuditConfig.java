package fr.lessagasmp3.core.config;

import fr.lessagasmp3.core.AuditListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AuditConfig implements WebMvcConfigurer {

    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditListener();
    }
}