package fr.lessagasmp3.core.controller;

import com.google.gson.Gson;
import fr.lessagasmp3.core.entity.Saga;
import fr.lessagasmp3.core.entity.Season;
import fr.lessagasmp3.core.exception.BadRequestException;
import fr.lessagasmp3.core.exception.NotFoundException;
import fr.lessagasmp3.core.model.SeasonModel;
import fr.lessagasmp3.core.repository.SagaRepository;
import fr.lessagasmp3.core.repository.SeasonRepository;
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
public class SeasonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeasonController.class);

    @Autowired
    private SagaRepository sagaRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private Gson gson;

    @RequestMapping(value = "/season", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"ids"})
    public Set<SeasonModel> getAllByIds(@RequestParam("ids") Set<Long> ids) {
        Set<SeasonModel> models = new LinkedHashSet<>();
        for(Long id : ids) {
            Season entity = seasonRepository.findById(id).orElse(null);
            if(entity == null) {
                LOGGER.error("Impossible to get season {} : it doesn't exist", id);
                continue;
            }
            models.add(SeasonModel.fromEntity(entity));
        }
        return models;
    }

    @RequestMapping(value = "/season", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"number", "sagaId"})
    public SeasonModel getByNumberAndSagaId(@RequestParam("number") Integer number, @RequestParam("sagaId") Long sagaId) {
        Season entity = seasonRepository.findByNumberAndSagaId(number, sagaId);
        if(entity != null) {
            return SeasonModel.fromEntity(entity);
        }
        return null;
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/season", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public SeasonModel create(@RequestBody String modelStr) {

        SeasonModel model = gson.fromJson(modelStr, SeasonModel.class);

        // Verify that body is complete
        if(model == null) {
            LOGGER.error("Impossible to create season : body is incomplete");
            throw new BadRequestException();
        }

        // Verify that entities exists
        Saga saga = sagaRepository.findById(model.getSagaRef()).orElse(null);
        if(saga == null) {
            LOGGER.error("Impossible to create season : saga {} not found", model.getSagaRef());
            throw new NotFoundException();
        }

        // Create entity
        Season season = Season.fromModel(model);
        season.setSaga(saga);
        return SeasonModel.fromEntity(seasonRepository.save(season));
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/season", method = RequestMethod.PUT, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void update(@RequestBody String modelStr) {

        SeasonModel model = gson.fromJson(modelStr, SeasonModel.class);

        // Verify that body is complete
        if(model == null || model.getId() <= 0) {
            LOGGER.error("Impossible to create season : body is incomplete");
            throw new BadRequestException();
        }

        // Verify that entity exists
        Season season = seasonRepository.findById(model.getId()).orElse(null);
        if(season == null) {
            LOGGER.error("Impossible to update season : season {} not found", model.getId());
            throw new NotFoundException();
        }

        // Update and save entity
        season.setNumber(model.getNumber());
        season.setName(model.getName());
        seasonRepository.save(season);
    }
}
