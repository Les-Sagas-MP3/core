package fr.lessagasmp3.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.lessagasmp3.core.constant.AuthorityName;
import fr.lessagasmp3.core.model.AuthorityModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@ToString(exclude = {"users"})
public class Authority extends AuthorityModel implements GrantedAuthority {

    @ManyToMany(mappedBy = "authorities")
    @JsonIgnoreProperties(value = {"username", "password", "email", "enabled", "lastPasswordResetDate", "authorities"})
    private Set<User> users = new LinkedHashSet<>();

    public Authority() {}

    public Authority(AuthorityName name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return getName().name();
    }
}
