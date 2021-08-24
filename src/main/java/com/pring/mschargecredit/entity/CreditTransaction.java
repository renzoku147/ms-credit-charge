package com.pring.mschargecredit.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditTransaction {
    private String id;

    private Credit credit;
    
    private DebitCard debitCard;

    private String transactionCode;

    private Double transactionAmount;

    private LocalDateTime transactionDateTime;
}
