package com.aryunin.conveyor.service;

import java.math.BigDecimal;

// fixme check for division by null
public interface CreditService {
    BigDecimal getRate(boolean isInsuranceEnabled, boolean isSalaryClient);
    BigDecimal getMonthlyPayment(BigDecimal totalAmount, BigDecimal rate, Integer term);
    BigDecimal getInsuranceAmount(BigDecimal amount);
    BigDecimal getTotalAmount(BigDecimal amount, boolean isInsuranceEnabled);
}
