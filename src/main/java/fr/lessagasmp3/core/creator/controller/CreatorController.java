package fr.lessagasmp3.core.creator.controller;

import com.google.gson.Gson;
import fr.lessagasmp3.core.common.constant.Strings;
import fr.lessagasmp3.core.creator.model.CreatorModel;
import fr.lessagasmp3.core.creator.service.CreatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api")
public class CreatorController {

    @Autowired
    private CreatorService creatorService;

    @Autowired
    private Gson gson;

    @RequestMapping(value = "/authors", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"ids"})
    public Set<CreatorModel> getAllByIds(@RequestParam("ids") Set<Long> ids) {
        return creatorService.getAllByIds(ids);
    }

    @RequestMapping(value = "/authors", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"name"})
    public CreatorModel getByName(@RequestParam("name") String name) {
        return creatorService.findByName(name);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/authors", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public CreatorModel create(@RequestBody String modelStr) {
        CreatorModel creatorModel = gson.fromJson(Strings.convertToUtf8(modelStr), CreatorModel.class);
        return creatorService.create(creatorModel);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/authors", method = RequestMethod.PUT, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public CreatorModel update(@RequestBody String modelStr) {
        CreatorModel creatorModel = gson.fromJson(Strings.convertToUtf8(modelStr), CreatorModel.class);
        return creatorService.update(creatorModel);
    }
}
