package fr.lessagasmp3.core.controller;

import com.google.gson.Gson;
import fr.lessagasmp3.core.constant.Strings;
import fr.lessagasmp3.core.entity.Anecdote;
import fr.lessagasmp3.core.entity.Saga;
import fr.lessagasmp3.core.exception.BadRequestException;
import fr.lessagasmp3.core.exception.NotFoundException;
import fr.lessagasmp3.core.model.AnecdoteModel;
import fr.lessagasmp3.core.repository.AnecdoteRepository;
import fr.lessagasmp3.core.repository.SagaRepository;
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
public class AnecdoteController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnecdoteController.class);

    @Autowired
    private AnecdoteRepository anecdoteRepository;

    @Autowired
    private SagaRepository sagaRepository;

    @Autowired
    private Gson gson;

    @RequestMapping(value = "/anecdote", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"ids"})
    public Set<AnecdoteModel> getAllByIds(@RequestParam("ids") Set<Long> ids) {
        Set<AnecdoteModel> models = new LinkedHashSet<>();
        for(Long id : ids) {
            Anecdote entity = anecdoteRepository.findById(id).orElse(null);
            if(entity == null) {
                LOGGER.error("Impossible to get anecdote {} : it doesn't exist", id);
                continue;
            }
            models.add(AnecdoteModel.fromEntity(entity));
        }
        return models;
    }

    @RequestMapping(value = "/anecdote", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"content", "sagaId"})
    public AnecdoteModel getByAnecdoteAndSagaId(@RequestParam("content") String content, @RequestParam("sagaId") Long sagaId) {
        Anecdote entity = anecdoteRepository.findByAnecdoteIgnoreCaseAndSagaId(content, sagaId);
        if(entity != null) {
            return AnecdoteModel.fromEntity(entity);
        }
        return null;
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/anecdote", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public AnecdoteModel create(@RequestBody String modelStr) {

        AnecdoteModel model = gson.fromJson(Strings.convertToUtf8(modelStr), AnecdoteModel.class);

        // Verify that body is complete
        if(model == null) {
            LOGGER.error("Impossible to create anecdote : body is incomplete");
            throw new BadRequestException();
        }

        // Verify that entities exists
        Saga saga = sagaRepository.findById(model.getSagaRef()).orElse(null);
        if(saga == null) {
            LOGGER.error("Impossible to create season : saga {} not found", model.getSagaRef());
            throw new NotFoundException();
        }

        // Create entity
        Anecdote anecdote = Anecdote.fromModel(model);
        anecdote.setSaga(saga);
        return AnecdoteModel.fromEntity(anecdoteRepository.save(anecdote));
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/anecdote", method = RequestMethod.PUT, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void update(@RequestBody String modelStr) {

        AnecdoteModel model = gson.fromJson(Strings.convertToUtf8(modelStr), AnecdoteModel.class);

        // Verify that body is complete
        if(model == null || model.getId() <= 0) {
            LOGGER.error("Impossible to create anecdote : body is incomplete");
            throw new BadRequestException();
        }

        // Verify that entities exists
        Anecdote anecdote = anecdoteRepository.findById(model.getId()).orElse(null);
        if(anecdote == null) {
            LOGGER.error("Impossible to update anecdote : anecdote {} not found", model.getId());
            throw new NotFoundException();
        }

        // Update and save entity
        anecdote.setAnecdote(model.getAnecdote());
        anecdoteRepository.save(anecdote);
    }
}
