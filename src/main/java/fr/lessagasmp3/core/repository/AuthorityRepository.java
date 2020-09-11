package fr.lessagasmp3.core.repository;

import fr.lessagasmp3.core.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Set<Authority> findAllByUsers_Id(Long userId);
}
