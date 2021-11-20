package fr.lessagasmp3.core.common.security;

import lombok.AccessLevel;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter(AccessLevel.PUBLIC)
public class JwtResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -8091879091924046844L;

    private final String token;

    public JwtResponse(String token) {
        this.token = token;
    }

}