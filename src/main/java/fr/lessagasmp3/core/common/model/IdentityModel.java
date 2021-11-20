package fr.lessagasmp3.core.common.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@MappedSuperclass
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@ToString
public class IdentityModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id = 0L;

}

