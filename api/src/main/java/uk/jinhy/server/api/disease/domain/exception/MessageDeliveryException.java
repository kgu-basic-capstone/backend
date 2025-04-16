package uk.jinhy.server.api.disease.domain.exception;

public class MessageDeliveryException extends RuntimeException {
    public MessageDeliveryException(String message) {
        super(message);
    }

    public MessageDeliveryException(String message, Throwable cause) {
        super(message, cause);
    }
} 