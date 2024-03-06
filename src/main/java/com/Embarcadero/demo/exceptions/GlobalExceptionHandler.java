package com.Embarcadero.demo.exceptions;

import com.Embarcadero.demo.exceptions.customsExceptions.*;
import com.Embarcadero.demo.model.entities.enums.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    // excepts personalizadas
    // excepts personalizadas
    @ExceptionHandler(AlreadyExistException.class)
    @ResponseBody
    public ResponseEntity<ExceptionMessages> alreadyExistException (RuntimeException ex){
        return new ResponseEntity<ExceptionMessages>(new ExceptionMessages(ex.getMessage(), InternalExceptionCodes.DUPLICATE_VALUES.ordinal()) , HttpStatus.CONFLICT);
    }
    @ExceptionHandler(NotFoundInputException.class)
    @ResponseBody
    public ResponseEntity<ExceptionMessages> notFoundInputException (RuntimeException ex){
        return new ResponseEntity<ExceptionMessages>(new ExceptionMessages(ex.getMessage(), InternalExceptionCodes.MISSING_VALUES.ordinal()) , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionMessages> notFoundException (RuntimeException ex){
        return new ResponseEntity<ExceptionMessages>(new ExceptionMessages(ex.getMessage(), InternalExceptionCodes.NOT_FOUND_BY_ID.ordinal()) , HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InvalidJwtException.class)
    public ResponseEntity<ExceptionMessages> invalidJwtException (InvalidJwtException ex){
        return new ResponseEntity<ExceptionMessages>(new ExceptionMessages(ex.getMessage(), InternalExceptionCodes.WRONG_JWT.ordinal()) , HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ForbiddenAction.class)
    public ResponseEntity<ExceptionMessages> forbiddenActionEx (ForbiddenAction ex){
        return new ResponseEntity<ExceptionMessages>(new ExceptionMessages(ex.getMessage(), InternalExceptionCodes.FORBIDDEN_ACTION.ordinal()) , HttpStatus.FORBIDDEN);
    }
    // excepts defecto
    // excepts defecto

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<ExceptionMessages> enumInputError (HttpMessageNotReadableException ex){
        String cause = ex.getCause().toString();
        String msg = "";
        if(cause.contains("EngineType_enum")) msg = "Error en tipo de motor, las opciones son : " + Arrays.asList(EngineType_enum.class.getEnumConstants());
        if(cause.contains("LicenseState_enum")) msg = "Error estado de matricula, las opciones son : " +  Arrays.asList(LicenseState_enum.class.getEnumConstants());
        if(cause.contains("TypeLicencedBoat_enum")) msg = "Error en tipo de embarcacion, las opciones con matricula son : " +  Arrays.asList(TypeLicencedBoat_enum.class.getEnumConstants());
        if(cause.contains("TypeSimpleBoat_enum")) msg = "Error en tipo de embarcacion, las opciones sin matricula son : " +  Arrays.asList(TypeSimpleBoat_enum.class.getEnumConstants());
        return new ResponseEntity<ExceptionMessages>(new ExceptionMessages(msg, InternalExceptionCodes.ILLEGAL_ARGS.ordinal()) , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidValueException.class)
    // @ResponseBody
    public ResponseEntity<ExceptionMessages> handlerInvalidValueException (InvalidValueException ex){
        return new ResponseEntity<ExceptionMessages>(new ExceptionMessages(ex.getMessage(), InternalExceptionCodes.ILLEGAL_ARGS.ordinal()) , HttpStatus.NOT_ACCEPTABLE);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    // @ResponseBody
    public ResponseEntity<ExceptionMessages> handlerArgException (IllegalArgumentException ex){
        return new ResponseEntity<ExceptionMessages>(new ExceptionMessages(ex.getMessage(), InternalExceptionCodes.ILLEGAL_ARGS.ordinal()) , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionMessages> handlerMethodArgumentNotValidException (MethodArgumentNotValidException ex){
        String exMsg ="";
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        if(errors.size() > 0){
            for (int i = 0 ; i < errors.size(); i++){
                exMsg += "Error "+ (i+1) + ": "+errors.get(i).getDefaultMessage()+" - ";
            }
        }
        return new ResponseEntity<>(new ExceptionMessages(exMsg , InternalExceptionCodes.ILLEGAL_ARGS.ordinal()) , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionMessages> accessDeniedEx(AccessDeniedException ex){
        return new ResponseEntity<ExceptionMessages>(new ExceptionMessages(ex.getMessage() , InternalExceptionCodes.ACCESS_DENIED.ordinal()),  HttpStatus.FORBIDDEN );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionMessages> runtimeException (RuntimeException ex){
        return new ResponseEntity<ExceptionMessages>(new ExceptionMessages(ex.getMessage(), InternalExceptionCodes.ACCESS_DENIED.ordinal()) , HttpStatus.BAD_REQUEST);
    }

}
