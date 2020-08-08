package fr.lessagasmp3.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@ToString
public class Saga extends Audit<String> {

    @NotNull
    private String title = "";

    private String url = "";

    @NotNull
    private String urlWiki = "";

    @ManyToMany
    @JsonIgnoreProperties(value = {"sagas"})
    private Set<Author> authors = new LinkedHashSet<>();

    @ManyToMany
    @JsonIgnoreProperties(value = {"sagas"})
    private Set<Category> categories = new LinkedHashSet<>();

    @NotNull
    private Integer levelArt = 100;

    @NotNull
    private Integer levelTech = 100;

    @NotNull
    private Integer nbReviews = 0;

    @NotNull
    private String urlReviews = "";

    @NotNull
    private Integer nbBravos = 0;

}
