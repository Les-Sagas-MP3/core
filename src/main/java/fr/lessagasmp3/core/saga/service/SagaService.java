package fr.lessagasmp3.core.saga.service;

import fr.lessagasmp3.core.category.entity.Category;
import fr.lessagasmp3.core.entity.Creator;
import fr.lessagasmp3.core.saga.entity.Saga;
import fr.lessagasmp3.core.exception.BadRequestException;
import fr.lessagasmp3.core.exception.NotFoundException;
import fr.lessagasmp3.core.saga.model.SagaModel;
import fr.lessagasmp3.core.category.repository.CategoryRepository;
import fr.lessagasmp3.core.repository.CreatorRepository;
import fr.lessagasmp3.core.saga.repository.SagaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SagaService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CreatorRepository creatorRepository;

    @Autowired
    private SagaRepository sagaRepository;

    public SagaModel findOrCreate(String title) {
        SagaModel saga = findByTitle(title);
        if (saga == null) {
            saga = new SagaModel();
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
        if(entity != null) {
            return SagaModel.fromEntity(entity);
        }
        return null;
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
            log.error("Impossible to create saga : body is incomplete");
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
        Creator creator = creatorRepository.findById(authorId).orElse(null);
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
        Creator creator = creatorRepository.findById(composerId).orElse(null);
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
        Category category = categoryRepository.findById(categoryId).orElse(null);
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
