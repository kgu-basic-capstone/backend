package uk.jinhy.server.api.common.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private boolean error = false;
    private String message;
    private String code;

    public static ErrorResponse of(String message, String code) {
        ErrorResponse response = new ErrorResponse();
        response.message = message;
        response.code = code;
        return response;
    }
}
