package uk.jinhy.server.api.community.domain.exception;

public class CommentUpdateForbiddenException extends RuntimeException {
    public CommentUpdateForbiddenException(String message) {
        super(message);
    }
}
