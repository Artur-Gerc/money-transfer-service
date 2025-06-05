package ru.transfermoney.exceptions;

import lombok.Getter;

@Getter
public class CardBalanceException extends RuntimeException {
    private String message;

    public CardBalanceException(String message) {
        super(message);
        this.message = message;
    }

    public CardBalanceException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public CardBalanceException(Throwable cause) {
        super(cause);
    }
}
