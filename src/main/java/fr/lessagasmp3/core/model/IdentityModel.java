package fr.lessagasmp3.core.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@MappedSuperclass
public class IdentityModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

}

