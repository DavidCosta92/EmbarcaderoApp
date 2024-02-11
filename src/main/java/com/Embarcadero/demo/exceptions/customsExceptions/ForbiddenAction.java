package com.Embarcadero.demo.exceptions.customsExceptions;

public class ForbiddenAction extends RuntimeException{
    public ForbiddenAction(String msg){ super(msg) ; }
}
