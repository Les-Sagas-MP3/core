package fr.lessagasmp3.core.repository;

import fr.lessagasmp3.core.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Author findByName(String name);

    Set<Author> findAllByUserId(Long userId);
}
