package fr.lessagasmp3.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthaurizedException extends RuntimeException {

    public UnauthaurizedException() {
    }

    public UnauthaurizedException(String message, Throwable cause) {
        super(message, cause);
    }
}