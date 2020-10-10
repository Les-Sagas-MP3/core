package fr.lessagasmp3.core.controller;

import com.google.gson.Gson;
import fr.lessagasmp3.core.entity.Saga;
import fr.lessagasmp3.core.exception.BadRequestException;
import fr.lessagasmp3.core.model.SagaModel;
import fr.lessagasmp3.core.pagination.DataPage;
import fr.lessagasmp3.core.repository.SagaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashSet;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class SagaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SagaController.class);

    @Autowired
    private Gson gson;

    @Autowired
    private SagaRepository sagaRepository;

    @RequestMapping(value = "/saga", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<SagaModel> getAll() {
        Set<Saga> entities = sagaRepository.findAllByOrderByTitleAsc();
        Set<SagaModel> models = new LinkedHashSet<>();
        entities.forEach(entity -> models.add(SagaModel.fromEntity(entity)));
        return models;
    }

    @RequestMapping(value = "/saga/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public SagaModel getById(@PathVariable Long id) {
        return SagaModel.fromEntity(sagaRepository.findById(id).orElse(null));
    }

    @RequestMapping(value = "/saga", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"title"})
    public SagaModel getByTitle(@RequestParam("title") String title) {
        Saga entity = sagaRepository.findByTitle(title);
        if(entity != null) {
            return SagaModel.fromEntity(entity);
        }
        return null;
    }

    @RequestMapping(value = "/saga", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"offset", "limit"})
    public DataPage<SagaModel> getPaginated(@RequestParam("offset") int offset, @RequestParam("limit") int limit) {

        // Verify that params are correct
        if(offset < 0 || limit <= 0) {
            LOGGER.error("Cannot get paginated sagas : offset or limit is incorrect");
            throw new BadRequestException();
        }

        // Get and transform contents
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("title").ascending());
        Page<Saga> entities = sagaRepository.findAll(pageable);
        DataPage<SagaModel> models = new DataPage<>(entities);
        entities.getContent().forEach(entity -> models.getContent().add(SagaModel.fromEntity(entity)));
        return models;
    }

    @RequestMapping(value = "/saga", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"search"})
    public Set<SagaModel> search(@RequestParam("search") String search) {
        Set<SagaModel> models = new LinkedHashSet<>();
        Set<Saga> entities = sagaRepository.findAllByTitleContainsIgnoreCaseOrderByTitleAsc(search);
        entities.forEach(entity -> models.add(SagaModel.fromEntity(entity)));
        return models;
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/saga", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public SagaModel create(@RequestBody String modelStr) {

        SagaModel model = gson.fromJson(modelStr, SagaModel.class);

        // Verify that body is complete
        if(model == null ||
                model.getTitle() == null || model.getTitle().isEmpty()) {
            LOGGER.error("Impossible to create saga : body is incomplete");
            throw new BadRequestException();
        }

        // Create author
        Saga entity = Saga.fromModel(model);
        return SagaModel.fromEntity(sagaRepository.save(entity));
    }

}
