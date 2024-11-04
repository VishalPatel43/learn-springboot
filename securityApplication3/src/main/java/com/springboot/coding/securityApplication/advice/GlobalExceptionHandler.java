package com.springboot.coding.securityApplication.advice;

import com.springboot.coding.securityApplication.exceptions.ResourceNotFoundException;
import io.jsonwebtoken.JwtException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
//                "Requested resource not found.",
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
//                "Authentication failed. Please check your credentials.",
                request,
                null
        );
    }

    /*
        Note on Filter Context Exceptions:
        Exceptions thrown within filters (e.g., JwtAuthFilter) are outside the DispatcherServlet context.
        They require the use of HandlerExceptionResolver to relay them to this handler.
        JwtAuthFilter processes authentication before the DispatcherServlet, making this approach necessary.
    */
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse<?>> handleRuntimeException(JwtException exception,
                                                                 WebRequest request) {
        return buildErrorResponseEntity(exception,
                HttpStatus.UNAUTHORIZED,
                exception.getLocalizedMessage(),
//                "Invalid or expired JWT token.",
                request,
                null
        );
    }

    // Handler for AccessDeniedException to catch 403 errors
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(AccessDeniedException exception,
                                                                      WebRequest request) {
        return buildErrorResponseEntity(exception,
                HttpStatus.FORBIDDEN,
                "Access is denied. You do not have permission to access this resource.",
//                exception.getLocalizedMessage(),
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
//                exception.getLocalizedMessage(),
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
//                "An unexpected error occurred. Please try again later.",
                request,
                null
        );
    }

    private ResponseEntity<ApiResponse<?>> buildErrorResponseEntity(
            Exception exception,
            HttpStatus status,
            String message,
            WebRequest request,
            List<String> subErrors
    ) {
        String path = request
                .getDescription(false)
                .replace("uri=", "");

        StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));
//        String trace = sw.toString();

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
