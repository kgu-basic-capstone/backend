package uk.jinhy.server.api.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uk.jinhy.server.api.vaccination.presentation.exception.VaccinationNotFoundException;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.of("서버 오류가 발생했습니다.", "UNKNOWN_500"));
    }

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ErrorResponse> handleHttpException(HttpException ex) {
        return ResponseEntity.status(ex.getStatus())
            .body(ErrorResponse.of(ex.getMessage(), ex.getCode()));
    }

    @ExceptionHandler(VaccinationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleVaccinationNotFoundException(VaccinationNotFoundException ex) {
        ErrorResponse errorResponse = ErrorResponse.of(
            ex.getMessage(),
            Integer.toString(HttpStatus.NOT_FOUND.value())
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
