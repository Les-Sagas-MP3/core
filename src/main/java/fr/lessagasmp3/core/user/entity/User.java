package fr.lessagasmp3.core.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.lessagasmp3.core.creator.entity.Creator;
import fr.lessagasmp3.core.auth.entity.Authority;
import fr.lessagasmp3.core.user.model.UserModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = "users")
@Table
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class User extends UserModel {

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "id")})
    @JsonIgnoreProperties(value = {"createdAt", "createdBy", "updatedAt", "updatedBy", "users"})
    private Set<Authority> authorities = new LinkedHashSet<>();

    @OneToOne(mappedBy = "user")
    @JsonIgnoreProperties(value = {"sagasWritten", "sagasComposed", "user"})
    private Creator creator;

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

    public static User fromModel(UserModel model) {
        User entity = new User();
        entity.setCreatedAt(model.getCreatedAt());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setUpdatedBy(model.getUpdatedBy());
        entity.setId(model.getId());
        entity.setUsername(model.getUsername());
        entity.setPassword(model.getPassword());
        entity.setEmail(model.getEmail());
        entity.setEnabled(model.getEnabled());
        entity.setLastPasswordResetDate(model.getLastPasswordResetDate());
        entity.setCreatorRef(model.getCreatorRef());
        entity.setAuthoritiesRef(model.getAuthoritiesRef());
        return entity;
    }
}
