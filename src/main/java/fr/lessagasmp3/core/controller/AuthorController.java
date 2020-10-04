package fr.lessagasmp3.core.controller;

import fr.lessagasmp3.core.entity.Creator;
import fr.lessagasmp3.core.exception.BadRequestException;
import fr.lessagasmp3.core.exception.NotFoundException;
import fr.lessagasmp3.core.model.CreatorModel;
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
    public Set<CreatorModel> getAllByIds(@RequestParam("ids") Set<Long> ids) {
        Set<CreatorModel> models = new LinkedHashSet<>();
        for(Long id : ids) {
            Creator entity = authorRepository.findById(id).orElse(null);
            if(entity == null) {
                LOGGER.error("Impossible to get author {} : it doesn't exist", id);
                continue;
            }
            models.add(CreatorModel.fromEntity(entity));
        }
        return models;
    }

    @RequestMapping(value = "/authors", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"name"})
    public CreatorModel getByName(@RequestParam("name") String name) {
        Creator entity = authorRepository.findByName(name);
        if(entity != null) {
            return CreatorModel.fromEntity(entity);
        }
        return null;
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/authors", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public CreatorModel create(@RequestBody CreatorModel creatorModel) {

        // Verify that body is complete
        if(creatorModel == null ||
                creatorModel.getName() == null || creatorModel.getName().isEmpty()) {
            LOGGER.error("Impossible to create author : body is incomplete");
            throw new BadRequestException();
        }

        // Create author
        Creator creator = new Creator();
        creator.setName(creatorModel.getName());
        creator.setNbSagas(creatorModel.getNbSagas());
        return CreatorModel.fromEntity(authorRepository.save(creator));
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/authors", method = RequestMethod.PUT, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void update(@RequestBody CreatorModel creatorModel) {

        // Verify that body is complete
        if(creatorModel == null ||
                creatorModel.getId() <= 0 ||
                creatorModel.getName() == null || creatorModel.getName().isEmpty()) {
            LOGGER.error("Impossible to create author : body is incomplete");
            throw new BadRequestException();
        }

        // Verify that author exists
        Creator creator = authorRepository.findById(creatorModel.getId()).orElse(null);
        if(creator == null) {
            LOGGER.error("Impossible to update author : author {} not found", creatorModel.getId());
            throw new NotFoundException();
        }

        // Update and save author
        creator.setName(creatorModel.getName());
        creator.setNbSagas(creatorModel.getNbSagas());
        authorRepository.save(creator);
    }
}
