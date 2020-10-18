package fr.lessagasmp3.core.controller;

import com.google.gson.Gson;
import fr.lessagasmp3.core.constant.Strings;
import fr.lessagasmp3.core.entity.Creator;
import fr.lessagasmp3.core.entity.DistributionEntry;
import fr.lessagasmp3.core.entity.Saga;
import fr.lessagasmp3.core.exception.BadRequestException;
import fr.lessagasmp3.core.exception.NotFoundException;
import fr.lessagasmp3.core.model.DistributionEntryModel;
import fr.lessagasmp3.core.repository.CreatorRepository;
import fr.lessagasmp3.core.repository.DistributionEntryRepository;
import fr.lessagasmp3.core.repository.SagaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DistributionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributionController.class);

    @Autowired
    private CreatorRepository creatorRepository;

    @Autowired
    private DistributionEntryRepository distributionEntryRepository;

    @Autowired
    private SagaRepository sagaRepository;

    @Autowired
    private Gson gson;

    @RequestMapping(value = "/distribution", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"actorId", "sagaId", "roles"})
    public DistributionEntryModel getByCreatorIdAndSagaIdAndRoles(@RequestParam("actorId") Long actorId, @RequestParam("sagaId") Long sagaId, @RequestParam("roles") String roles) {
        DistributionEntry entity = distributionEntryRepository.findByActorIdAndSagaIdAndRoles(actorId, sagaId, roles);
        if(entity != null) {
            return DistributionEntryModel.fromEntity(entity);
        }
        return null;
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/distribution", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public DistributionEntryModel create(@RequestBody String modelStr) {

        DistributionEntryModel model = gson.fromJson(Strings.convertToUtf8(modelStr), DistributionEntryModel.class);

        // Verify that body is complete
        if(model == null ||
                model.getActorRef() <= 0 ||
                model.getSagaRef() <= 0) {
            LOGGER.error("Impossible to create distribution entry : body is incomplete");
            throw new BadRequestException();
        }

        // Verify that references exist
        Creator creator = creatorRepository.findById(model.getActorRef()).orElse(null);
        Saga saga = sagaRepository.findById(model.getSagaRef()).orElse(null);
        if(creator == null || saga == null) {
            LOGGER.error("Impossible to create distribution entry : creator {} or saga {} not found", model.getActorRef(), model.getSagaRef());
            throw new NotFoundException();
        }

        // Create author
        DistributionEntry entity = new DistributionEntry();
        entity.setActor(creator);
        entity.setSaga(saga);
        entity.setRoles(model.getRoles());
        return DistributionEntryModel.fromEntity(distributionEntryRepository.save(entity));
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/distribution", method = RequestMethod.PUT, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void update(@RequestBody DistributionEntryModel model) {

        // Verify that body is complete
        if(model == null ||
                model.getId() <= 0 ||
                model.getActorRef() <= 0 ||
                model.getSagaRef() <= 0 ||
                model.getRoles() == null || model.getRoles().isEmpty()) {
            LOGGER.error("Impossible to create distribution entry : body is incomplete");
            throw new BadRequestException();
        }

        // Verify that references exist
        DistributionEntry entity = distributionEntryRepository.findById(model.getId()).orElse(null);
        Creator creator = creatorRepository.findById(model.getActorRef()).orElse(null);
        Saga saga = sagaRepository.findById(model.getActorRef()).orElse(null);
        if(entity == null || creator == null || saga == null) {
            LOGGER.error("Impossible to create distribution entry : at least one reference is not found");
            throw new NotFoundException();
        }

        // Update and save author
        entity.setActor(creator);
        entity.setSaga(saga);
        entity.setRoles(model.getRoles());
        distributionEntryRepository.save(entity);
    }
}
