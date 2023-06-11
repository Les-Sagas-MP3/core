package fr.lessagasmp3.core.role.controller;

import com.google.gson.Gson;
import fr.lessagasmp3.core.common.constant.Strings;
import fr.lessagasmp3.core.role.model.RoleModel;
import fr.lessagasmp3.core.role.service.RoleService;
import fr.lessagasmp3.core.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api")
public class RoleController {

    private final Gson gson;
    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public RoleController(
            Gson gson,
            RoleService roleService,
            UserService userService) {
        this.gson = gson;
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping(value = "/role/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RoleModel getById(@PathVariable Long id) {
        return roleService.getById(id);
    }

    @PostMapping(value = "/role", produces = MediaType.APPLICATION_JSON_VALUE)
    public RoleModel create(Principal principal, @RequestBody String modelStr) {

        // Convert json to object
        RoleModel model = gson.fromJson(Strings.convertToUtf8(modelStr), RoleModel.class);

        // Save and return object
        return roleService.controlsAndCreate(principal, model);
    }

    @DeleteMapping(value = "/role/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean delete(Principal principal, @PathVariable("id") Long id) {
        return roleService.controlsAndDelete(principal, id);
    }


}
