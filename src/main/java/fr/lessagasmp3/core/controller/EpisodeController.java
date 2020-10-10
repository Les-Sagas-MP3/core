package fr.lessagasmp3.core.controller;

import com.google.gson.Gson;
import fr.lessagasmp3.core.entity.Episode;
import fr.lessagasmp3.core.exception.BadRequestException;
import fr.lessagasmp3.core.exception.NotFoundException;
import fr.lessagasmp3.core.model.EpisodeModel;
import fr.lessagasmp3.core.repository.EpisodeRepository;
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
    private EpisodeRepository episodeRepository;

    @Autowired
    private Gson gson;

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

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/episode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public EpisodeModel create(@RequestBody String modelStr) {

        EpisodeModel model = gson.fromJson(modelStr, EpisodeModel.class);

        // Verify that body is complete
        if(model == null) {
            LOGGER.error("Impossible to create episode : body is incomplete");
            throw new BadRequestException();
        }

        // Create entity
        Episode episode = Episode.fromModel(model);
        return EpisodeModel.fromEntity(episodeRepository.save(episode));
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/episode", method = RequestMethod.PUT, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void update(@RequestBody EpisodeModel model) {

        // Verify that body is complete
        if(model == null || model.getId() <= 0) {
            LOGGER.error("Impossible to create episode : body is incomplete");
            throw new BadRequestException();
        }

        // Verify that author exists
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
