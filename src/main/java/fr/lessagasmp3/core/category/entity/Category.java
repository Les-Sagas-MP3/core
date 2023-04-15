package fr.lessagasmp3.core.category.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.lessagasmp3.core.category.model.CategoryModel;
import fr.lessagasmp3.core.saga.entity.Saga;
import jakarta.persistence.FetchType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class Category extends CategoryModel {

    @ManyToMany(mappedBy="categories", fetch = FetchType.LAZY)
    private Set<Saga> sagas = new LinkedHashSet<>();

    public static Category fromModel(CategoryModel model) {
        Category entity = new Category();
        entity.setCreatedAt(model.getCreatedAt());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setUpdatedAt(model.getUpdatedAt());
        entity.setUpdatedBy(model.getUpdatedBy());
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setNbSagas(model.getNbSagas());
        entity.setSagasRef(model.getSagasRef());
        return entity;
    }
}
