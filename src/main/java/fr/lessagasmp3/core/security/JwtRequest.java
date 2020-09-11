package fr.lessagasmp3.core.security;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class JwtRequest implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    private String email;
    private String password;

    //default constructor for JSON Parsing
    public JwtRequest() {}

    public JwtRequest(String email, String password) {
        this.setEmail(email);
        this.setPassword(password);
    }

}
