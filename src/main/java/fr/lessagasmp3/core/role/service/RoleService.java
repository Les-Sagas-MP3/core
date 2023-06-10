package fr.lessagasmp3.core.role.service;

import fr.lessagasmp3.core.exception.BadRequestException;
import fr.lessagasmp3.core.exception.NotFoundException;
import fr.lessagasmp3.core.role.entity.Role;
import fr.lessagasmp3.core.role.model.RoleModel;
import fr.lessagasmp3.core.role.repository.RoleRepository;
import fr.lessagasmp3.core.saga.entity.Saga;
import fr.lessagasmp3.core.saga.service.SagaService;
import fr.lessagasmp3.core.user.entity.User;
import fr.lessagasmp3.core.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Slf4j
@Service
public class RoleService {

    private final SagaService sagaService;
    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(
            SagaService sagaService,
            UserService userService,
            RoleRepository roleRepository) {
        this.sagaService = sagaService;
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    public Role findInDbById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    public RoleModel create(Principal principal, RoleModel model) {

        // Verify that body is complete
        if(model == null) {
            log.error("Impossible to create role : body is incomplete");
            throw new BadRequestException();
        }

        // Verify that role do not already exists
        Role role;
        if(model.getSagaRef() > 0) {
            role = roleRepository.findByNameAndUserIdAndSagaId(model.getName(), model.getUserRef(), model.getSagaRef()).orElse(null);
        } else {
            role = roleRepository.findByNameAndUserId(model.getName(), model.getUserRef()).orElse(null);
        }
        if(role != null) {
            log.error("Impossible to create role : it already exists");
            throw new BadRequestException();
        }

        // Verify that entities exists
        User user = userService.findById(model.getUserRef());
        if(user == null) {
            log.error("Impossible to create role : user {} does not exist", model.getUserRef());
            throw new NotFoundException();
        }
        if(model.getSagaRef() > 0) {
            Saga saga = sagaService.findById(model.getSagaRef());
            if(saga == null) {
                log.error("Impossible to create role : saga {} does not exist", model.getSagaRef());
                throw new NotFoundException();
            }
        }

        // Verify that principal has privileges
        Long userPrincipalId = userService.get(principal).getId();
        if(userService.isNotAdmin(userPrincipalId)) {
            log.error("Impossible to create role : user {} has not enough privilege", userPrincipalId);
            throw new NotFoundException();
        }

        // Create entity
        role = Role.fromModel(model);
        return RoleModel.fromEntity(roleRepository.save(role));
    }

    public Boolean delete(Long fileId) {

        // Check if ID is correct
        if(fileId <= 0) {
            log.error("Impossible to delete role : ID is missing");
            throw new BadRequestException();
        }

        // Check if file is registered in DB
        Role entity = roleRepository.findById(fileId).orElse(null);
        if(entity == null) {
            log.error("Impossible to delete role : role {} does not exist in DB", fileId);
            throw new NotFoundException();
        }

        // Delete file in DB
        roleRepository.delete(entity);

        return Boolean.TRUE;
    }

}
