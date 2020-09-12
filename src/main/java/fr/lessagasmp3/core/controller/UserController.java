package fr.lessagasmp3.core.controller;

import fr.lessagasmp3.core.constant.AuthorityName;
import fr.lessagasmp3.core.entity.Author;
import fr.lessagasmp3.core.entity.Authority;
import fr.lessagasmp3.core.entity.User;
import fr.lessagasmp3.core.exception.BadRequestException;
import fr.lessagasmp3.core.exception.ForbiddenException;
import fr.lessagasmp3.core.exception.NotFoundException;
import fr.lessagasmp3.core.model.UserModel;
import fr.lessagasmp3.core.repository.AuthorRepository;
import fr.lessagasmp3.core.repository.AuthorityRepository;
import fr.lessagasmp3.core.repository.UserRepository;
import fr.lessagasmp3.core.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserModel getById(Principal principal, @PathVariable("id") Long id) {

        // Verify that ID is correct
        if(id <= 0) {
            LOGGER.error("Impossible to get user by ID : ID is incorrect");
            throw new BadRequestException();
        }

        // Verify that principal is the user requested
        Long userLoggedInId = userService.get(principal).getId();
        if(!userLoggedInId.equals(id) && userService.isNotAdmin(userLoggedInId)) {
            LOGGER.error("Impossible to get user {} : principal {} does not match", id, userLoggedInId);
            throw new ForbiddenException();
        }

        // Verify that entity exists
        User entity = userRepository.findById(id).orElse(null);
        if(entity == null) {
            LOGGER.error("Impossible to get user by ID : user not found");
            throw new NotFoundException();
        }

        // Transform and return organization
        UserModel model = UserModel.fromEntity(entity);
        model.hidePassword();
        return model;
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void create(@RequestBody UserModel userModel) {

        // Verify that body is complete
        if(userModel == null ||
                userModel.getEmail() == null || userModel.getEmail().isEmpty() ||
                userModel.getPassword() == null || userModel.getPassword().isEmpty()) {
            LOGGER.error("Impossible to update user : body is incomplete");
            throw new BadRequestException();
        }

        // Get default authority
        Authority userAuthority = authorityRepository.findByName(AuthorityName.ROLE_USER);

        // Create user
        User user = new User(userModel.getEmail(), userModel.getPassword());
        user.setUsername(userModel.getUsername());
        user.addAuthority(userAuthority);
        user.setEnabled(true);
        userRepository.save(user);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/user", method = RequestMethod.PUT, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void save(Principal principal, @RequestBody UserModel userModel) {

        // Verify that body is complete
        if(userModel == null ||
                userModel.getId() <= 0 ||
                userModel.getEmail() == null || userModel.getEmail().isEmpty()) {
            LOGGER.error("Impossible to update user : body is incomplete");
            throw new BadRequestException();
        }

        // Verify that principal has correct privileges :
        // Principal is the contributor OR Principal is admin
        Long userLoggedInId = userService.get(principal).getId();
        if(!userLoggedInId.equals(userModel.getId()) && userService.isNotAdmin(userLoggedInId)) {
            LOGGER.error("Impossible to update user : user {} has not enough privileges", userLoggedInId);
            throw new ForbiddenException();
        }

        // Verify that user exists
        User user = userRepository.findById(userModel.getId()).orElse(null);
        if(user == null) {
            LOGGER.error("Impossible to update user : user {} not found", userModel.getId());
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
        userRepository.save(user);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void delete(Principal principal, @PathVariable Long id) {

        // Verify that ID is correct
        if(id <= 0) {
            LOGGER.error("Impossible to delete user : ID is incorrect");
            throw new BadRequestException();
        }

        // Verify that principal has correct privileges :
        // Principal is the contributor OR Principal is admin
        Long userLoggedInId = userService.get(principal).getId();
        if(!userLoggedInId.equals(id) && userService.isNotAdmin(userLoggedInId)) {
            LOGGER.error("Impossible to delete user : user {} has not enough privileges", userLoggedInId);
            throw new ForbiddenException();
        }

        // Verify that user exists
        User user = userRepository.findById(id).orElse(null);
        if(user == null) {
            LOGGER.error("Impossible to update user : user {} not found", id);
            throw new NotFoundException();
        }

        // Unlink data to user
        Set<Author> authors = authorRepository.findAllByUserId(id);
        authors.forEach(author -> author.setUser(null));
        authorRepository.saveAll(authors);

        // Delete user
        userRepository.deleteById(id);
    }
}
