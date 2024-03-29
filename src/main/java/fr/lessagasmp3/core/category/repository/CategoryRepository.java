package fr.lessagasmp3.core.category.repository;

import fr.lessagasmp3.core.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByNameIgnoreCase(String name);

    Set<Category> findAllBySagas_Id(Long id);
}
