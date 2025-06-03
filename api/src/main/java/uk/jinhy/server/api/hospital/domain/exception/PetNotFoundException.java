package uk.jinhy.server.api.hospital.domain.exception;

import uk.jinhy.server.api.common.exception.HttpException;

public class PetNotFoundException extends HttpException.NotFoundException {
    public PetNotFoundException(String message) {
        super("hospital", (short) 2, message);;
    }
}
