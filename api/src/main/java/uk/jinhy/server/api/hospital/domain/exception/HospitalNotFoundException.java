package uk.jinhy.server.api.hospital.domain.exception;


import uk.jinhy.server.api.common.exception.HttpException;

public class HospitalNotFoundException extends HttpException.NotFoundException {
    public HospitalNotFoundException(String message) {
        super("hospital", (short) 1, message);
    }
}
