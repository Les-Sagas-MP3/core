package fr.lessagasmp3.core.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@ToString
public class RssMessage extends Audit<String>  {

    private String feedTitle;

    private String title;

    private String pubdate;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String link;

    private String author;

    private String guid;

}
