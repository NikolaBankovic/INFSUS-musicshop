package hr.fer.infsus.exception;

public class SamePasswordException extends Exception{
    public SamePasswordException(final String message) {
        super(message);
    }
}