package ru.transfermoney.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jdk.jshell.spi.ExecutionControl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleCardDataException(CardDataException e) {
        log.warn("CardDataException: {}", e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlePasswordException(PasswordException e) {
        log.warn("PasswordException: {}", e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Внутренняя ошибка сервера: {}", e.getMessage());
        return new ResponseEntity<>(new ErrorResponse("Ошибка при проведении транзакции"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
