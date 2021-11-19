package fr.lessagasmp3.core.anecdote.controller;

import com.google.gson.Gson;
import fr.lessagasmp3.core.anecdote.model.AnecdoteModel;
import fr.lessagasmp3.core.anecdote.service.AnecdoteService;
import fr.lessagasmp3.core.constant.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api")
public class AnecdoteController {

    @Autowired
    private AnecdoteService anecdoteService;

    @Autowired
    private Gson gson;

    @RequestMapping(value = "/anecdote", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"ids"})
    public Set<AnecdoteModel> getAllByIds(@RequestParam("ids") Set<Long> ids) {
        return anecdoteService.getAllByIds(ids);
    }

    @RequestMapping(value = "/anecdote", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"content", "sagaId"})
    public AnecdoteModel getByAnecdoteAndSagaId(@RequestParam("content") String content, @RequestParam("sagaId") Long sagaId) {
        return anecdoteService.findByAnecdoteAndSagaId(content, sagaId);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/anecdote", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public AnecdoteModel create(@RequestBody String modelStr) {
        AnecdoteModel model = gson.fromJson(Strings.convertToUtf8(modelStr), AnecdoteModel.class);
        return anecdoteService.create(model);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/anecdote", method = RequestMethod.PUT, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public AnecdoteModel update(@RequestBody String modelStr) {
        AnecdoteModel model = gson.fromJson(Strings.convertToUtf8(modelStr), AnecdoteModel.class);
        return anecdoteService.update(model);
    }
}
