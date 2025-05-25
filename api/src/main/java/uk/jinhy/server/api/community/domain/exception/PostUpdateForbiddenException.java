package uk.jinhy.server.api.community.domain.exception;

import uk.jinhy.server.api.common.exception.HttpException;

public class PostUpdateForbiddenException extends HttpException.ForbiddenException {
    public PostUpdateForbiddenException(String message) {
        super("community", (short) 6, message);
    }
}
