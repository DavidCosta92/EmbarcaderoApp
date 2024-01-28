package com.Embarcadero.demo.exceptions.customsExceptions;

public class InvalidValueException extends RuntimeException{
    public InvalidValueException(String message) {
        super(message);
    }
}
