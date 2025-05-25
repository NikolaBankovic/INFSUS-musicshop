package hr.fer.infsus.exception;

import lombok.Getter;

@Getter
public class InvalidTokenException extends Exception {
    public InvalidTokenException(final String message) {
        super(message);
    }
}