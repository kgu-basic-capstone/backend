package uk.jinhy.server.api.community.domain.exception;

public class PostUpdateForbiddenException extends RuntimeException {
    public PostUpdateForbiddenException(String message) {
        super(message);
    }
}
