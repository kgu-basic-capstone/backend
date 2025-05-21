package uk.jinhy.server.api.community.domain.exception;

import uk.jinhy.server.api.common.exception.HttpException;

public class CommentNotFoundException extends HttpException.NotFoundException {
    public CommentNotFoundException(String message) {
        super("community", (short) 2, message);
    }
}
