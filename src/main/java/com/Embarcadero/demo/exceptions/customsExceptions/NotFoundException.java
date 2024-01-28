package com.Embarcadero.demo.exceptions.customsExceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException (String msg){
        super(msg);
    }
}
