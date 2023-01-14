package fr.lessagasmp3.core.user.model;

import fr.lessagasmp3.core.common.model.AuditModel;
import fr.lessagasmp3.core.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@ToString
public class UserModel extends AuditModel<String> {

    @NotNull
    protected String username;

    @NotNull
    protected String password;

    @Column(unique = true)
    @NotNull
    protected String email;

    @NotNull
    protected Boolean enabled = false;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    protected Date lastPasswordResetDate = new Date();

    @Transient
    protected Long creatorRef = 0L;

    @Transient
    private Set<Long> authoritiesRef = new LinkedHashSet<>();

    public static UserModel fromEntity(User entity) {
        Objects.requireNonNull(entity);
        UserModel model = new UserModel();
        model.setCreatedAt(entity.getCreatedAt());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setUpdatedBy(entity.getUpdatedBy());
        model.setId(entity.getId());
        model.setUsername(entity.getUsername());
        model.setPassword("***");
        model.setEmail(entity.getEmail());
        model.setEnabled(entity.getEnabled());
        model.setLastPasswordResetDate(entity.getLastPasswordResetDate());
        if(entity.getCreator() != null) {
            model.setCreatorRef(entity.getCreator().getId());
        }
        entity.getAuthorities().forEach(authority -> model.getAuthoritiesRef().add(authority.getId()));
        return model;
    }

}
