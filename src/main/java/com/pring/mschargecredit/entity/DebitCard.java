package com.pring.mschargecredit.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DebitCard implements Card{

    private String id;

    private String cardNumber;
    
    private Customer customer;
    
    private List<Accounts> accounts;

    private LocalDate expirationDate;

    private LocalDateTime date;
    
}
