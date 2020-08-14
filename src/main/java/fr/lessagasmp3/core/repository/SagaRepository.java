package fr.lessagasmp3.core.repository;

import fr.lessagasmp3.core.entity.Saga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface SagaRepository extends JpaRepository<Saga, Long> {
    Set<Saga> findAllByTitle(String title);
}
