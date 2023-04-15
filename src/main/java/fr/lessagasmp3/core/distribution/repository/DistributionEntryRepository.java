package fr.lessagasmp3.core.distribution.repository;

import fr.lessagasmp3.core.distribution.entity.DistributionEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface DistributionEntryRepository extends JpaRepository<DistributionEntry, Long> {
    DistributionEntry findByActorIdAndSagaIdAndRolesIgnoreCase(Long actorId, Long sagaId, String roles);

    Set<DistributionEntry> findAllBySagaId(Long sagaId);
}
