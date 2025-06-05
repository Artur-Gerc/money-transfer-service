package ru.transfermoney.exceptions;

import lombok.Getter;

@Getter
public class OperationNotFoundException extends RuntimeException {
    private String message;

    public OperationNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    public OperationNotFoundException(Throwable cause, String message) {
        super(cause);
        this.message = message;
    }

    public OperationNotFoundException(Throwable cause) {
        super(cause);
    }
}
