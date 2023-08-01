package com.aryunin.conveyor.service;

import com.aryunin.conveyor.dto.PaymentScheduleElement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

// fixme check for division by null
public interface CreditService {
    BigDecimal getRate(boolean isInsuranceEnabled, boolean isSalaryClient);
    BigDecimal getMonthlyPayment(BigDecimal amount, BigDecimal rate, Integer term);
    BigDecimal getTotalAmount(BigDecimal amount, boolean isInsuranceEnabled);
    BigDecimal getPSK(BigDecimal amount, BigDecimal monthlyPayment, Integer term);
    List<PaymentScheduleElement> getPaymentSchedule(
            LocalDate startDate,
            BigDecimal amount,
            BigDecimal rate,
            Integer term
    );

}
