package fr.lessagasmp3.core.user.service;

import fr.lessagasmp3.core.auth.entity.Authority;
import fr.lessagasmp3.core.auth.repository.AuthorityRepository;
import fr.lessagasmp3.core.common.constant.AuthorityName;
import fr.lessagasmp3.core.common.security.JwtRequest;
import fr.lessagasmp3.core.common.security.JwtUser;
import fr.lessagasmp3.core.exception.EntityAlreadyExistsException;
import fr.lessagasmp3.core.exception.UnauthaurizedException;
import fr.lessagasmp3.core.user.entity.User;
import fr.lessagasmp3.core.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserRepository userRepository;

    public void create(JwtRequest jwtRequest) {
        User user = userRepository.findByEmail(jwtRequest.getEmail());
        if (user != null) {
            throw new EntityAlreadyExistsException();
        } else {
            user = new User();
            user.setEmail(jwtRequest.getEmail());
            user.setPassword(BCrypt.hashpw(jwtRequest.getPassword(), BCrypt.gensalt()));
            user.setEnabled(true);
            user.setUsername(jwtRequest.getEmail());

            Authority authority = authorityRepository.findByName(AuthorityName.ROLE_USER);
            user.getAuthorities().add(authority);
            userRepository.save(user);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new JwtUser(email, user.getPassword(), getAuthorities(user.getId()));
    }

    public List<GrantedAuthority> getAuthorities(Long userId) {
        Set<Authority> userAuthorities = authorityRepository.findAllByUsers_Id(userId);
        log.debug("Authorities for user {} :", userId);
        return userAuthorities.stream().map(r -> {
            log.debug("{}", r.getName().name());
            return new SimpleGrantedAuthority(r.getAuthority());
        }).collect(Collectors.toList());
    }

    public User get(Principal principal) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        if(token == null) {
            throw new UnauthaurizedException();
        }
        UserDetails userPrincipal = (UserDetails) token.getPrincipal();
        return userRepository.findByEmail(userPrincipal.getUsername());
    }

    public boolean isNotAdmin(Long userId) {
        return authorityRepository.findByNameAndUsers_Id(AuthorityName.ROLE_ADMIN, userId) == null;
    }

}