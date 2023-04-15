package fr.lessagasmp3.core.creator.repository;

import fr.lessagasmp3.core.creator.entity.Creator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CreatorRepository extends JpaRepository<Creator, Long> {
    Creator findByNameIgnoreCase(String name);

    Set<Creator> findAllByUserId(Long userId);

    Set<Creator> findAllBySagasWritten_Id(Long sagaId);

    Set<Creator> findAllBySagasComposed_Id(Long sagaId);
}
