package uk.jinhy.server.api.hospital.domain.exception;

import uk.jinhy.server.api.common.exception.HttpException;

public class InvalidReservationStatusException extends HttpException.BadRequestException {
    public InvalidReservationStatusException(String message) {
        super("hospital", (short) 4, message);
    }
}
