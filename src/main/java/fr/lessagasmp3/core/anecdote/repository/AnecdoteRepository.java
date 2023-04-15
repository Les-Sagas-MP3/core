package fr.lessagasmp3.core.anecdote.repository;

import fr.lessagasmp3.core.anecdote.entity.Anecdote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AnecdoteRepository extends JpaRepository<Anecdote, Long> {
    Anecdote findByAnecdoteIgnoreCaseAndSagaId(String content, Long sagaId);

    Set<Anecdote> findAllBySagaId(Long sagaId);
}
