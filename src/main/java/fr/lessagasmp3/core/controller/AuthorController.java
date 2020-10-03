package fr.lessagasmp3.core.controller;

import fr.lessagasmp3.core.entity.Author;
import fr.lessagasmp3.core.exception.BadRequestException;
import fr.lessagasmp3.core.exception.NotFoundException;
import fr.lessagasmp3.core.model.AuthorModel;
import fr.lessagasmp3.core.repository.AuthorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/authors", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"name"})
    public AuthorModel getByName(@RequestParam("name") String name) {
        Author entity = authorRepository.findByName(name);
        return AuthorModel.fromEntity(entity);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/authors", method = RequestMethod.POST, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void create(@RequestBody AuthorModel authorModel) {

        // Verify that body is complete
        if(authorModel == null ||
                authorModel.getId() <= 0 ||
                authorModel.getName() == null || authorModel.getName().isEmpty()) {
            LOGGER.error("Impossible to create author : body is incomplete");
            throw new BadRequestException();
        }

        // Create author
        Author author = new Author();
        author.setName(authorModel.getName());
        author.setNbSagas(authorModel.getNbSagas());
        authorRepository.save(author);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/authors", method = RequestMethod.PUT, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void update(@RequestBody AuthorModel authorModel) {

        // Verify that body is complete
        if(authorModel == null ||
                authorModel.getId() <= 0 ||
                authorModel.getName() == null || authorModel.getName().isEmpty()) {
            LOGGER.error("Impossible to create author : body is incomplete");
            throw new BadRequestException();
        }

        // Verify that author exists
        Author author = authorRepository.findById(authorModel.getId()).orElse(null);
        if(author == null) {
            LOGGER.error("Impossible to update author : author {} not found", authorModel.getId());
            throw new NotFoundException();
        }

        // Update and save author
        author.setName(authorModel.getName());
        author.setNbSagas(authorModel.getNbSagas());
        authorRepository.save(author);
    }
}
