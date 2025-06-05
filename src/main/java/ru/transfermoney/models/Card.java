package ru.transfermoney.models;

import lombok.Data;

@Data
public class Card {
    private String number;
    private String validTill;
    private String cvv;
    private Integer balance;
}
