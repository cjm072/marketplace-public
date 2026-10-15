package com.project.marketplace.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<T>(HttpStatus.OK.value(), "success", data);
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>(HttpStatus.OK.value(), "success", null);
    }
}
