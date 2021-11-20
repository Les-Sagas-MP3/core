package fr.lessagasmp3.core.common.security;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class JwtRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 5926468583005150707L;

    private String email;
    private String password;

}
