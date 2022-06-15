package fr.lessagasmp3.core.episode.controller;

import com.google.gson.Gson;
import fr.lessagasmp3.core.common.constant.Strings;
import fr.lessagasmp3.core.episode.model.EpisodeModel;
import fr.lessagasmp3.core.episode.service.EpisodeService;
import fr.lessagasmp3.core.season.repository.SeasonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api")
public class EpisodeController {

    @Autowired
    private Gson gson;

    @Autowired
    private EpisodeService episodeService;

    @Autowired
    private SeasonRepository seasonRepository;

    @RequestMapping(value = "/episode/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public EpisodeModel getById(@PathVariable Long id) {
        return episodeService.getById(id);
    }

    @RequestMapping(value = "/episode", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"ids"})
    public List<EpisodeModel> getAllByIds(@RequestParam("ids") Set<Long> ids) {
        return episodeService.getAllByIds(ids);
    }

    @RequestMapping(value = "/episode", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"number", "seasonId"})
    public EpisodeModel getByNumberAndSeasonId(@RequestParam("number") Integer number, @RequestParam("seasonId") Long seasonId) {
        return episodeService.findByNumberAndSeasonId(number, seasonId);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/episode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public EpisodeModel create(@RequestBody String modelStr) {
        EpisodeModel model = gson.fromJson(Strings.convertToUtf8(modelStr), EpisodeModel.class);
        return episodeService.create(model);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/episode", method = RequestMethod.PUT, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public EpisodeModel update(@RequestBody String modelStr) {
        EpisodeModel model = gson.fromJson(Strings.convertToUtf8(modelStr), EpisodeModel.class);
        return episodeService.update(model);
    }
}
