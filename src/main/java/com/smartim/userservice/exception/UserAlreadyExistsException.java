package com.smartim.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when system receives a bad request.
 * Automatically returns HTTP 400 (Bad Request) when thrown in a controller.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserAlreadyExistsException extends RuntimeException{

    /**
     * Constructs a new UserAlreadyExistsException with a detailed message.
     *
     * @param message exception message
     */
    public UserAlreadyExistsException(String message){
        super(message);
    }
}
