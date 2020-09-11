package fr.lessagasmp3.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.lessagasmp3.core.model.UserModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = "users")
@Table
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@ToString(exclude = {"authorities"})
public class User extends UserModel {

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "id")})
    @JsonIgnoreProperties(value = {"createdAt", "createdBy", "updatedAt", "updatedBy", "users"})
    private Set<Authority> authorities = new LinkedHashSet<>();

    public User() {
        super();
    }

    public User(String email, String password) {
        this.email = email;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public void addAuthority(Authority newAuthority) {
        Long newAuthorityId = newAuthority.getId();
        this.authorities.stream()
                .filter(authority -> newAuthorityId.equals(authority.getId()))
                .findAny()
                .ifPresentOrElse(
                        authority -> {},
                        () -> this.authorities.add(newAuthority));
    }
}
