package com.smartim.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a requested resource is not found in the system.
 * Automatically returns HTTP 404 (Not Found) when thrown in a controller.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException  extends RuntimeException{

    /**
     * Constructs a new ResourceNotFoundException with a detailed message.
     *
     * @param resourceName the name of the resource (e.g., "User")
     * @param fieldName    the field used to search the resource (e.g., "email", "id")
     * @param fieldValue   the value of the field that was not found
     */
    public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue){
        super(String.format("%s not found with the given input data %s: '%s'",
                resourceName, fieldName, fieldValue));
    }
}