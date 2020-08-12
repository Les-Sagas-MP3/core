package fr.lessagasmp3.core.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class File extends Audit<String> {

    @NotNull
    private String directory = "";

    @NotNull
    private String name = "";

    @NotNull
    private String path = "";

    @Column(columnDefinition = "TEXT")
    @NotNull
    private String content;

}
