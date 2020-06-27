package com.netophonix.core.controller;

import com.netophonix.core.model.Saga;
import com.netophonix.core.repository.SagaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SagaController {

    @Autowired
    private SagaRepository sagaRepository;

    @RequestMapping(value = "/api/sagas", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Saga> getAllSagas() {
        return sagaRepository.findAll();
    }

}
