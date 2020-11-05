package fr.lessagasmp3.core.repository;

import fr.lessagasmp3.core.entity.Creator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CreatorRepository extends JpaRepository<Creator, Long> {
    Creator findByNameIgnoreCase(String name);

    Set<Creator> findAllByUserId(Long userId);
}
