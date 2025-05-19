package ru.transfermoney.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
@ToString
public class OperationResponseDto {

    private static final AtomicLong  currentOperationId = new AtomicLong(0L);
    private String operationId;

    public OperationResponseDto() {
        this.operationId = String.valueOf(currentOperationId.incrementAndGet());
    }
}
