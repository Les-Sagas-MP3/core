package fr.lessagasmp3.core.category.service;

import fr.lessagasmp3.core.category.entity.Category;
import fr.lessagasmp3.core.category.model.CategoryModel;
import fr.lessagasmp3.core.category.repository.CategoryRepository;
import fr.lessagasmp3.core.exception.BadRequestException;
import fr.lessagasmp3.core.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryModel> getAll() {
        List<Category> entities = categoryRepository.findAll();
        List<CategoryModel> models = new ArrayList<>();
        entities.forEach(entity -> models.add(CategoryModel.fromEntity(entity)));
        return models;
    }

    public Set<CategoryModel> getAllByIds(Set<Long> ids) {
        Set<CategoryModel> models = new LinkedHashSet<>();
        for(Long id : ids) {
            Category entity = categoryRepository.findById(id).orElse(null);
            if(entity == null) {
                log.error("Impossible to get category {} : it doesn't exist", id);
                continue;
            }
            models.add(CategoryModel.fromEntity(entity));
        }
        return models;
    }

    public CategoryModel findByName(String name) {
        Category entity = categoryRepository.findByNameIgnoreCase(name);
        if(entity != null) {
            return CategoryModel.fromEntity(entity);
        }
        return null;
    }

    public CategoryModel create(CategoryModel categoryModel) {

        // Verify that body is complete
        if(categoryModel == null ||
                categoryModel.getName() == null || categoryModel.getName().isEmpty()) {
            log.error("Impossible to create category : body is incomplete");
            throw new BadRequestException();
        }

        // Create category
        Category category = new Category();
        category.setName(categoryModel.getName());
        category.setNbSagas(categoryModel.getNbSagas());
        return CategoryModel.fromEntity(categoryRepository.save(category));
    }

    public CategoryModel update(CategoryModel categoryModel) {

        // Verify that body is complete
        if(categoryModel == null ||
                categoryModel.getId() <= 0 ||
                categoryModel.getName() == null || categoryModel.getName().isEmpty()) {
            log.error("Impossible to create category : body is incomplete");
            throw new BadRequestException();
        }

        // Verify that category exists
        Category category = categoryRepository.findById(categoryModel.getId()).orElse(null);
        if(category == null) {
            log.error("Impossible to update category : category {} not found", categoryModel.getId());
            throw new NotFoundException();
        }

        // Update and save category
        category.setName(categoryModel.getName());
        category.setNbSagas(categoryModel.getNbSagas());
        return CategoryModel.fromEntity(categoryRepository.save(category));
    }

}
