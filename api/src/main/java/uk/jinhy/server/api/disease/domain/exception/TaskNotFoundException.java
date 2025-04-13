package uk.jinhy.server.api.disease.domain.exception;

import uk.jinhy.server.api.common.exception.HttpException;

public class TaskNotFoundException extends HttpException.NotFoundException {
    public TaskNotFoundException(String message) {
        super("disease", (short) 1, message);
    }
}
