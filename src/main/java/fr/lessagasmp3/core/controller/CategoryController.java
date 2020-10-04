package fr.lessagasmp3.core.controller;

import fr.lessagasmp3.core.entity.Category;
import fr.lessagasmp3.core.exception.BadRequestException;
import fr.lessagasmp3.core.exception.NotFoundException;
import fr.lessagasmp3.core.model.CategoryModel;
import fr.lessagasmp3.core.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @RequestMapping(value = "/categories", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CategoryModel> getAll() {
        List<Category> entities = categoryRepository.findAll();
        List<CategoryModel> models = new ArrayList<>();
        entities.forEach(entity -> models.add(CategoryModel.fromEntity(entity)));
        return models;
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"ids"})
    public Set<CategoryModel> getAllByIds(@RequestParam("ids") Set<Long> ids) {
        Set<CategoryModel> models = new LinkedHashSet<>();
        for(Long id : ids) {
            Category entity = categoryRepository.findById(id).orElse(null);
            if(entity == null) {
                LOGGER.error("Impossible to get category {} : it doesn't exist", id);
                continue;
            }
            models.add(CategoryModel.fromEntity(entity));
        }
        return models;
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"name"})
    public CategoryModel getByName(@RequestParam("name") String name) {
        Category entity = categoryRepository.findByName(name);
        if(entity != null) {
            return CategoryModel.fromEntity(entity);
        }
        return null;
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/categories", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public CategoryModel create(@RequestBody CategoryModel categoryModel) {

        // Verify that body is complete
        if(categoryModel == null ||
                categoryModel.getId() <= 0 ||
                categoryModel.getName() == null || categoryModel.getName().isEmpty()) {
            LOGGER.error("Impossible to create category : body is incomplete");
            throw new BadRequestException();
        }

        // Create category
        Category category = new Category();
        category.setName(categoryModel.getName());
        category.setNbSagas(categoryModel.getNbSagas());
        return CategoryModel.fromEntity(categoryRepository.save(category));
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/categories", method = RequestMethod.PUT, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void update(@RequestBody CategoryModel categoryModel) {

        // Verify that body is complete
        if(categoryModel == null ||
                categoryModel.getId() <= 0 ||
                categoryModel.getName() == null || categoryModel.getName().isEmpty()) {
            LOGGER.error("Impossible to create category : body is incomplete");
            throw new BadRequestException();
        }

        // Verify that category exists
        Category category = categoryRepository.findById(categoryModel.getId()).orElse(null);
        if(category == null) {
            LOGGER.error("Impossible to update category : category {} not found", categoryModel.getId());
            throw new NotFoundException();
        }

        // Update and save category
        category.setName(categoryModel.getName());
        category.setNbSagas(categoryModel.getNbSagas());
        categoryRepository.save(category);
    }

}
