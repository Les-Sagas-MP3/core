package fr.lessagasmp3.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.lessagasmp3.core.constant.AuthorityName;
import fr.lessagasmp3.core.model.AuthorityModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
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

    public static Authority fromModel(AuthorityModel model) {
        Authority entity = new Authority();
        entity.setCreatedAt(model.getCreatedAt());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setUpdatedBy(model.getUpdatedBy());
        entity.setId(model.getId());
        entity.setName(model.getName());
        return entity;
    }
}
