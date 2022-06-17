package fr.lessagasmp3.core.season.controller;

import com.google.gson.Gson;
import fr.lessagasmp3.core.common.constant.Strings;
import fr.lessagasmp3.core.season.model.SeasonModel;
import fr.lessagasmp3.core.season.service.SeasonService;
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
public class SeasonController {

    @Autowired
    private SeasonService seasonService;

    @Autowired
    private Gson gson;

    @RequestMapping(value = "/season/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public SeasonModel getById(@PathVariable Long id) {
        return seasonService.getById(id);
    }

    @RequestMapping(value = "/season", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"ids"})
    public List<SeasonModel> getAllByIds(@RequestParam("ids") Set<Long> ids) {
        return seasonService.getAllByIds(ids);
    }

    @RequestMapping(value = "/season", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"number", "sagaId"})
    public SeasonModel getByNumberAndSagaId(@RequestParam("number") Integer number, @RequestParam("sagaId") Long sagaId) {
        return seasonService.findByNumberAndSagaId(number, sagaId);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/season", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public SeasonModel create(@RequestBody String modelStr) {
        SeasonModel model = gson.fromJson(Strings.convertToUtf8(modelStr), SeasonModel.class);
        return seasonService.create(model);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/season", method = RequestMethod.PUT, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public SeasonModel update(@RequestBody String modelStr) {
        SeasonModel model = gson.fromJson(Strings.convertToUtf8(modelStr), SeasonModel.class);
        return seasonService.update(model);
    }
}
