package fr.lessagasmp3.core.category.model;

import fr.lessagasmp3.core.category.entity.Category;
import fr.lessagasmp3.core.common.model.AuditModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@MappedSuperclass
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class CategoryModel extends AuditModel<String> {

    @NotNull
    protected String name = "";

    @NotNull
    protected Integer nbSagas = 0;

    @Transient
    private Set<Long> sagasRef = new LinkedHashSet<>();

    public static CategoryModel fromEntity(Category entity) {
        Objects.requireNonNull(entity);
        CategoryModel model = new CategoryModel();
        model.setCreatedAt(entity.getCreatedAt());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setUpdatedBy(entity.getUpdatedBy());
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setNbSagas(entity.getNbSagas());
        entity.getSagas().forEach(saga -> model.getSagasRef().add(saga.getId()));
        return model;
    }

}
