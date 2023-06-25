package fr.lessagasmp3.core.auth.controller;

import fr.lessagasmp3.core.common.constant.Strings;
import fr.lessagasmp3.core.common.security.JwtRequest;
import fr.lessagasmp3.core.common.security.JwtResponse;
import fr.lessagasmp3.core.common.security.JwtTokenUtil;
import fr.lessagasmp3.core.exception.BadRequestException;
import fr.lessagasmp3.core.exception.EntityAlreadyExistsException;
import fr.lessagasmp3.core.exception.NotFoundException;
import fr.lessagasmp3.core.exception.UnauthaurizedException;
import fr.lessagasmp3.core.role.model.RoleModel;
import fr.lessagasmp3.core.role.service.RoleService;
import fr.lessagasmp3.core.user.entity.User;
import fr.lessagasmp3.core.user.model.UserModel;
import fr.lessagasmp3.core.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    private final RoleService roleService;

    private final UserDetailsService jwtInMemoryUserDetailsService;

    private final UserService userService;

    @Autowired
    public AuthenticationController(
            AuthenticationManager authenticationManager,
            JwtTokenUtil jwtTokenUtil,
            RoleService roleService,
            UserDetailsService jwtInMemoryUserDetailsService,
            UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.roleService = roleService;
        this.jwtInMemoryUserDetailsService = jwtInMemoryUserDetailsService;
        this.userService = userService;
    }

    @RequestMapping(value = "/auth/signup", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void signup(@RequestBody UserModel userModel) {
        try {
            userService.controlsAndCreate(userModel);
        } catch (EntityAlreadyExistsException e) {
            throw new BadRequestException();
        }
    }

    @RequestMapping(value = "/auth/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JwtResponse login(@RequestBody JwtRequest request) {
        authenticate(request.getEmail(), request.getPassword());
        final UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername(request.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return new JwtResponse(token);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/auth/whoami/user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserModel whoami(Principal principal) {
        User user = userService.whoami(principal);
        if (user == null) {
            throw new NotFoundException();
        } else {
            user.setPassword(Strings.EMPTY);
            return UserModel.fromEntity(user);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/auth/whoami/roles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<RoleModel> getMyRoles(Principal principal) {
        return roleService.whoami(principal);
    }

    private void authenticate(String email, String password) {
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new UnauthaurizedException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new UnauthaurizedException("INVALID_CREDENTIALS", e);
        }
    }
}