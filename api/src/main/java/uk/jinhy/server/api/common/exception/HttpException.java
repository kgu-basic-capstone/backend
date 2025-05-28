package uk.jinhy.server.api.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class HttpException extends RuntimeException {
    private final String domainName;
    private final short uniqueErrorNumber;
    private final String message;

    protected HttpException(String domainName, short uniqueErrorNumber, String message) {
        this.domainName = domainName;
        this.uniqueErrorNumber = uniqueErrorNumber;
        this.message = message;
    }

    public String getCode() {
        return domainName.toUpperCase() + '-' + String.format("%05d", uniqueErrorNumber);
    }

    public abstract HttpStatus getStatus();

    public static BadRequestException badRequest(String domainName, short uniqueErrorNumber, String message) {
        return new BadRequestException(domainName, uniqueErrorNumber, message);
    }

    public static UnauthorizedException unauthorized(String domainName, short uniqueErrorNumber, String message) {
        return new UnauthorizedException(domainName, uniqueErrorNumber, message);
    }

    public static ForbiddenException forbidden(String domainName, short uniqueErrorNumber, String message) {
        return new ForbiddenException(domainName, uniqueErrorNumber, message);
    }

    public static NotFoundException notFound(String domainName, short uniqueErrorNumber, String message) {
        return new NotFoundException(domainName, uniqueErrorNumber, message);
    }

    public static ConflictException conflict(String domainName, short uniqueErrorNumber, String message) {
        return new ConflictException(domainName, uniqueErrorNumber, message);
    }

    public static TooManyRequestsException tooManyRequests(String domainName, short uniqueErrorNumber, String message) {
        return new TooManyRequestsException(domainName, uniqueErrorNumber, message);
    }

    public static InternalServerErrorException internalServerError(String domainName, short uniqueErrorNumber, String message) {
        return new InternalServerErrorException(domainName, uniqueErrorNumber, message);
    }

    @Getter
    public static class BadRequestException extends HttpException {
        public BadRequestException(String domainName, short uniqueErrorNumber, String message) {
            super(domainName, uniqueErrorNumber, message);
        }

        @Override
        public HttpStatus getStatus() {
            return HttpStatus.BAD_REQUEST;
        }
    }

    @Getter
    public static class UnauthorizedException extends HttpException {
        public UnauthorizedException(String domainName, short uniqueErrorNumber, String message) {
            super(domainName, uniqueErrorNumber, message);
        }

        @Override
        public HttpStatus getStatus() {
            return HttpStatus.UNAUTHORIZED;
        }
    }

    @Getter
    public static class ForbiddenException extends HttpException {
        public ForbiddenException(String domainName, short uniqueErrorNumber, String message) {
            super(domainName, uniqueErrorNumber, message);
        }

        @Override
        public HttpStatus getStatus() {
            return HttpStatus.FORBIDDEN;
        }
    }

    @Getter
    public static class NotFoundException extends HttpException {
        public NotFoundException(String domainName, short uniqueErrorNumber, String message) {
            super(domainName, uniqueErrorNumber, message);
        }

        @Override
        public HttpStatus getStatus() {
            return HttpStatus.NOT_FOUND;
        }
    }

    @Getter
    public static class ConflictException extends HttpException {
        public ConflictException(String domainName, short uniqueErrorNumber, String message) {
            super(domainName, uniqueErrorNumber, message);
        }

        @Override
        public HttpStatus getStatus() {
            return HttpStatus.CONFLICT;
        }
    }

    @Getter
    public static class TooManyRequestsException extends HttpException {
        public TooManyRequestsException(String domainName, short uniqueErrorNumber, String message) {
            super(domainName, uniqueErrorNumber, message);
        }

        @Override
        public HttpStatus getStatus() {
            return HttpStatus.TOO_MANY_REQUESTS;
        }
    }

    @Getter
    public static class InternalServerErrorException extends HttpException {
        public InternalServerErrorException(String domainName, short uniqueErrorNumber, String message) {
            super(domainName, uniqueErrorNumber, message);
        }

        @Override
        public HttpStatus getStatus() {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
