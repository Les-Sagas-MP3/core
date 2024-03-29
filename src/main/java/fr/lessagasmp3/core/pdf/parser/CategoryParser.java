package fr.lessagasmp3.core.pdf.parser;

import fr.lessagasmp3.core.category.model.CategoryModel;
import fr.lessagasmp3.core.category.service.CategoryService;
import fr.lessagasmp3.core.pdf.extractor.LinesExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class CategoryParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryParser.class);

    @Autowired
    private CategoryService categoryService;

    public Set<CategoryModel> parse(String categoriesString) {
        Set<CategoryModel> categories = new LinkedHashSet<>();
        categoriesString = categoriesString.replace(" & ", "|")
                .replace(" et ", "|")
                .replace(", ", "|")
                .replace("| ", "|")
                .replace(" |", "|");
        String[] split = categoriesString.split("\\|");
        for (String categoryString : split) {
            categoryString = LinesExtractor.removeLastSpaces(categoryString);
            CategoryModel category = categoryService.findByName(categoryString);
            if (category == null) {
                category = new CategoryModel();
                category.setName(categoryString);
                category = categoryService.create(category);
                LOGGER.debug("Category {} created", category.getName());
            } else {
                LOGGER.debug("Creator already exists : ID={} NAME={}", category.getId(), category.getName());
            }
            categories.add(category);
        }
        return categories;
    }


}
