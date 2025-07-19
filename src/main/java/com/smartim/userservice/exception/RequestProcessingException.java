package com.smartim.userservice.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a request cannot be processed by the system.
 * Automatically returns HTTP 400 (Bad Request) when thrown in a controller.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class RequestProcessingException extends RuntimeException {
    /**
     * Constructs a new RequestProcessingException with a detailed message.
     *
     * @param message exception message
     */
    public RequestProcessingException(String message){
        super(message);
    }
}
