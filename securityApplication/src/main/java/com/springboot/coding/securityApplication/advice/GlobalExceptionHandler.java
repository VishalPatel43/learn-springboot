package com.springboot.coding.securityApplication.advice;

import com.springboot.coding.securityApplication.exceptions.ResourceNotFoundException;
import io.jsonwebtoken.JwtException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFound(ResourceNotFoundException exception,
                                                                 WebRequest request) {

        return buildErrorResponseEntity(exception,
                HttpStatus.NOT_FOUND,
//                exception.getMessage(),
                exception.getLocalizedMessage(),
                request,
                null
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthenticationException(AuthenticationException exception,
                                                                        WebRequest request) {
        return buildErrorResponseEntity(exception,
                HttpStatus.UNAUTHORIZED,
                exception.getLocalizedMessage(),
                request,
                null
        );
    }

    /*
    When we get exception inside our controller, service
        --> if handling of exception or context of this exception is under the dispatcher servlet then our global exception handler will handle it
        --> if handling of exception or context of this exception is outside the dispatcher servlet then our global exception handler will not handle it
        --> currently we go to the filter (JwtAuthFilter) and take it to another context basically we are going inside filter context, but we are not inside dispatcher servlet yet
        --> After filter (JwtAuthFilter) done its magic our request will go to the dispatcher servlet
        --> Global exception handler handle only dispatcher servlet exceptions
        --> therefore we use the HandlerExceptionResolver inside the filter to handle exceptions
        --> it will pass exception to one context to other context here servlet dispatcher context

     */
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<?>> handleRuntimeException(JwtException exception,
                                                                 WebRequest request) {
        return buildErrorResponseEntity(exception,
                HttpStatus.UNAUTHORIZED,
                exception.getLocalizedMessage(),
                request,
                null
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleInputValidationErrors(MethodArgumentNotValidException exception,
                                                                      WebRequest request) {
        List<String> errors = exception
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        return buildErrorResponseEntity(exception,
                HttpStatus.BAD_REQUEST,
                "Input validation failed",
                request,
                errors
        );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleInternalServerError(Exception exception,
                                                                    WebRequest request) {
        return buildErrorResponseEntity(exception,
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage(),
                request,
                null
        );
    }

    private ResponseEntity<ApiResponse<?>> buildErrorResponseEntity(
            Exception exception,
            HttpStatus status, String message,
            WebRequest request,
            List<String> subErrors
    ) {
        String path = request
                .getDescription(false)
                .replace("uri=", "");

        StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));
        String trace = sw.toString();

        ApiError apiError = ApiError.builder()
                .status(status)
                .statusCode(status.value())
                .message(message)
//                .trace(trace)
                .subErrors(subErrors)
                .build();

        ApiResponse<?> response = new ApiResponse<>(apiError);
        response.setPath(path);
        return new ResponseEntity<>(response, status);
    }
}
