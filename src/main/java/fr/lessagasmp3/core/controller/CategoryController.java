package fr.lessagasmp3.core.controller;

import fr.lessagasmp3.core.entity.Category;
import fr.lessagasmp3.core.model.CategoryModel;
import fr.lessagasmp3.core.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
