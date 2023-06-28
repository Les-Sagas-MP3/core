package fr.lessagasmp3.core.role.controller;

import com.google.gson.Gson;
import fr.lessagasmp3.core.common.constant.Strings;
import fr.lessagasmp3.core.role.model.RoleModel;
import fr.lessagasmp3.core.role.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api")
public class RoleController {

    private final Gson gson;
    private final RoleService roleService;

    @Autowired
    public RoleController(
            Gson gson,
            RoleService roleService) {
        this.gson = gson;
        this.roleService = roleService;
    }

    @GetMapping(value = "/role/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RoleModel getById(@PathVariable Long id) {
        return roleService.getById(id);
    }

    @GetMapping(value = "/role", produces = MediaType.APPLICATION_JSON_VALUE, params = {"userId"})
    public Set<RoleModel> getAllByUserId(@RequestParam("userId") Long userId) {
        return roleService.getAllByUserId(userId);
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
