package fr.lessagasmp3.core.saga.controller;

import com.google.gson.Gson;
import fr.lessagasmp3.core.constant.Strings;
import fr.lessagasmp3.core.saga.entity.Saga;
import fr.lessagasmp3.core.exception.BadRequestException;
import fr.lessagasmp3.core.saga.model.SagaModel;
import fr.lessagasmp3.core.pagination.DataPage;
import fr.lessagasmp3.core.category.repository.CategoryRepository;
import fr.lessagasmp3.core.repository.CreatorRepository;
import fr.lessagasmp3.core.saga.repository.SagaRepository;
import fr.lessagasmp3.core.saga.service.SagaService;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@RequestMapping("/api")
public class SagaController {

    @Autowired
    private Gson gson;

    @Autowired
    private SagaService sagaService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CreatorRepository creatorRepository;

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
        return sagaService.findByTitle(title);
    }

    @RequestMapping(value = "/saga", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"offset", "limit"})
    public DataPage<SagaModel> getPaginated(@RequestParam("offset") int offset, @RequestParam("limit") int limit) {

        // Verify that params are correct
        if(offset < 0 || limit <= 0) {
            log.error("Cannot get paginated sagas : offset or limit is incorrect");
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

        log.debug("POST /api/saga");
        log.debug("BODY : {}", modelStr);
        SagaModel model = gson.fromJson(Strings.convertToUtf8(modelStr), SagaModel.class);

        return sagaService.create(model);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/saga", method = RequestMethod.PUT, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public SagaModel update(@RequestBody String modelStr) {

        log.debug("PUT /api/saga");
        log.debug("BODY : {}", modelStr);
        SagaModel model = gson.fromJson(Strings.convertToUtf8(modelStr), SagaModel.class);

        return sagaService.update(model);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/saga", method = RequestMethod.POST, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE, params = {"id", "authorId"})
    public void addAuthor(@RequestParam("id") Long id, @RequestParam("authorId") Long authorId) {
        sagaService.addAuthor(id, authorId);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/saga", method = RequestMethod.POST, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE, params = {"id", "composerId"})
    public void addComposer(@RequestParam("id") Long id, @RequestParam("composerId") Long composerId) {
        sagaService.addComposer(id, composerId);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/saga", method = RequestMethod.POST, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE, params = {"id", "categoryId"})
    public void addCategory(@RequestParam("id") Long id, @RequestParam("categoryId") Long categoryId) {
        sagaService.addCategory(id, categoryId);
    }

}
