package uk.jinhy.server.service.vaccination.presentation;

import lombok.Getter;
import org.springframework.http.HttpStatus;

//예외 처리 개선
@Getter
public class ResourceNotFoundException {
    private final HttpStatus status = HttpStatus.NOT_FOUND;

    public ResourceNotFoundException(String message) {
        super();
    }
}
