package fr.lessagasmp3.core.user.service;

import fr.lessagasmp3.core.common.security.JwtRequest;
import fr.lessagasmp3.core.common.security.JwtUser;
import fr.lessagasmp3.core.creator.entity.Creator;
import fr.lessagasmp3.core.creator.repository.CreatorRepository;
import fr.lessagasmp3.core.exception.*;
import fr.lessagasmp3.core.role.entity.Role;
import fr.lessagasmp3.core.role.model.RoleName;
import fr.lessagasmp3.core.role.repository.RoleRepository;
import fr.lessagasmp3.core.user.entity.User;
import fr.lessagasmp3.core.user.model.UserModel;
import fr.lessagasmp3.core.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final CreatorRepository creatorRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserService(
            CreatorRepository creatorRepository,
            RoleRepository roleRepository,
            UserRepository userRepository) {
        this.creatorRepository = creatorRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new JwtUser(email, user.getPassword(), getAuthorities(user.getId()));
    }

    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Set<User> getAllByUsername(String username) {
        return userRepository.findAllByUsernameContainsIgnoreCaseOrderByUsernameAsc(username);
    }

    public long count() {
        return userRepository.count();
    }

    public UserModel controlsAndGetById(Principal principal, Long id) {

        // Verify that ID is correct
        if(id <= 0) {
            log.error("Impossible to get user by ID : ID is incorrect");
            throw new BadRequestException();
        }

        // Verify that principal is the user requested
        Long userLoggedInId = this.whoami(principal).getId();
        if(!userLoggedInId.equals(id) && this.isNotAdmin(userLoggedInId)) {
            log.error("Impossible to get user {} : principal {} does not match", id, userLoggedInId);
            throw new ForbiddenException();
        }

        // Verify that entity exists
        User entity = this.getById(id);
        if(entity == null) {
            log.error("Impossible to get user by ID : user not found");
            throw new NotFoundException();
        }

        // Transform and return organization
        return UserModel.fromEntity(entity);
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User controlsAndCreate(UserModel model) {

        // Verify that body is complete
        if(model == null ||
                model.getEmail() == null || model.getEmail().isEmpty() ||
                model.getPassword() == null || model.getPassword().isEmpty()) {
            log.error("Impossible to create user : body is incomplete");
            throw new BadRequestException();
        }

        // Verify that user does not already exist
        User user = userRepository.findByEmail(model.getEmail());
        if(user != null) {
            log.error("Impossible to create user : it already exists");
            throw new EntityAlreadyExistsException();
        }

        // Create user
        JwtRequest jwtUserRequest = new JwtRequest();
        jwtUserRequest.setEmail(model.getEmail());
        jwtUserRequest.setPassword(model.getPassword());
        return this.create(jwtUserRequest, model.getUsername());
    }

    public User create(JwtRequest jwtRequest, String username) {

        // Create user
        User user = new User();
        user.setUsername(username);
        user.setEmail(jwtRequest.getEmail());
        user.setPassword(BCrypt.hashpw(jwtRequest.getPassword(), BCrypt.gensalt()));
        user.setEnabled(true);
        user.setWorkspace(UUID.randomUUID().toString());
        user = userRepository.save(user);

        // Assign default role
        Role role = new Role();
        role.setName(RoleName.MEMBER);
        role.setUser(user);
        role.setSaga(null);
        roleRepository.save(role);

        return user;
    }

    public void controlsAndUpdate(Principal principal, UserModel userModel) {

        // Verify that body is complete
        if(userModel == null ||
                userModel.getId() <= 0 ||
                userModel.getEmail() == null || userModel.getEmail().isEmpty()) {
            log.error("Impossible to update user : body is incomplete");
            throw new BadRequestException();
        }

        // Verify that principal has correct privileges :
        // Principal is the contributor OR Principal is admin
        Long userLoggedInId = this.whoami(principal).getId();
        if(!userLoggedInId.equals(userModel.getId()) && this.isNotAdmin(userLoggedInId)) {
            log.error("Impossible to update user : user {} has not enough privileges", userLoggedInId);
            throw new ForbiddenException();
        }

        // Verify that user exists
        User user = this.getById(userModel.getId());
        if(user == null) {
            log.error("Impossible to update user : user {} not found", userModel.getId());
            throw new NotFoundException();
        }

        // Update and save user
        if(userModel.getPassword() != null && !userModel.getPassword().isEmpty()) {
            user.setPassword(BCrypt.hashpw(userModel.getPassword(), BCrypt.gensalt()));
            user.setLastPasswordResetDate(new Date());
        }
        user.setUsername(userModel.getUsername());
        user.setEmail(userModel.getEmail());
        user.setEnabled(userModel.getEnabled());
        user.setAvatarUrl(userModel.getAvatarUrl());
        userRepository.save(user);
    }

    public void controlsAndDelete(Principal principal, Long id) {

        // Verify that ID is correct
        if(id <= 0) {
            log.error("Impossible to delete user : ID is incorrect");
            throw new BadRequestException();
        }

        // Verify that principal has correct privileges :
        // Principal is the contributor OR Principal is admin
        Long userLoggedInId = this.whoami(principal).getId();
        if(!userLoggedInId.equals(id) && this.isNotAdmin(userLoggedInId)) {
            log.error("Impossible to delete user : user {} has not enough privileges", userLoggedInId);
            throw new ForbiddenException();
        }

        // Verify that user exists
        User user = this.getById(id);
        if(user == null) {
            log.error("Impossible to update user : user {} not found", id);
            throw new NotFoundException();
        }

        // Unlink data to user
        Set<Creator> creators = creatorRepository.findAllByUserId(id);
        creators.forEach(author -> author.setUser(null));
        creatorRepository.saveAll(creators);

        // Delete user
        userRepository.deleteById(id);
    }

    public List<GrantedAuthority> getAuthorities(Long userId) {
        Set<Role> roles = roleRepository.findAllByUserId(userId);
        log.debug("Authorities for user {} :", userId);
        return roles.stream().map(r -> {
            log.debug("{}", r.getName().name());
            return new SimpleGrantedAuthority("ROLE_" + r.getName().name());
        }).collect(Collectors.toList());
    }

    public User whoami(Principal principal) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        if(token == null) {
            throw new UnauthaurizedException();
        }
        UserDetails userPrincipal = (UserDetails) token.getPrincipal();
        return userRepository.findByEmail(userPrincipal.getUsername());
    }

    public boolean isNotAdmin(Long userId) {
        return roleRepository.findByNameAndUserId(RoleName.ADMINISTRATOR, userId).orElse(null) == null;
    }

    public boolean isNotAuthor(Long userId, Long sagaId) {
        return roleRepository.findByNameAndUserIdAndSagaId(RoleName.ADMINISTRATOR, userId, sagaId).orElse(null) == null;
    }

}