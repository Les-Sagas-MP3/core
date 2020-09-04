package fr.lessagasmp3.core.controller;

import fr.lessagasmp3.core.entity.Category;
import fr.lessagasmp3.core.model.CategoryModel;
import fr.lessagasmp3.core.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @RequestMapping(value = "/api/categories", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CategoryModel> getAll() {
        List<Category> entities = categoryRepository.findAll();
        List<CategoryModel> models = new ArrayList<>();
        entities.forEach(entity -> models.add(CategoryModel.fromEntity(entity)));
        return models;
    }

}
