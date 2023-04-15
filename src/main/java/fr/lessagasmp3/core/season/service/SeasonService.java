package fr.lessagasmp3.core.season.service;

import fr.lessagasmp3.core.exception.BadRequestException;
import fr.lessagasmp3.core.exception.NotFoundException;
import fr.lessagasmp3.core.saga.entity.Saga;
import fr.lessagasmp3.core.saga.repository.SagaRepository;
import fr.lessagasmp3.core.season.entity.Season;
import fr.lessagasmp3.core.season.model.SeasonModel;
import fr.lessagasmp3.core.season.repository.SeasonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class SeasonService {

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private SagaRepository sagaRepository;

    public Set<Season> findAllEntitiesBySaga(Long sagaId) {
        return seasonRepository.findAllBySagaId(sagaId);
    }

    public SeasonModel getById(Long id) {
        return SeasonModel.fromEntity(seasonRepository.findById(id).orElse(null));
    }

    public List<SeasonModel> getAllByIds(Set<Long> ids) {
        List<Season> entities = seasonRepository.findAllById(ids);

        List<SeasonModel> models = new ArrayList<>();
        entities.forEach(entity -> models.add(SeasonModel.fromEntity(entity)));
        Collections.sort(models);

        return models;
    }

    public SeasonModel findByNumberAndSagaId(Integer number, Long sagaId) {
        Season entity = seasonRepository.findByNumberAndSagaId(number, sagaId);
        if(entity != null) {
            return SeasonModel.fromEntity(entity);
        }
        return null;
    }

    public SeasonModel findOrCreate(Integer number, Long sagaId) {
        SeasonModel season = findByNumberAndSagaId(number, sagaId);
        if (season == null) {
            season = new SeasonModel();
            season.setNumber(number);
            season.setSagaRef(sagaId);
            season = create(season);
            if(season != null) {
                log.debug("Season {} created", season);
            } else {
                log.error("Season {} not created", number);
            }
        } else {
            log.debug("Season already exists : {}", season);
        }
        return season;
    }

    public SeasonModel create(SeasonModel model) {

        // Verify that body is complete
        if(model == null) {
            log.error("Impossible to create season : body is incomplete");
            throw new BadRequestException();
        }

        // Verify that entities exists
        Saga saga = sagaRepository.findById(model.getSagaRef()).orElse(null);
        if(saga == null) {
            log.error("Impossible to create season : saga {} not found", model.getSagaRef());
            throw new NotFoundException();
        }

        // Create entity
        Season season = Season.fromModel(model);
        season.setSaga(saga);
        return SeasonModel.fromEntity(seasonRepository.save(season));
    }

    public SeasonModel update(SeasonModel model) {

        // Verify that body is complete
        if(model == null || model.getId() <= 0) {
            log.error("Impossible to create season : body is incomplete");
            throw new BadRequestException();
        }

        // Verify that entity exists
        Season season = seasonRepository.findById(model.getId()).orElse(null);
        if(season == null) {
            log.error("Impossible to update season : season {} not found", model.getId());
            throw new NotFoundException();
        }

        // Update and save entity
        season.setNumber(model.getNumber());
        season.setName(model.getName());
        return SeasonModel.fromEntity(seasonRepository.save(season));
    }

}
