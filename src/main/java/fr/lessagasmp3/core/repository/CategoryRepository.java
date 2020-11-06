package fr.lessagasmp3.core.repository;

import fr.lessagasmp3.core.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByNameIgnoreCase(String name);
}
