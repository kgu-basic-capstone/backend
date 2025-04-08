package uk.jinhy.server.api.community.domain.exception;

public class CommentDeleteForbiddenException extends RuntimeException {
    public CommentDeleteForbiddenException(String message) {
        super(message);
    }
}
