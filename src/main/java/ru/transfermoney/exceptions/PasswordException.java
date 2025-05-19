package ru.transfermoney.exceptions;

import lombok.Getter;

@Getter
public class PasswordException extends RuntimeException {
    private String message;
    public PasswordException(String message) {
        super(message);
    }
}
