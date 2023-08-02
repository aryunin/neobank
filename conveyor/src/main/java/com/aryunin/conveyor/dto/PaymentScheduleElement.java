package com.aryunin.conveyor.dto;

import com.aryunin.conveyor.util.DecimalSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class PaymentScheduleElement {
    private Integer number;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate date;
    @JsonSerialize(using = DecimalSerializer.class)
    private BigDecimal totalPayment;
    @JsonSerialize(using = DecimalSerializer.class)
    private BigDecimal interestPayment;
    @JsonSerialize(using = DecimalSerializer.class)
    private BigDecimal debtPayment;
    @JsonSerialize(using = DecimalSerializer.class)
    private BigDecimal remainingDebt;
}
