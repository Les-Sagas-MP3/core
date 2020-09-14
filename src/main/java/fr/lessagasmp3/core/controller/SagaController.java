package fr.lessagasmp3.core.controller;

import fr.lessagasmp3.core.entity.Saga;
import fr.lessagasmp3.core.model.SagaModel;
import fr.lessagasmp3.core.repository.SagaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashSet;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class SagaController {

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

    @RequestMapping(value = "/saga", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"search"})
    public Set<SagaModel> search(@RequestParam("search") String search) {
        Set<SagaModel> models = new LinkedHashSet<>();
        Set<Saga> entities = sagaRepository.findAllByTitleContainsIgnoreCaseOrderByTitleAsc(search);
        entities.forEach(entity -> models.add(SagaModel.fromEntity(entity)));
        return models;
    }

}
