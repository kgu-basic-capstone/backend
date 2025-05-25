package uk.jinhy.server.api.hospital.domain.exception;

import uk.jinhy.server.api.common.exception.HttpException;

public class ReservationNotFoundException extends HttpException.NotFoundException {
    public ReservationNotFoundException(String message) {
        super("hospital", (short) 3, message);
    }
}
