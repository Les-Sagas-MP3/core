package fr.lessagasmp3.core.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@ToString(exclude = {"sagas"})
public class Author extends Audit<String> {

    @NotNull
    private String name = "";

    @NotNull
    private Integer nbSagas = 0;

    @ManyToMany(mappedBy="authors")
    private Set<Saga> sagas = new LinkedHashSet<>();

}
