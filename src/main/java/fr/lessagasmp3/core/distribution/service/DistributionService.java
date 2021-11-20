package fr.lessagasmp3.core.distribution.service;

import fr.lessagasmp3.core.creator.entity.Creator;
import fr.lessagasmp3.core.creator.repository.CreatorRepository;
import fr.lessagasmp3.core.distribution.entity.DistributionEntry;
import fr.lessagasmp3.core.distribution.model.DistributionEntryModel;
import fr.lessagasmp3.core.distribution.repository.DistributionEntryRepository;
import fr.lessagasmp3.core.exception.BadRequestException;
import fr.lessagasmp3.core.exception.NotFoundException;
import fr.lessagasmp3.core.saga.entity.Saga;
import fr.lessagasmp3.core.saga.repository.SagaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DistributionService {

    @Autowired
    private CreatorRepository creatorRepository;

    @Autowired
    private DistributionEntryRepository distributionEntryRepository;

    @Autowired
    private SagaRepository sagaRepository;

    public DistributionEntryModel findByActorIdAndSagaIdAndRolesIgnoreCase(Long actorId, Long sagaId, String roles) {
        DistributionEntry entity = distributionEntryRepository.findByActorIdAndSagaIdAndRolesIgnoreCase(actorId, sagaId, roles);
        if(entity != null) {
            return DistributionEntryModel.fromEntity(entity);
        }
        return null;
    }

    public DistributionEntryModel findOrCreate(Long actorId, Long sagaId, String roles) {
        DistributionEntryModel distributionEntry = findByActorIdAndSagaIdAndRolesIgnoreCase(actorId, sagaId, roles);
        if (distributionEntry == null) {
            distributionEntry = new DistributionEntryModel();
            distributionEntry.setActorRef(actorId);
            distributionEntry.setSagaRef(sagaId);
            distributionEntry.setRoles(roles);
            distributionEntry = create(distributionEntry);
            if(distributionEntry != null) {
                log.debug("Distribytion Entry {} created", distributionEntry);
            } else {
                log.error("Distribytion Entry {}:{} not created", actorId, roles);
            }
        } else {
            log.debug("Distribytion Entry already exists : {}", distributionEntry);
        }
        return distributionEntry;
    }

    public DistributionEntryModel create(DistributionEntryModel model) {

        // Verify that body is complete
        if(model == null ||
                model.getActorRef() <= 0 ||
                model.getSagaRef() <= 0) {
            log.error("Impossible to create distribution entry : body is incomplete");
            throw new BadRequestException();
        }

        // Verify that references exist
        Creator creator = creatorRepository.findById(model.getActorRef()).orElse(null);
        Saga saga = sagaRepository.findById(model.getSagaRef()).orElse(null);
        if(creator == null || saga == null) {
            log.error("Impossible to create distribution entry : creator {} or saga {} not found", model.getActorRef(), model.getSagaRef());
            throw new NotFoundException();
        }

        // Create author
        DistributionEntry entity = new DistributionEntry();
        entity.setActor(creator);
        entity.setSaga(saga);
        entity.setRoles(model.getRoles());
        return DistributionEntryModel.fromEntity(distributionEntryRepository.save(entity));
    }

    public DistributionEntryModel update(DistributionEntryModel model) {

        // Verify that body is complete
        if(model == null ||
                model.getId() <= 0 ||
                model.getActorRef() <= 0 ||
                model.getSagaRef() <= 0 ||
                model.getRoles() == null || model.getRoles().isEmpty()) {
            log.error("Impossible to create distribution entry : body is incomplete");
            throw new BadRequestException();
        }

        // Verify that references exist
        DistributionEntry entity = distributionEntryRepository.findById(model.getId()).orElse(null);
        Creator creator = creatorRepository.findById(model.getActorRef()).orElse(null);
        Saga saga = sagaRepository.findById(model.getActorRef()).orElse(null);
        if(entity == null || creator == null || saga == null) {
            log.error("Impossible to create distribution entry : at least one reference is not found");
            throw new NotFoundException();
        }

        // Update and save author
        entity.setActor(creator);
        entity.setSaga(saga);
        entity.setRoles(model.getRoles());
        return DistributionEntryModel.fromEntity(distributionEntryRepository.save(entity));
    }

}
