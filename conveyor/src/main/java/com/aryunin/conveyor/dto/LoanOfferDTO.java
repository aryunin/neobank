package com.aryunin.conveyor.dto;

import com.aryunin.conveyor.util.MoneySerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LoanOfferDTO {
    private Long applicationId;
    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal requestedAmount;
    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal totalAmount;
    private Integer term;
    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryEnabled;
}
