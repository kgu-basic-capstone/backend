package uk.jinhy.server.api.common.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {
    private boolean error = false;
    private String message;
    private String code;
    private int status;
    private LocalDateTime timestamp = LocalDateTime.now();


    public static ErrorResponse of(String message, String code) {
        ErrorResponse response = new ErrorResponse();
        response.message = message;
        response.code = code;
        return response;
    }
    public static ErrorResponse of(int status, String message) {
        ErrorResponse response = new ErrorResponse();
        response.status = status;
        response.message = message;
        return response;
    }
    }
