package fr.lessagasmp3.core.controller;

import com.google.gson.Gson;
import fr.lessagasmp3.core.constant.Strings;
import fr.lessagasmp3.core.entity.Episode;
import fr.lessagasmp3.core.entity.Season;
import fr.lessagasmp3.core.exception.BadRequestException;
import fr.lessagasmp3.core.exception.NotFoundException;
import fr.lessagasmp3.core.model.EpisodeModel;
import fr.lessagasmp3.core.repository.EpisodeRepository;
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
public class EpisodeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EpisodeController.class);

    @Autowired
    private Gson gson;

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @RequestMapping(value = "/episode", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"ids"})
    public Set<EpisodeModel> getAllByIds(@RequestParam("ids") Set<Long> ids) {
        Set<EpisodeModel> models = new LinkedHashSet<>();
        for(Long id : ids) {
            Episode entity = episodeRepository.findById(id).orElse(null);
            if(entity == null) {
                LOGGER.error("Impossible to get episode {} : it doesn't exist", id);
                continue;
            }
            models.add(EpisodeModel.fromEntity(entity));
        }
        return models;
    }

    @RequestMapping(value = "/episode", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"number", "seasonId"})
    public EpisodeModel getByNumberAndSeasonId(@RequestParam("number") Integer number, @RequestParam("seasonId") Long seasonId) {
        Episode entity = episodeRepository.findByNumberAndSeasonId(number, seasonId);
        if(entity != null) {
            return EpisodeModel.fromEntity(entity);
        }
        return null;
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/episode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public EpisodeModel create(@RequestBody String modelStr) {

        EpisodeModel model = gson.fromJson(Strings.convertToUtf8(modelStr), EpisodeModel.class);

        // Verify that body is complete
        if(model == null) {
            LOGGER.error("Impossible to create episode : body is incomplete");
            throw new BadRequestException();
        }

        // Verify that entities exists
        Season season = seasonRepository.findById(model.getSeasonRef()).orElse(null);
        if(season == null) {
            LOGGER.error("Impossible to create episode : season {} not found", model.getSeasonRef());
            throw new NotFoundException();
        }

        // Create entity
        Episode episode = Episode.fromModel(model);
        episode.setSeason(season);
        return EpisodeModel.fromEntity(episodeRepository.save(episode));
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/episode", method = RequestMethod.PUT, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void update(@RequestBody String modelStr) {

        EpisodeModel model = gson.fromJson(Strings.convertToUtf8(modelStr), EpisodeModel.class);

        // Verify that body is complete
        if(model == null || model.getId() <= 0) {
            LOGGER.error("Impossible to create episode : body is incomplete");
            throw new BadRequestException();
        }

        // Verify that entity exists
        Episode episode = episodeRepository.findById(model.getId()).orElse(null);
        if(episode == null) {
            LOGGER.error("Impossible to update episode : episode {} not found", model.getId());
            throw new NotFoundException();
        }

        // Update and save entity
        episode.setNumber(model.getNumber());
        episode.setDisplayedNumber(model.getDisplayedNumber());
        episode.setTitle(model.getTitle());
        episode.setInfos(model.getInfos());
        episodeRepository.save(episode);
    }
}
