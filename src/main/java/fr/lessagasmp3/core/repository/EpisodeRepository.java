package fr.lessagasmp3.core.repository;

import fr.lessagasmp3.core.entity.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long> {
    Episode findByNumberAndSeasonId(Integer number, Long seasonId);
}
