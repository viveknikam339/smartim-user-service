package com.smartim.userservice.exception;

import com.smartim.userservice.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

/**
 * GlobalExceptionHandler is a centralized exception handling component
 * that intercepts exceptions thrown across the whole application and provides
 * appropriate HTTP responses using @ControllerAdvice.
 *
 * This class ensures consistent structure for error responses by wrapping
 * exceptions into a common {@link ErrorResponseDto} format.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles multiple types of exceptions that should result in HTTP 400 Bad Request.
     *
     * Supported exceptions:
     * <ul>
     *     <li>{@link UserAlreadyExistsException}</li>
     *     <li>{@link RequestProcessingException}</li>
     *     <li>{@link UsernameNotFoundException}</li>
     *     <li>{@link BadCredentialsException}</li>
     * </ul>
     *
     * @param exception   the exception thrown
     * @param webRequest  request metadata
     * @return a ResponseEntity containing a structured error response
     */
    @ExceptionHandler({
            UserAlreadyExistsException.class,
            RequestProcessingException.class,
            UsernameNotFoundException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<ErrorResponseDto> handleBadRequestExceptions(
            RuntimeException exception, WebRequest webRequest
    ) {
        return buildErrorResponse(exception, webRequest, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link ResourceNotFoundException} which results in HTTP 404 Not Found.
     *
     * @param exception   the exception thrown
     * @param webRequest  request metadata
     * @return a ResponseEntity containing a structured error response
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(
            ResourceNotFoundException exception, WebRequest webRequest
    ) {
        return buildErrorResponse(exception, webRequest, HttpStatus.NOT_FOUND);
    }

    /**
     * Helper method to build a standardized error response from any exception.
     *
     * @param exception   the exception to convert
     * @param webRequest  request metadata
     * @param status      HTTP status to return
     * @return a ResponseEntity containing the {@link ErrorResponseDto}
     */
    private ResponseEntity<ErrorResponseDto> buildErrorResponse(
            Exception exception, WebRequest webRequest, HttpStatus status
    ) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false),
                status,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDto, status);
    }
}
