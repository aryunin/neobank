package com.aryunin.conveyor.service;

import com.aryunin.conveyor.dto.PaymentScheduleElement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

// fixme check for division by null
public interface CreditService {
    BigDecimal getRate(boolean isInsuranceEnabled, boolean isSalaryClient);
    BigDecimal getMonthlyPayment(BigDecimal totalAmount, BigDecimal rate, Integer term);
    BigDecimal getInsuranceAmount(BigDecimal amount);
    BigDecimal getTotalAmount(BigDecimal amount, boolean isInsuranceEnabled);
    List<PaymentScheduleElement> getPaymentSchedule(
            LocalDate startDate,
            BigDecimal totalAmount,
            BigDecimal rate,
            Integer term
    );
}
