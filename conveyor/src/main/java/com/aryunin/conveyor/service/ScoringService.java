package com.aryunin.conveyor.service;

import java.math.BigDecimal;

public interface ScoringService {
    BigDecimal getRate(boolean isInsuranceEnabled, boolean isSalaryClient);

    BigDecimal getTotalAmount(BigDecimal amount, boolean isInsuranceEnabled);

    BigDecimal getMonthlyPayment(BigDecimal totalAmount, BigDecimal rate, Integer term);
}
