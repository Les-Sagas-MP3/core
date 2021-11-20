package fr.lessagasmp3.core.season.repository;

import fr.lessagasmp3.core.season.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Long> {
    Season findByNumberAndSagaId(Integer number, Long sagaId);
}
