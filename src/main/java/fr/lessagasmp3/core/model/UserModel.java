package fr.lessagasmp3.core.model;

import fr.lessagasmp3.core.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

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

    public static UserModel fromEntity(User entity) {
        UserModel model = new UserModel();
        model.setCreatedAt(entity.getCreatedAt());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setUpdatedBy(entity.getUpdatedBy());
        model.setId(entity.getId());
        model.setUsername(entity.getUsername());
        model.setPassword(entity.getPassword());
        model.setEmail(entity.getEmail());
        model.setEnabled(entity.getEnabled());
        model.setLastPasswordResetDate(entity.getLastPasswordResetDate());
        return model;
    }

    public void hidePassword() {
        this.password = "***";
    }
}
