package fr.lessagasmp3.core.repository;

import fr.lessagasmp3.core.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
}
