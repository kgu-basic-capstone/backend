package uk.jinhy.server.api.community.domain.exception;

public class PostDeleteForbiddenException extends RuntimeException {
    public PostDeleteForbiddenException(String message) {
        super(message);
    }
}
