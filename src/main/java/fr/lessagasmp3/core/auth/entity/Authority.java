package fr.lessagasmp3.core.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.lessagasmp3.core.auth.model.AuthorityModel;
import fr.lessagasmp3.core.common.constant.AuthorityName;
import fr.lessagasmp3.core.user.entity.User;
import jakarta.persistence.FetchType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class Authority extends AuthorityModel implements GrantedAuthority {

    @ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY)
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
