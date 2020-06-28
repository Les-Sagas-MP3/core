package fr.lessagasmp3.core.repository;

import fr.lessagasmp3.core.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Author findByName(String name);
}
