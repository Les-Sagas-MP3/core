package fr.lessagasmp3.core.role.model;

import fr.lessagasmp3.core.common.model.AuditModel;
import fr.lessagasmp3.core.role.entity.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@MappedSuperclass
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class RoleModel extends AuditModel<String> {

    @Column(length = 50)
    @NotNull
    @Enumerated(EnumType.STRING)
    protected RoleName name = RoleName.MEMBER;

    @Transient
    private Long userRef = 0L;

    @Transient
    private Long sagaRef = 0L;

    public static RoleModel fromEntity(Role entity) {
        Objects.requireNonNull(entity);
        RoleModel model = new RoleModel();
        model.setCreatedAt(entity.getCreatedAt());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setUpdatedBy(entity.getUpdatedBy());
        model.setId(entity.getId());
        model.setName(entity.getName());
        if(entity.getUser() != null) {
            model.setUserRef(entity.getUser().getId());
        }
        if(entity.getSaga() != null) {
            model.setSagaRef(entity.getSaga().getId());
        }
        return model;
    }

}
