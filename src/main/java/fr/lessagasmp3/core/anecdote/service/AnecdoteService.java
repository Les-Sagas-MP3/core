package fr.lessagasmp3.core.anecdote.service;

import fr.lessagasmp3.core.anecdote.entity.Anecdote;
import fr.lessagasmp3.core.saga.entity.Saga;
import fr.lessagasmp3.core.exception.BadRequestException;
import fr.lessagasmp3.core.exception.NotFoundException;
import fr.lessagasmp3.core.anecdote.model.AnecdoteModel;
import fr.lessagasmp3.core.anecdote.repository.AnecdoteRepository;
import fr.lessagasmp3.core.saga.repository.SagaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
@Service
public class AnecdoteService {

    @Autowired
    private AnecdoteRepository anecdoteRepository;

    @Autowired
    private SagaRepository sagaRepository;

    public Set<AnecdoteModel> getAllByIds(Set<Long> ids) {
        Set<AnecdoteModel> models = new LinkedHashSet<>();
        for(Long id : ids) {
            Anecdote entity = anecdoteRepository.findById(id).orElse(null);
            if(entity == null) {
                log.error("Impossible to get anecdote {} : it doesn't exist", id);
                continue;
            }
            models.add(AnecdoteModel.fromEntity(entity));
        }
        return models;
    }

    public AnecdoteModel findOrCreate(String content, Long sagaId) {
        AnecdoteModel anecdote = findByAnecdoteAndSagaId(content, sagaId);
        if (anecdote == null) {
            anecdote = new AnecdoteModel();
            anecdote.setAnecdote(content);
            anecdote.setSagaRef(sagaId);
            anecdote = create(anecdote);
            if(anecdote != null) {
                log.debug("Anecdote {} created", anecdote);
            } else {
                log.error("Anecdote {} not created", content);
            }
        } else {
            log.debug("Anecdote already exists : {}", anecdote);
        }
        return anecdote;
    }

    public AnecdoteModel findByAnecdoteAndSagaId(String content, Long sagaId) {
        Anecdote entity = anecdoteRepository.findByAnecdoteIgnoreCaseAndSagaId(content, sagaId);
        if(entity != null) {
            return AnecdoteModel.fromEntity(entity);
        }
        return null;
    }

    public AnecdoteModel create(AnecdoteModel model) {

        // Verify that body is complete
        if(model == null) {
            log.error("Impossible to create anecdote : body is incomplete");
            throw new BadRequestException();
        }

        // Verify that entities exists
        Saga saga = sagaRepository.findById(model.getSagaRef()).orElse(null);
        if(saga == null) {
            log.error("Impossible to create season : saga {} not found", model.getSagaRef());
            throw new NotFoundException();
        }

        // Create entity
        Anecdote anecdote = Anecdote.fromModel(model);
        anecdote.setSaga(saga);
        return AnecdoteModel.fromEntity(anecdoteRepository.save(anecdote));
    }

    public AnecdoteModel update(AnecdoteModel model) {

        // Verify that body is complete
        if(model == null || model.getId() <= 0) {
            log.error("Impossible to create anecdote : body is incomplete");
            throw new BadRequestException();
        }

        // Verify that entities exists
        Anecdote anecdote = anecdoteRepository.findById(model.getId()).orElse(null);
        if(anecdote == null) {
            log.error("Impossible to update anecdote : anecdote {} not found", model.getId());
            throw new NotFoundException();
        }

        // Update and save entity
        anecdote.setAnecdote(model.getAnecdote());
        return AnecdoteModel.fromEntity(anecdoteRepository.save(anecdote));
    }

}
