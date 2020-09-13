package fr.lessagasmp3.core.controller;

import fr.lessagasmp3.core.entity.Author;
import fr.lessagasmp3.core.model.AuthorModel;
import fr.lessagasmp3.core.repository.AuthorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashSet;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class AuthorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorController.class);

    @Autowired
    private AuthorRepository authorRepository;

    @RequestMapping(value = "/authors", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"ids"})
    public Set<AuthorModel> getAllByIds(@RequestParam("ids") Set<Long> ids) {
        Set<AuthorModel> models = new LinkedHashSet<>();
        for(Long id : ids) {
            Author entity = authorRepository.findById(id).orElse(null);
            if(entity == null) {
                LOGGER.error("Impossible to get author {} : it doesn't exist", id);
                continue;
            }
            models.add(AuthorModel.fromEntity(entity));
        }
        return models;
    }

}
