package fr.lessagasmp3.core.category.controller;

import fr.lessagasmp3.core.category.model.CategoryModel;
import fr.lessagasmp3.core.category.repository.CategoryRepository;
import fr.lessagasmp3.core.category.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @RequestMapping(value = "/categories", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CategoryModel> getAll() {
        return categoryService.getAll();
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"ids"})
    public Set<CategoryModel> getAllByIds(@RequestParam("ids") Set<Long> ids) {
        return categoryService.getAllByIds(ids);
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"name"})
    public CategoryModel getByName(@RequestParam("name") String name) {
        return categoryService.findByName(name);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/categories", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public CategoryModel create(@RequestBody CategoryModel categoryModel) {
        return categoryService.create(categoryModel);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/categories", method = RequestMethod.PUT, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public CategoryModel update(@RequestBody CategoryModel categoryModel) {
        return categoryService.update(categoryModel);
    }

}
