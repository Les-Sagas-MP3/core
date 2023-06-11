package fr.lessagasmp3.core.user.controller;

import fr.lessagasmp3.core.user.model.UserModel;
import fr.lessagasmp3.core.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserModel getById(Principal principal, @PathVariable("id") Long id) {
        return userService.controlsAndGetById(principal, id);
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
