package ru.transfermoney.exceptions;

import lombok.Getter;

@Getter
public class CardDataException extends RuntimeException {
    private String message;

    public CardDataException(String message) {
        super(message);
        this.message = message;
    }
}
