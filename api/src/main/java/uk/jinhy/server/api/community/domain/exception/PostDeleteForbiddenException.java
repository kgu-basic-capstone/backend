package uk.jinhy.server.api.community.domain.exception;

import uk.jinhy.server.api.common.exception.HttpException;

public class PostDeleteForbiddenException extends HttpException.ForbiddenException {
    public PostDeleteForbiddenException(String message) {
        super("community", (short) 4, message);
    }
}
