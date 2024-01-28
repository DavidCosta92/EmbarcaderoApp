package com.Embarcadero.demo.exceptions.customsExceptions;

public class InvalidJwtException extends RuntimeException{
    public InvalidJwtException(String msg){super(msg);}
}
