package uk.jinhy.server.api.vaccination.presentation.exception;

import org.springframework.http.HttpStatus;

public class VaccinationNotFoundException extends RuntimeException {
    public VaccinationNotFoundException(String message) {
        super(message);
    }
    private final HttpStatus status = HttpStatus.NOT_FOUND; // 선택 사항: 예외 자체에 상태 코드 포함

    public VaccinationNotFoundException(Long vaccinationId) {
        super("Vaccination not found with id: " + vaccinationId);
    }
    public HttpStatus getStatus() {
        return status;
    }
}
