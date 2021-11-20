package fr.lessagasmp3.core.creator.service;

import fr.lessagasmp3.core.creator.entity.Creator;
import fr.lessagasmp3.core.creator.model.CreatorModel;
import fr.lessagasmp3.core.creator.repository.CreatorRepository;
import fr.lessagasmp3.core.exception.BadRequestException;
import fr.lessagasmp3.core.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
@Service
public class CreatorService {

    @Autowired
    private CreatorRepository creatorRepository;

    public Set<CreatorModel> getAllByIds(@RequestParam("ids") Set<Long> ids) {
        Set<CreatorModel> models = new LinkedHashSet<>();
        for(Long id : ids) {
            Creator entity = creatorRepository.findById(id).orElse(null);
            if(entity == null) {
                log.error("Impossible to get creator {} : it doesn't exist", id);
                continue;
            }
            models.add(CreatorModel.fromEntity(entity));
        }
        return models;
    }

    public CreatorModel findOrCreate(String name) {
        CreatorModel creator = findByName(name);
        if (creator == null) {
            creator = new CreatorModel();
            creator.setName(name);
            creator.setNbSagas(1);
            creator = create(creator);
            if(creator != null) {
                log.debug("Creator {} created", creator);
            } else {
                log.error("Creator {} not created", name);
            }
        } else {
            log.debug("Creator already exists : {}", creator);
        }
        return creator;
    }

    public CreatorModel findByName(String name) {
        Creator entity = creatorRepository.findByNameIgnoreCase(name);
        if(entity != null) {
            return CreatorModel.fromEntity(entity);
        }
        return null;
    }

    public CreatorModel create(CreatorModel creatorModel) {

        // Verify that body is complete
        if(creatorModel == null ||
                creatorModel.getName() == null || creatorModel.getName().isEmpty()) {
            log.error("Impossible to create creator : body is incomplete");
            throw new BadRequestException();
        }

        // Create author
        Creator creator = new Creator();
        creator.setName(creatorModel.getName());
        creator.setNbSagas(creatorModel.getNbSagas());
        return CreatorModel.fromEntity(creatorRepository.save(creator));
    }

    public CreatorModel update(CreatorModel creatorModel) {

        // Verify that body is complete
        if(creatorModel == null ||
                creatorModel.getId() <= 0 ||
                creatorModel.getName() == null || creatorModel.getName().isEmpty()) {
            log.error("Impossible to create creator : body is incomplete");
            throw new BadRequestException();
        }

        // Verify that author exists
        Creator creator = creatorRepository.findById(creatorModel.getId()).orElse(null);
        if(creator == null) {
            log.error("Impossible to update creator : creator {} not found", creatorModel.getId());
            throw new NotFoundException();
        }

        // Update and save author
        creator.setName(creatorModel.getName());
        creator.setNbSagas(creatorModel.getNbSagas());
        return CreatorModel.fromEntity(creatorRepository.save(creator));
    }

}
