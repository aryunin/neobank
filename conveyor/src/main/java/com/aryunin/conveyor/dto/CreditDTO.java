package com.aryunin.conveyor.dto;

import com.aryunin.conveyor.util.MoneySerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CreditDTO {
    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal amount;
    private Integer term;
    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    @JsonSerialize(using = MoneySerializer.class)
    private BigDecimal psk;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
    private List<PaymentScheduleElement> paymentSchedule;
}
