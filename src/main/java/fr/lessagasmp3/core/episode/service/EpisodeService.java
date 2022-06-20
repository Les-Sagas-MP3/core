package fr.lessagasmp3.core.episode.service;

import fr.lessagasmp3.core.episode.entity.Episode;
import fr.lessagasmp3.core.episode.model.EpisodeModel;
import fr.lessagasmp3.core.episode.repository.EpisodeRepository;
import fr.lessagasmp3.core.exception.BadRequestException;
import fr.lessagasmp3.core.exception.NotFoundException;
import fr.lessagasmp3.core.file.entity.File;
import fr.lessagasmp3.core.file.repository.FileRepository;
import fr.lessagasmp3.core.season.entity.Season;
import fr.lessagasmp3.core.season.repository.SeasonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class EpisodeService {

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    public EpisodeModel getById(Long id) {
        return EpisodeModel.fromEntity(episodeRepository.findById(id).orElse(null));
    }

    public List<EpisodeModel> getAllByIds(Set<Long> ids) {
        List<Episode> entities = episodeRepository.findAllById(ids);

        List<EpisodeModel> models = new ArrayList<>();
        entities.forEach(entity -> models.add(EpisodeModel.fromEntity(entity)));
        Collections.sort(models);

        return models;
    }

    public EpisodeModel findByNumberAndSeasonId(Integer number, Long seasonId) {
        Episode entity = episodeRepository.findByNumberAndSeasonId(number, seasonId);
        if(entity != null) {
            return EpisodeModel.fromEntity(entity);
        }
        return null;
    }

    public EpisodeModel findOrCreate(Integer number, Long seasonId) {
        EpisodeModel episode = findByNumberAndSeasonId(number, seasonId);
        if (episode == null) {
            episode = new EpisodeModel();
            episode.setWorkspace(UUID.randomUUID().toString());
            episode.setNumber(number);
            episode.setSeasonRef(seasonId);
            episode = create(episode);
            if(episode != null) {
                log.debug("Episode {} created", episode);
            } else {
                log.error("Episode {} not created", number);
            }
        } else {
            log.debug("Episode already exists : {}", episode);
        }
        return episode;
    }

    public EpisodeModel create(EpisodeModel model) {

        // Verify that body is complete
        if(model == null) {
            log.error("Impossible to create episode : body is incomplete");
            throw new BadRequestException();
        }

        // Verify that entities exists
        Season season = seasonRepository.findById(model.getSeasonRef()).orElse(null);
        if(season == null) {
            log.error("Impossible to create episode : season {} not found", model.getSeasonRef());
            throw new NotFoundException();
        }

        // Create entity
        Episode episode = Episode.fromModel(model);
        episode.setSeason(season);
        return EpisodeModel.fromEntity(episodeRepository.save(episode));
    }

    public EpisodeModel update(EpisodeModel model) {

        // Verify that body is complete
        if(model == null || model.getId() <= 0) {
            log.error("Impossible to create episode : body is incomplete");
            throw new BadRequestException();
        }

        // Verify that entity exists
        Episode episode = episodeRepository.findById(model.getId()).orElse(null);
        Season season = seasonRepository.findById(model.getSeasonRef()).orElse(null);
        File file = fileRepository.findById(model.getFileRef()).orElse(null);
        if(episode == null || season == null) {
            log.error("Impossible to update episode : episode {} or season {} not found", model.getId(), model.getSeasonRef());
            throw new NotFoundException();
        }

        // Update and save entity
        episode.setNumber(model.getNumber());
        episode.setDisplayedNumber(model.getDisplayedNumber());
        episode.setTitle(model.getTitle());
        episode.setInfos(model.getInfos());
        episode.setSeason(season);
        if(file != null) {
            episode.setFile(file);
        }
        return EpisodeModel.fromEntity(episodeRepository.save(episode));
    }

}
