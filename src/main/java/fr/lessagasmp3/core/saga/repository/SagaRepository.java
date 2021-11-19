package fr.lessagasmp3.core.saga.repository;

import fr.lessagasmp3.core.saga.entity.Saga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface SagaRepository extends JpaRepository<Saga, Long> {
    Saga findByTitleIgnoreCase(String title);
    Set<Saga> findAllByOrderByTitleAsc();
    Set<Saga> findAllByTitle(String title);
    Set<Saga> findAllByTitleContainsIgnoreCaseOrderByTitleAsc(String search);
}
