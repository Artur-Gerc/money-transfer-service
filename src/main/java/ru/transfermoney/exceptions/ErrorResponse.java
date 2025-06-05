package ru.transfermoney.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ErrorResponse {

    private String message;
    private static Integer currentId = 0;
    private int id;

    public ErrorResponse(String message) {
        this.message = message;
        id = ++currentId;
    }
}
