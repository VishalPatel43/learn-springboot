package com.springboot.coding.testingApp.advice;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // This will exclude null fields from the response
@AllArgsConstructor
public class ApiResponse<T> {

    // write time in 24-hours format
    @JsonFormat(pattern = "HH:mm:ss dd-MM-yyyy")
    private LocalDateTime timeStamp;
    private String path;

    // either data or error will be null
    private T data;
    private ApiError error;

    public ApiResponse() {
        this.timeStamp = LocalDateTime.now();
    }

    public ApiResponse(T data) {
        this();
        this.data = data;
    }

    public ApiResponse(ApiError error) {
        this();
        this.error = error;
    }
}
