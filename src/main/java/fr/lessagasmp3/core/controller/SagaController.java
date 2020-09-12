package fr.lessagasmp3.core.controller;

import fr.lessagasmp3.core.entity.Saga;
import fr.lessagasmp3.core.model.SagaModel;
import fr.lessagasmp3.core.repository.SagaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SagaController {

    @Autowired
    private SagaRepository sagaRepository;

    @RequestMapping(value = "/api/saga", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SagaModel> getAll() {
        List<Saga> entities = sagaRepository.findAll();
        List<SagaModel> models = new ArrayList<>();
        entities.forEach(entity -> models.add(SagaModel.fromEntity(entity)));
        return models;
    }

    @RequestMapping(value = "/api/saga/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public SagaModel getById(@PathVariable Long id) {
        return SagaModel.fromEntity(sagaRepository.findById(id).orElse(null));
    }

}
