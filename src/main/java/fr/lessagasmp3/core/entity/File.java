package fr.lessagasmp3.core.entity;

import fr.lessagasmp3.core.model.FileModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class File extends FileModel {

}
