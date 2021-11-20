package fr.lessagasmp3.core.distribution.controller;

import com.google.gson.Gson;
import fr.lessagasmp3.core.common.constant.Strings;
import fr.lessagasmp3.core.creator.repository.CreatorRepository;
import fr.lessagasmp3.core.distribution.model.DistributionEntryModel;
import fr.lessagasmp3.core.distribution.service.DistributionService;
import fr.lessagasmp3.core.saga.repository.SagaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class DistributionController {

    @Autowired
    private CreatorRepository creatorRepository;

    @Autowired
    private DistributionService distributionService;

    @Autowired
    private SagaRepository sagaRepository;

    @Autowired
    private Gson gson;

    @RequestMapping(value = "/distribution", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"actorId", "sagaId", "roles"})
    public DistributionEntryModel getByCreatorIdAndSagaIdAndRoles(@RequestParam("actorId") Long actorId, @RequestParam("sagaId") Long sagaId, @RequestParam("roles") String roles) {
        return distributionService.findByActorIdAndSagaIdAndRolesIgnoreCase(actorId, sagaId, roles);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/distribution", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public DistributionEntryModel create(@RequestBody String modelStr) {
        DistributionEntryModel model = gson.fromJson(Strings.convertToUtf8(modelStr), DistributionEntryModel.class);
        return distributionService.create(model);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/distribution", method = RequestMethod.PUT, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public DistributionEntryModel update(@RequestBody DistributionEntryModel model) {
        return distributionService.update(model);
    }
}
