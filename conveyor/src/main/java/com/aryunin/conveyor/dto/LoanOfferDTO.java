package com.aryunin.conveyor.dto;

import com.aryunin.conveyor.util.DecimalSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LoanOfferDTO {
    private Long applicationId;
    @JsonSerialize(using = DecimalSerializer.class)
    private BigDecimal requestedAmount;
    @JsonSerialize(using = DecimalSerializer.class)
    private BigDecimal totalAmount;
    private Integer term;
    @JsonSerialize(using = DecimalSerializer.class)
    private BigDecimal monthlyPayment;
    @JsonSerialize(using = DecimalSerializer.class)
    private BigDecimal rate;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryEnabled;
}
