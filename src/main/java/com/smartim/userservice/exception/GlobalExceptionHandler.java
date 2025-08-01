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
 * GlobalExceptionHandler for handling all the exception globally
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles UserAlreadyExistsException
     *
     * @param exception contains UserAlreadyExistsException information
     * @param webRequest contains request related information
     * @return a response entity containing error details
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleUserAlreadyExistsException(
            UserAlreadyExistsException exception, WebRequest webRequest
    ){
        ErrorResponseDto errorResponseDto =  new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles UserAlreadyExistsException
     *
     * @param exception contains ResourceNotFoundException information
     * @param webRequest contains request related information
     * @return a response entity containing error details
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(
            ResourceNotFoundException exception, WebRequest webRequest
    ){
        ErrorResponseDto errorResponseDto =  new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles JsonProcessingException
     *
     * @param exception contains JsonProcessingException information
     * @param webRequest contains request related information
     * @return a response entity containing error details
     */
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ErrorResponseDto> handleJsonProcessingException(
            UserAlreadyExistsException exception, WebRequest webRequest
    ){
        ErrorResponseDto errorResponseDto =  new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles RequestProcessingException
     *
     * @param exception contains RequestProcessingException information
     * @param webRequest contains request related information
     * @return a response entity containing error details
     */
    @ExceptionHandler(RequestProcessingException.class)
    public ResponseEntity<ErrorResponseDto> handleRequestProcessingException(
            RequestProcessingException exception, WebRequest webRequest
    ){
        ErrorResponseDto errorResponseDto =  new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles UsernameNotFoundException
     *
     * @param exception contains UsernameNotFoundException information
     * @param webRequest contains request related information
     * @return a response entity containing error details
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUsernameNotFoundException(
            UsernameNotFoundException exception, WebRequest webRequest
    ){
        ErrorResponseDto errorResponseDto =  new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles BadCredentialsException
     *
     * @param exception contains BadCredentialsException information
     * @param webRequest contains request related information
     * @return a response entity containing error details
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handleBadCredentialsException(
            BadCredentialsException exception, WebRequest webRequest
    ){
        ErrorResponseDto errorResponseDto =  new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }
}
