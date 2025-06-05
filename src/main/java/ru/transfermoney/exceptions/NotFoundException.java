package ru.transfermoney.exceptions;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private String message;

    public NotFoundException(String message) {
        super(message);
        this.message = message;
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }
}
