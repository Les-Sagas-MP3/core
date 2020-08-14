package fr.lessagasmp3.core.entity;

import fr.lessagasmp3.core.model.RssMessageModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@ToString
public class RssMessage extends RssMessageModel {

}
