package fr.lessagasmp3.core.controller;

import com.google.gson.Gson;
import fr.lessagasmp3.core.constant.Strings;
import fr.lessagasmp3.core.entity.Category;
import fr.lessagasmp3.core.entity.Creator;
import fr.lessagasmp3.core.entity.Saga;
import fr.lessagasmp3.core.exception.BadRequestException;
import fr.lessagasmp3.core.exception.NotFoundException;
import fr.lessagasmp3.core.model.SagaModel;
import fr.lessagasmp3.core.pagination.DataPage;
import fr.lessagasmp3.core.repository.CategoryRepository;
import fr.lessagasmp3.core.repository.CreatorRepository;
import fr.lessagasmp3.core.repository.SagaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashSet;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class SagaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SagaController.class);

    @Autowired
    private Gson gson;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CreatorRepository creatorRepository;

    @Autowired
    private SagaRepository sagaRepository;

    @RequestMapping(value = "/saga", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<SagaModel> getAll() {
        Set<Saga> entities = sagaRepository.findAllByOrderByTitleAsc();
        Set<SagaModel> models = new LinkedHashSet<>();
        entities.forEach(entity -> models.add(SagaModel.fromEntity(entity)));
        return models;
    }

    @RequestMapping(value = "/saga/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public SagaModel getById(@PathVariable Long id) {
        return SagaModel.fromEntity(sagaRepository.findById(id).orElse(null));
    }

    @RequestMapping(value = "/saga", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"title"})
    public SagaModel getByTitle(@RequestParam("title") String title) {
        Saga entity = sagaRepository.findByTitle(title);
        if(entity != null) {
            return SagaModel.fromEntity(entity);
        }
        return null;
    }

    @RequestMapping(value = "/saga", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"offset", "limit"})
    public DataPage<SagaModel> getPaginated(@RequestParam("offset") int offset, @RequestParam("limit") int limit) {

        // Verify that params are correct
        if(offset < 0 || limit <= 0) {
            LOGGER.error("Cannot get paginated sagas : offset or limit is incorrect");
            throw new BadRequestException();
        }

        // Get and transform contents
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("title").ascending());
        Page<Saga> entities = sagaRepository.findAll(pageable);
        DataPage<SagaModel> models = new DataPage<>(entities);
        entities.getContent().forEach(entity -> models.getContent().add(SagaModel.fromEntity(entity)));
        return models;
    }

    @RequestMapping(value = "/saga", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"search"})
    public Set<SagaModel> search(@RequestParam("search") String search) {
        Set<SagaModel> models = new LinkedHashSet<>();
        Set<Saga> entities = sagaRepository.findAllByTitleContainsIgnoreCaseOrderByTitleAsc(search);
        entities.forEach(entity -> models.add(SagaModel.fromEntity(entity)));
        return models;
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/saga", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public SagaModel create(@RequestBody String modelStr) {

        LOGGER.debug("POST /api/saga");
        LOGGER.debug("BODY : {}", modelStr);
        SagaModel model = gson.fromJson(Strings.convertToUtf8(modelStr), SagaModel.class);

        // Verify that body is complete
        if(model == null ||
                model.getTitle() == null || model.getTitle().isEmpty()) {
            LOGGER.error("Impossible to create saga : body is incomplete");
            throw new BadRequestException();
        }

        // Create entity
        Saga entity = Saga.fromModel(model);
        return SagaModel.fromEntity(sagaRepository.save(entity));
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/saga", method = RequestMethod.PUT, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void update(@RequestBody String modelStr) {

        LOGGER.debug("PUT /api/saga");
        LOGGER.debug("BODY : {}", modelStr);
        SagaModel model = gson.fromJson(Strings.convertToUtf8(modelStr), SagaModel.class);

        // Verify that body is complete
        if(model == null || model.getId() <= 0) {
            LOGGER.error("Impossible to create saga : body is incomplete");
            throw new BadRequestException();
        }

        // Verify that entity exists
        Saga entity = sagaRepository.findById(model.getId()).orElse(null);
        if(entity == null) {
            LOGGER.error("Impossible to update saga : saga {} not found", model.getId());
            throw new NotFoundException();
        }

        // Update and save entity
        entity.setTitle(model.getTitle());
        entity.setStartDate(model.getStartDate());
        entity.setDuration(model.getDuration());
        entity.setSynopsis(model.getSynopsis());
        entity.setOrigin(model.getOrigin());
        entity.setGenese(model.getGenese());
        entity.setAwards(model.getAwards());
        entity.setBackgroundUrl(model.getBackgroundUrl());
        entity.setCoverUrl(model.getCoverUrl());
        entity.setUrl(model.getUrl());
        entity.setUrlWiki(model.getUrlWiki());
        entity.setLevelArt(model.getLevelArt());
        entity.setLevelTech(model.getLevelTech());
        entity.setNbReviews(model.getNbReviews());
        entity.setUrlReviews(model.getUrlReviews());
        entity.setNbBravos(model.getNbBravos());
        sagaRepository.save(entity);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/saga", method = RequestMethod.POST, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE, params = {"id", "authorId"})
    public void addAuthor(@RequestParam("id") Long id, @RequestParam("authorId") Long authorId) {

        // Verify that body is complete
        if(id <= 0 || authorId <= 0) {
            LOGGER.error("Impossible to associate saga {} with author {} : IDs are incorrects", id, authorId);
            throw new BadRequestException();
        }

        // Verify that entity exists
        Saga entity = sagaRepository.findById(id).orElse(null);
        Creator creator = creatorRepository.findById(authorId).orElse(null);
        if(entity == null || creator == null) {
            LOGGER.error("Impossible to associate saga {} with author {} : saga or author not found", id, authorId);
            throw new NotFoundException();
        }

        // Update and save entity
        entity.getAuthors().stream()
                .filter(author -> authorId.equals(author.getId()))
                .findAny()
                .ifPresentOrElse(author -> {}, () -> entity.getAuthors().add(creator));
        sagaRepository.save(entity);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/saga", method = RequestMethod.POST, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE, params = {"id", "composerId"})
    public void addComposer(@RequestParam("id") Long id, @RequestParam("composerId") Long composerId) {

        // Verify that body is complete
        if(id <= 0 || composerId <= 0) {
            LOGGER.error("Impossible to associate saga {} with composer {} : IDs are incorrects", id, composerId);
            throw new BadRequestException();
        }

        // Verify that entity exists
        Saga entity = sagaRepository.findById(id).orElse(null);
        Creator creator = creatorRepository.findById(composerId).orElse(null);
        if(entity == null || creator == null) {
            LOGGER.error("Impossible to associate saga {} with composer {} : saga or composer not found", id, composerId);
            throw new NotFoundException();
        }

        // Update and save entity
        entity.getComposers().stream()
                .filter(composer -> composerId.equals(composer.getId()))
                .findAny()
                .ifPresentOrElse(author -> {}, () -> entity.getComposers().add(creator));
        sagaRepository.save(entity);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/saga", method = RequestMethod.POST, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE, params = {"id", "categoryId"})
    public void addCategory(@RequestParam("id") Long id, @RequestParam("categoryId") Long categoryId) {

        // Verify that body is complete
        if(id <= 0 || categoryId <= 0) {
            LOGGER.error("Impossible to associate saga {} with category {} : IDs are incorrects", id, categoryId);
            throw new BadRequestException();
        }

        // Verify that entity exists
        Saga entity = sagaRepository.findById(id).orElse(null);
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if(entity == null || category == null) {
            LOGGER.error("Impossible to associate saga {} with category {} : saga or category not found", id, categoryId);
            throw new NotFoundException();
        }

        // Update and save entity
        entity.getCategories().stream()
                .filter(sagaCategory -> categoryId.equals(sagaCategory.getId()))
                .findAny()
                .ifPresentOrElse(author -> {}, () -> entity.getCategories().add(category));
        sagaRepository.save(entity);
    }
}
