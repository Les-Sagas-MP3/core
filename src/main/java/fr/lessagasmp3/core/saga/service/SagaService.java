package fr.lessagasmp3.core.saga.service;

import fr.lessagasmp3.core.anecdote.service.AnecdoteService;
import fr.lessagasmp3.core.category.entity.Category;
import fr.lessagasmp3.core.category.service.CategoryService;
import fr.lessagasmp3.core.creator.entity.Creator;
import fr.lessagasmp3.core.creator.service.CreatorService;
import fr.lessagasmp3.core.distribution.service.DistributionService;
import fr.lessagasmp3.core.exception.BadRequestException;
import fr.lessagasmp3.core.exception.NotFoundException;
import fr.lessagasmp3.core.saga.entity.Saga;
import fr.lessagasmp3.core.saga.model.SagaModel;
import fr.lessagasmp3.core.saga.repository.SagaRepository;
import fr.lessagasmp3.core.season.service.SeasonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class SagaService {

    private final AnecdoteService anecdoteService;

    private final CategoryService categoryService;

    private final CreatorService creatorService;

    private final DistributionService distributionService;

    private final SeasonService seasonService;

    private final SagaRepository sagaRepository;

    @Autowired
    public SagaService(AnecdoteService anecdoteService,
                       CategoryService categoryService,
                       CreatorService creatorService,
                       DistributionService distributionService,
                       SeasonService seasonService,
                       SagaRepository sagaRepository) {
        this.anecdoteService = anecdoteService;
        this.creatorService = creatorService;
        this.categoryService = categoryService;
        this.distributionService = distributionService;
        this.seasonService = seasonService;
        this.sagaRepository = sagaRepository;
    }

    public Set<SagaModel> getAll() {
        Set<Saga> entities = sagaRepository.findAllByOrderByTitleAsc();
        Set<SagaModel> models = new LinkedHashSet<>();
        entities.forEach(entity -> models.add(SagaModel.fromEntity(entity)));
        return models;
    }

    public Saga findById(Long id) {
        return sagaRepository.findById(id).orElse(null);
    }

    public SagaModel findOrCreate(String title) {
        SagaModel saga = findByTitle(title);
        if (saga == null) {
            saga = new SagaModel();
            saga.setWorkspace(UUID.randomUUID().toString());
            saga.setTitle(title);
            saga = create(saga);
            if(saga != null) {
                log.debug("Creator {} created", saga);
            } else {
                log.error("Creator \"{}\" not created", title);
            }
        } else {
            log.debug("Creator already exists : {}", saga);
        }
        return saga;
    }

    public SagaModel findByTitle(String title) {
        Saga entity = sagaRepository.findByTitleIgnoreCase(title);
        SagaModel model = null;
        if(entity != null) {
            entity.setAuthors(creatorService.findAllEntitiesBySagasWritten(entity.getId()));
            entity.setComposers(creatorService.findAllEntitiesBySagasComposed(entity.getId()));
            entity.setCategories(categoryService.findAllEntitiesBySagas(entity.getId()));
            entity.setSeasons(seasonService.findAllEntitiesBySaga(entity.getId()));
            entity.setDistributionEntries(distributionService.findAllEntitiesBySaga(entity.getId()));
            entity.setAnecdotes(anecdoteService.findAllEntitiesBySaga(entity.getId()));
            model = SagaModel.fromEntity(entity);
        }
        return model;
    }

    public SagaModel create(SagaModel model) {

        // Verify that body is complete
        if(model == null ||
                model.getTitle() == null || model.getTitle().isEmpty()) {
            log.error("Impossible to create saga : body is incomplete");
            throw new BadRequestException();
        }

        // Create entity
        Saga entity = Saga.fromModel(model);
        return SagaModel.fromEntity(sagaRepository.save(entity));
    }

    public SagaModel update(SagaModel model) {

        // Verify that body is complete
        if(model == null || model.getId() <= 0) {
            log.error("Impossible to update saga : body is incomplete");
            throw new BadRequestException();
        }

        // Verify that entity exists
        Saga entity = sagaRepository.findById(model.getId()).orElse(null);
        if(entity == null) {
            log.error("Impossible to update saga : saga {} not found", model.getId());
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
        entity.setBannerUrl(model.getBannerUrl());
        entity.setCoverUrl(model.getCoverUrl());
        entity.setUrl(model.getUrl());
        entity.setUrlWiki(model.getUrlWiki());
        entity.setLevelArt(model.getLevelArt());
        entity.setLevelTech(model.getLevelTech());
        entity.setNbReviews(model.getNbReviews());
        entity.setUrlReviews(model.getUrlReviews());
        entity.setNbBravos(model.getNbBravos());
        return SagaModel.fromEntity(sagaRepository.save(entity));
    }

    public void addAuthor(Long id, Long authorId) {

        // Verify that body is complete
        if(id <= 0 || authorId <= 0) {
            log.error("Impossible to associate saga {} with author {} : IDs are incorrects", id, authorId);
            throw new BadRequestException();
        }

        // Verify that entity exists
        Saga entity = sagaRepository.findById(id).orElse(null);
        Creator creator = creatorService.findEntityById(authorId);
        if(entity == null || creator == null) {
            log.error("Impossible to associate saga {} with author {} : saga or author not found", id, authorId);
            throw new NotFoundException();
        }

        // Update and save entity
        entity.getAuthors().stream()
                .filter(author -> authorId.equals(author.getId()))
                .findAny()
                .ifPresentOrElse(author -> {}, () -> entity.getAuthors().add(creator));
        sagaRepository.save(entity);
    }

    public void addComposer(Long id, Long composerId) {

        // Verify that body is complete
        if(id <= 0 || composerId <= 0) {
            log.error("Impossible to associate saga {} with composer {} : IDs are incorrects", id, composerId);
            throw new BadRequestException();
        }

        // Verify that entity exists
        Saga entity = sagaRepository.findById(id).orElse(null);
        Creator creator = creatorService.findEntityById(composerId);
        if(entity == null || creator == null) {
            log.error("Impossible to associate saga {} with composer {} : saga or composer not found", id, composerId);
            throw new NotFoundException();
        }

        // Update and save entity
        entity.getComposers().stream()
                .filter(composer -> composerId.equals(composer.getId()))
                .findAny()
                .ifPresentOrElse(author -> {}, () -> entity.getComposers().add(creator));
        sagaRepository.save(entity);
    }

    public void addCategory(Long id, Long categoryId) {

        // Verify that body is complete
        if(id <= 0 || categoryId <= 0) {
            log.error("Impossible to associate saga {} with category {} : IDs are incorrects", id, categoryId);
            throw new BadRequestException();
        }

        // Verify that entity exists
        Saga entity = sagaRepository.findById(id).orElse(null);
        Category category = categoryService.findEntityById(categoryId);
        if(entity == null || category == null) {
            log.error("Impossible to associate saga {} with category {} : saga or category not found", id, categoryId);
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
