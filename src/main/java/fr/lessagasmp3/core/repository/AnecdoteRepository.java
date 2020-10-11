package fr.lessagasmp3.core.repository;

import fr.lessagasmp3.core.entity.Anecdote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnecdoteRepository extends JpaRepository<Anecdote, Long> {
    Anecdote findByAnecdoteAndSagaId(String content, Long sagaId);
}
