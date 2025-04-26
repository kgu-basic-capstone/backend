package uk.jinhy.server.api.community.domain.exception;

import uk.jinhy.server.api.common.exception.HttpException;

public class CommentDeleteForbiddenException extends HttpException.ForbiddenException {
    public CommentDeleteForbiddenException(String message) {
        super("community", (short) 1, message);
    }
}
