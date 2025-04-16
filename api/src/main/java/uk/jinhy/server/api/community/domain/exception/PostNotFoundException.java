package uk.jinhy.server.api.community.domain.exception;

import uk.jinhy.server.api.common.exception.HttpException;

public class PostNotFoundException extends HttpException.NotFoundException {
    public PostNotFoundException(String message) {
        super("community", (short) 5, message);
    }
}
