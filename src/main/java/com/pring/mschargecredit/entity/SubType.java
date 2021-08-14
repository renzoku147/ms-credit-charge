package com.pring.mschargecredit.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;

@Data
@Builder
@Document("SubType")
@AllArgsConstructor
@NoArgsConstructor
public class SubType {
    @Id
    String id;

    @Valid
    EnumSubType value;

    enum EnumSubType{
        NORMAL, VIP, PYME
    }
}
