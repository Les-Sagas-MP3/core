package fr.lessagasmp3.core.controller;

import fr.lessagasmp3.core.constant.Strings;
import fr.lessagasmp3.core.entity.User;
import fr.lessagasmp3.core.exception.BadRequestException;
import fr.lessagasmp3.core.exception.EntityAlreadyExistsException;
import fr.lessagasmp3.core.exception.NotFoundException;
import fr.lessagasmp3.core.exception.UnauthaurizedException;
import fr.lessagasmp3.core.model.UserModel;
import fr.lessagasmp3.core.security.JwtRequest;
import fr.lessagasmp3.core.security.JwtResponse;
import fr.lessagasmp3.core.security.JwtTokenUtil;
import fr.lessagasmp3.core.service.UserService;
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

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService jwtInMemoryUserDetailsService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/auth/signup", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void singup(@RequestBody JwtRequest request) {
        try {
            userService.create(request);
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
    @RequestMapping(value = "/auth/whoami", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserModel whoami(Principal principal) {
        User user = userService.get(principal);
        if(user == null) {
            throw new NotFoundException();
        } else {
            user.setPassword(Strings.EMPTY);
            return UserModel.fromEntity(user);
        }
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