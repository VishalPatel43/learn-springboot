package com.springboot.springbootwebtutorial.advices;


import com.springboot.springbootwebtutorial.exceptions.ResourceNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

// Global Exception Handler

//@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFound(ResourceNotFoundException exception,
                                                                 WebRequest request) {
        String path = request
                .getDescription(false)
                .replace("uri=", "");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String trace = sw.toString();

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.NOT_FOUND)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .trace(trace)
                .build();

        return buildErrorResponseEntity(apiError, path);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleInputValidationErrors(MethodArgumentNotValidException exception,
                                                                      WebRequest request) {
        // exception.getBindingResult() --> we get all the exception and bind them together

        String path = request
                .getDescription(false)
                .replace("uri=", "");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String trace = sw.toString();

        List<ObjectError> errorList = exception.getBindingResult().getAllErrors();
        List<String> errors = errorList
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        ApiError apiError = ApiError
                .builder()
                .status(HttpStatus.BAD_REQUEST)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Input validation failed")
                .subErrors(errors)
                .trace(trace)
                .build();

        return buildErrorResponseEntity(apiError, path);
    }

    // Handle All Exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleInternalServerError(Exception exception,
                                                                    WebRequest request) {
        String path = request
                .getDescription(false)
                .replace("uri=", "");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String trace = sw.toString();

        ApiError apiError = ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(exception.getMessage())
                .trace(trace)
                .build();

        return buildErrorResponseEntity(apiError, path);
    }

    private ResponseEntity<ApiResponse<?>> buildErrorResponseEntity(ApiError apiError, String path) {
        ApiResponse<?> response = new ApiResponse<>(apiError);
        response.setPath(path);
        return new ResponseEntity<>(response, apiError.getStatus());
    }
}
