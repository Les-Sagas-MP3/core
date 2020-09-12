package fr.lessagasmp3.core.service;

import fr.lessagasmp3.core.constant.AuthorityName;
import fr.lessagasmp3.core.entity.Authority;
import fr.lessagasmp3.core.entity.User;
import fr.lessagasmp3.core.repository.AuthorityRepository;
import fr.lessagasmp3.core.repository.UserRepository;
import fr.lessagasmp3.core.security.JwtUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserRepository userRepository;

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
        LOGGER.debug("Authorities for user {} :", userId);
        return userAuthorities.stream().map(r -> {
            LOGGER.debug("{}", r.getName().name());
            return new SimpleGrantedAuthority(r.getAuthority());
        }).collect(Collectors.toList());
    }

    public User get(Principal principal) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        UserDetails userPrincipal = (UserDetails) token.getPrincipal();
        return userRepository.findByEmail(userPrincipal.getUsername());
    }

    public boolean isNotAdmin(Long userId) {
        return authorityRepository.findByNameAndUsers_Id(AuthorityName.ROLE_ADMIN, userId) == null;
    }

}