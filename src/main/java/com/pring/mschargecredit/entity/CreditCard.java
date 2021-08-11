package com.pring.mschargecredit.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CreditCard {

    private String id;

    private String cardNumber;

    private Customer customer;

    private Double limitCredit;

    private LocalDate expirationDate;

    private LocalDateTime date;
}
