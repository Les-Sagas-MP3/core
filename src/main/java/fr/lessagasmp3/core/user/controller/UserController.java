package fr.lessagasmp3.core.user.controller;

import fr.lessagasmp3.core.common.pagination.DataPage;
import fr.lessagasmp3.core.exception.BadRequestException;
import fr.lessagasmp3.core.user.entity.User;
import fr.lessagasmp3.core.user.model.UserModel;
import fr.lessagasmp3.core.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserModel getById(Principal principal, @PathVariable("id") Long id) {
        return userService.controlsAndGetById(principal, id);
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"offset", "limit"})
    public DataPage<UserModel> getPaginated(@RequestParam("offset") int offset, @RequestParam("limit") int limit) {

        // Verify that params are correct
        if(offset < 0 || limit <= 0) {
            log.error("Cannot get paginated sagas : offset or limit is incorrect");
            throw new BadRequestException();
        }

        // Get and transform contents
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("username").ascending());
        Page<User> entities = userService.getAll(pageable);
        DataPage<UserModel> models = new DataPage<>(entities);
        entities.getContent().forEach(entity -> models.getContent().add(UserModel.fromEntity(entity)));
        return models;
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = {"search"})
    public Set<UserModel> search(@RequestParam("search") String search) {
        Set<UserModel> models = new LinkedHashSet<>();
        Set<User> entities = userService.getAllByUsername(search);
        entities.forEach(entity -> models.add(UserModel.fromEntity(entity)));
        return models;
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public UserModel create(Principal principal, @RequestBody UserModel userModel) {
        return userService.controlsAndCreate(userModel);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/user", method = RequestMethod.PUT, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void save(Principal principal, @RequestBody UserModel userModel) {
        this.userService.controlsAndUpdate(principal, userModel);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void delete(Principal principal, @PathVariable Long id) {
        this.userService.controlsAndDelete(principal, id);
    }
}
