package fr.lessagasmp3.core.user.model;

import fr.lessagasmp3.core.common.constant.Strings;
import fr.lessagasmp3.core.common.model.AuditModel;
import fr.lessagasmp3.core.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

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

    @NotNull
    protected String avatarUrl = Strings.EMPTY;

    @NotNull
    protected String workspace = Strings.EMPTY;

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
        model.setAvatarUrl(entity.getAvatarUrl());
        model.setWorkspace(entity.getWorkspace());
        if(entity.getCreator() != null) {
            model.setCreatorRef(entity.getCreator().getId());
        }
        return model;
    }

}
