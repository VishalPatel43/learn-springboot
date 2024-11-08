package com.springboot.coding.testingApp.advice;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Objects;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // This will exclude null fields from the response
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    private HttpStatus status;
    private Integer statusCode;
    private String message;
    private List<String> subErrors;
    private String trace;

    public ApiError(HttpStatus status, String message) {
        this.status = status;
        this.statusCode = status.value();
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiError apiError)) return false;
        return status == apiError.status &&
                Objects.equals(statusCode, apiError.statusCode) &&
                Objects.equals(message, apiError.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, statusCode, message);
    }
}
