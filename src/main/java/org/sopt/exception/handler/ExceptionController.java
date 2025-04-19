package org.sopt.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopt.dto.ErrorResponseDto;
import org.sopt.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomException(CustomException exception) {
        HttpStatus httpStatus = resolveHttpStatus(exception);
        writeLog(exception);
        return new ResponseEntity<>(ErrorResponseDto.from(exception), httpStatus);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception exception) {
        writeLog(exception);
        return new ResponseEntity<>(ErrorResponseDto.of(HttpStatus.INTERNAL_SERVER_ERROR.toString(), exception),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private HttpStatus resolveHttpStatus(CustomException customException) {
        return HttpStatus.resolve(Integer.parseInt(customException
                .getErrorCode()
                .getStatusCode()
                .substring(0, 3)));
    }

    private void writeLog(CustomException e) {
        Logger logger = LoggerFactory.getLogger(ExceptionController.class);
        logger.error("[{}]{}: {}",
                e.getClass().getSimpleName(),
                e.getErrorCode().getMessage(),
                e.getDetail());
    }

    private void writeLog(Exception e) {
        Logger logger = LoggerFactory.getLogger(ExceptionController.class);
        logger.error("[{}]: {}",
                e.getClass().getSimpleName(),
                e.getMessage());
    }
}
