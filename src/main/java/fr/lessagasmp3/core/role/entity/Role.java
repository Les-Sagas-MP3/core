package fr.lessagasmp3.core.role.entity;

import fr.lessagasmp3.core.role.model.RoleModel;
import fr.lessagasmp3.core.saga.entity.Saga;
import fr.lessagasmp3.core.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class Role extends RoleModel {

    @ManyToOne(fetch = FetchType.LAZY)
    private Saga saga = new Saga();

    @ManyToOne(fetch = FetchType.LAZY)
    private User user = new User();

    public static Role fromModel(RoleModel model) {
        Role entity = new Role();
        entity.setCreatedAt(model.getCreatedAt());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setUpdatedBy(model.getUpdatedBy());
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setSagaRef(model.getSagaRef());
        entity.setUserRef(model.getUserRef());
        return entity;
    }
}
