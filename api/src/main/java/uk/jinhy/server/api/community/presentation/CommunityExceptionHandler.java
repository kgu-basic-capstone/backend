package uk.jinhy.server.api.community.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uk.jinhy.server.api.common.exception.ErrorResponse;
import uk.jinhy.server.api.common.exception.HttpException;
import uk.jinhy.server.api.community.domain.CommentNotFoundException;
import uk.jinhy.server.api.community.domain.PostNotFoundException;

@RestControllerAdvice
public class CommunityExceptionHandler {
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleHttpException(PostNotFoundException ex) {
        throw HttpException.notFound("community", (short) 0, "글을 찾을 수 없습니다.");
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleHttpException(CommentNotFoundException ex) {
        throw HttpException.notFound("community", (short) 1, "댓글을 찾을 수 없습니다.");
    }
}
