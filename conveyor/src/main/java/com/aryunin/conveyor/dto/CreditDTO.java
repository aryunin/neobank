package com.aryunin.conveyor.dto;

import com.aryunin.conveyor.util.DecimalSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CreditDTO {
    @JsonSerialize(using = DecimalSerializer.class)
    private BigDecimal amount;
    private Integer term;
    @JsonSerialize(using = DecimalSerializer.class)
    private BigDecimal monthlyPayment;
    @JsonSerialize(using = DecimalSerializer.class)
    private BigDecimal rate;
    @JsonSerialize(using = DecimalSerializer.class)
    private BigDecimal psk;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
    private List<PaymentScheduleElement> paymentSchedule;
}
