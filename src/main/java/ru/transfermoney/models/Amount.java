package ru.transfermoney.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Amount {
    private String currency;
    private Integer value;
}
