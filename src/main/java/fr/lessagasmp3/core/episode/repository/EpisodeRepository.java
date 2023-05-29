package fr.lessagasmp3.core.episode.repository;

import fr.lessagasmp3.core.episode.entity.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long> {
    Episode findByNumberAndSeasonId(Integer number, Long seasonId);

    List<Episode> findAllBySeasonIdOrderByNumberAsc(Long seasonRef);
}
