package com.project.marketplace.exception;

import com.project.marketplace.payload.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler({Exception.class})
    public ApiResponse handleException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        log.error("Global Exception", e);
        return new ApiResponse(500, "error", null);
    }
}
