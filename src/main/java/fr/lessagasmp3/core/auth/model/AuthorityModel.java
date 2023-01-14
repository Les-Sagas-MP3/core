package fr.lessagasmp3.core.auth.model;

import fr.lessagasmp3.core.auth.entity.Authority;
import fr.lessagasmp3.core.common.constant.AuthorityName;
import fr.lessagasmp3.core.common.model.AuditModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@MappedSuperclass
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class AuthorityModel extends AuditModel<String> {

    @Column(length = 50)
    @NotNull
    @Enumerated(EnumType.STRING)
    protected AuthorityName name;

    public static AuthorityModel fromEntity(Authority entity) {
        Objects.requireNonNull(entity);
        AuthorityModel model = new AuthorityModel();
        model.setCreatedAt(entity.getCreatedAt());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setUpdatedBy(entity.getUpdatedBy());
        model.setId(entity.getId());
        model.setName(entity.getName());
        return model;
    }
}
