package fr.lessagasmp3.core.model;

import fr.lessagasmp3.core.entity.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@MappedSuperclass
public class CategoryModel extends AuditModel<String> {

    @NotNull
    protected String name = "";

    @NotNull
    protected Integer nbSagas = 0;

    @Transient
    private Set<Long> sagasRef = new LinkedHashSet<>();

    public static CategoryModel fromEntity(Category entity) {
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
