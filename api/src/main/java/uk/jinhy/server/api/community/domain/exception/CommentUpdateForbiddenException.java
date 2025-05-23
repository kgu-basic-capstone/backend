package uk.jinhy.server.api.community.domain.exception;

import uk.jinhy.server.api.common.exception.HttpException;

public class CommentUpdateForbiddenException extends HttpException.ForbiddenException {
    public CommentUpdateForbiddenException(String message)  {
        super("community", (short) 3, message);
    }
}
