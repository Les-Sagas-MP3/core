package fr.lessagasmp3.core.role.service;

import fr.lessagasmp3.core.exception.BadRequestException;
import fr.lessagasmp3.core.exception.EntityAlreadyExistsException;
import fr.lessagasmp3.core.exception.ForbiddenException;
import fr.lessagasmp3.core.exception.NotFoundException;
import fr.lessagasmp3.core.role.entity.Role;
import fr.lessagasmp3.core.role.model.RoleModel;
import fr.lessagasmp3.core.role.model.RoleName;
import fr.lessagasmp3.core.role.repository.RoleRepository;
import fr.lessagasmp3.core.saga.entity.Saga;
import fr.lessagasmp3.core.saga.service.SagaService;
import fr.lessagasmp3.core.user.entity.User;
import fr.lessagasmp3.core.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

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

    public Role getById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    public Role getRole(RoleName roleName, Long userId) {
        return roleRepository.findByNameAndUserId(roleName, userId).orElse(null);
    }

    public Role getRole(RoleName roleName, Long userId, Long sagaId) {
        return roleRepository.findByNameAndUserIdAndSagaId(roleName, userId, sagaId).orElse(null);
    }

    public Set<RoleModel> getAllByUserId(Long userId) {
        Set<Role> roles = roleRepository.findAllByUserId(userId);
        Set<RoleModel> roleModels = new LinkedHashSet<>();
        roles.forEach(role -> roleModels.add(RoleModel.fromEntity(role)));
        return roleModels;
    }

    public Set<RoleModel> whoami(Principal principal) {
        User user = userService.whoami(principal);
        if(user == null) {
            log.error("Impossible to get roles for principal : user does not exist");
            throw new NotFoundException();
        }
        return this.getAllByUserId(user.getId());
    }

    public RoleModel controlsAndCreate(Principal principal, RoleModel model) {

        // Verify that body is complete
        if(model == null) {
            log.error("Impossible to create role : body is incomplete");
            throw new BadRequestException();
        }

        // Verify that role do not already exists
        Role role;
        if(model.getSagaRef() > 0) {
            role = getRole(model.getName(), model.getUserRef(), model.getSagaRef());
        } else {
            role = getRole(model.getName(), model.getUserRef());
        }
        if(role != null) {
            log.error("Impossible to create role : it already exists");
            throw new EntityAlreadyExistsException();
        }

        // Verify that entities exists
        User user = userService.getById(model.getUserRef());
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
        Long userPrincipalId = userService.whoami(principal).getId();
        if(model.getName() != RoleName.MEMBER) {
            if(model.getSagaRef() <= 0 && userService.isNotAdmin(userPrincipalId)) {
                log.error("Impossible to create role : user {} has not enough privilege", userPrincipalId);
                throw new ForbiddenException();
            }
            if(model.getSagaRef() > 0 && userService.isNotAuthor(userPrincipalId, model.getSagaRef())) {
                log.error("Impossible to create role : user {} is not author of saga {}", userPrincipalId, model.getSagaRef());
                throw new ForbiddenException();
            }
        }

        return create(model);
    }

    public RoleModel create(RoleModel model) {
        Role role = Role.fromModel(model);
        return RoleModel.fromEntity(roleRepository.save(role));
    }

    public Boolean controlsAndDelete(Principal principal, Long roleId) {

        // Check if ID is correct
        if(roleId <= 0) {
            log.error("Impossible to delete role : ID is missing");
            throw new BadRequestException();
        }

        // Check if file is registered in DB
        Role entity = roleRepository.findById(roleId).orElse(null);
        if(entity == null) {
            log.error("Impossible to delete role : role {} does not exist in DB", roleId);
            throw new NotFoundException();
        }

        // Verify that principal has privileges
        Long userPrincipalId = userService.whoami(principal).getId();
        Long sagaId = entity.getSaga() == null ? 0L : entity.getSaga().getId();
        if(sagaId <= 0 && userService.isNotAdmin(userPrincipalId)) {
            log.error("Impossible to delete role : user {} has not enough privilege", userPrincipalId);
            throw new ForbiddenException();
        }
        if(sagaId > 0 && userService.isNotAdmin(userPrincipalId) && userService.isNotAuthor(userPrincipalId, sagaId)) {
            log.error("Impossible to delete role : user {} is not author of saga {}", userPrincipalId, sagaId);
            throw new ForbiddenException();
        }
        if(Objects.equals(userPrincipalId, entity.getUser().getId())) {
            log.error("Impossible to delete role : user {} cannot modify its own privileges", userPrincipalId);
            throw new ForbiddenException();
        }

        return delete(roleId);
    }

    public Boolean delete(Long roleId) {
        roleRepository.deleteById(roleId);
        return Boolean.TRUE;
    }

}
