package com.pring.mschargecredit.entity;

import java.time.LocalDateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DebitCardTransaction {
    private String id;

    private Credit credit;
    
    private DebitCard debitCard;

    private String transactionCode;

    private Double transactionAmount;
    
    private TypeTransactionDebitCard typeTransactionDebitCard;

    private LocalDateTime transactionDateTime;
}
