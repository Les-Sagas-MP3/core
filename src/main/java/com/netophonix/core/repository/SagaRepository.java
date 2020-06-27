package com.netophonix.core.repository;

import com.netophonix.core.model.Saga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface SagaRepository extends JpaRepository<Saga, Long> {
    Set<Saga> findAllByTitle(String title);
}
