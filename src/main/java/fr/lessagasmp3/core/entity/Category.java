package fr.lessagasmp3.core.entity;

import fr.lessagasmp3.core.model.CategoryModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@ToString(exclude = {"sagas"})
public class Category extends CategoryModel {

    @ManyToMany(mappedBy="categories")
    private Set<Saga> sagas = new LinkedHashSet<>();

}
