package fr.lessagasmp3.core.repository;

import fr.lessagasmp3.core.entity.DistributionEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistributionEntryRepository extends JpaRepository<DistributionEntry, Long> {
    DistributionEntry findByActorIdAndSagaIdAndRolesIgnoreCase(Long actorId, Long sagaId, String roles);
}
