package fr.lessagasmp3.core.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@ToString
public class RssMessage extends Audit<String>  {

    @NotNull
    private String feedTitle;

    @NotNull
    private String title;

    @NotNull
    private String pubdate;

    @Column(columnDefinition = "TEXT")
    @NotNull
    private String description;

    @NotNull
    private String link;

    @NotNull
    private String author;

    @NotNull
    private String guid;

}
