package com.smartim.userservice.exception;

import com.smartim.userservice.dto.ErrorResponseDto;
import com.smartim.userservice.security.JwtAuthFilter;
import com.smartim.userservice.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@WebMvcTest(GlobalExceptionHandlerTest.TestController.class)
@Import(GlobalExceptionHandler.class)
@WithMockUser(username = "testuser")
class GlobalExceptionHandlerTest {

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsService userDetailsService; // Mock UserDetailsService to avoid security config issues

    @RestController
    public static class TestController {
        @GetMapping("/user-already-exists")
        public void throwUserAlreadyExists() {
            throw new UserAlreadyExistsException("User already exists");
        }

        @GetMapping("/resource-not-found")
        public void throwResourceNotFound() {
            throw new ResourceNotFoundException("Resource", "id", "1");
        }

        @GetMapping("/json-processing")
        public void throwJsonProcessing() throws JsonProcessingException {
            throw new JsonProcessingException("JSON error") {};
        }

        @GetMapping("/request-processing")
        public void throwRequestProcessing() {
            throw new RequestProcessingException("Request processing error");
        }

        @GetMapping("/username-not-found")
        public void throwUsernameNotFound() {
            throw new UsernameNotFoundException("Username not found");
        }

        @GetMapping("/bad-credentials")
        public void throwBadCredentials() {
            throw new BadCredentialsException("Bad credentials");
        }
    }

    @Test
    void handleUserAlreadyExistsException() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        UserAlreadyExistsException ex =
                new UserAlreadyExistsException("User already exists");

        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false))
                .thenReturn("uri=/user-already-exists");

        ResponseEntity<ErrorResponseDto> response =
                handler.handleUserAlreadyExistsException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User already exists",
                response.getBody().getErrorMessage());
    }

    @Test
    void handleResourceNotFoundException() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResourceNotFoundException ex =
                new ResourceNotFoundException("Resource", "id", "1");

        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false))
                .thenReturn("uri=/resource-not-found");

        ResponseEntity<ErrorResponseDto> response =
                handler.handleResourceNotFoundException(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Resource not found with the given input data id: '1'",
                response.getBody().getErrorMessage());
    }

    @Test
    void handleJsonProcessingException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        JsonProcessingException exception =
                new JsonProcessingException("JSON error") {};

        WebRequest webRequest = mock(WebRequest.class);
        when(webRequest.getDescription(false))
                .thenReturn("uri=/json-processing");

        ResponseEntity<ErrorResponseDto> response =
                handler.handleJsonProcessingException(exception, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals("JSON error", body.getErrorMessage());
        assertEquals(HttpStatus.BAD_REQUEST, body.getErrorCode());
    }

    @Test
    void handleRequestProcessingException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        RequestProcessingException exception =
                new RequestProcessingException("Request processing error") {};

        WebRequest webRequest = mock(WebRequest.class);
        when(webRequest.getDescription(false))
                .thenReturn("uri=/request-processing");

        ResponseEntity<ErrorResponseDto> response =
                handler.handleRequestProcessingException(exception, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Request processing error", body.getErrorMessage());
        assertEquals(HttpStatus.BAD_REQUEST, body.getErrorCode());
    }

    @Test
    void handleUsernameNotFoundException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        UsernameNotFoundException exception =
                new UsernameNotFoundException("Username not found") {};

        WebRequest webRequest = mock(WebRequest.class);
        when(webRequest.getDescription(false))
                .thenReturn("uri=/username-not-found");

        ResponseEntity<ErrorResponseDto> response =
                handler.handleUsernameNotFoundException(exception, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Username not found", body.getErrorMessage());
        assertEquals(HttpStatus.BAD_REQUEST, body.getErrorCode());
    }

    @Test
    void handleBadCredentialsException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        UsernameNotFoundException exception =
                new UsernameNotFoundException("Bad credentials") {};

        WebRequest webRequest = mock(WebRequest.class);
        when(webRequest.getDescription(false))
                .thenReturn("uri=/bad-credentials");

        ResponseEntity<ErrorResponseDto> response =
                handler.handleUsernameNotFoundException(exception, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorResponseDto body = response.getBody();
        assertNotNull(body);
        assertEquals("Bad credentials", body.getErrorMessage());
        assertEquals(HttpStatus.BAD_REQUEST, body.getErrorCode());
    }
}
