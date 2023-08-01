package com.aryunin.conveyor.service;

import java.math.BigDecimal;

public interface ScoringService {
    BigDecimal getRate();
    BigDecimal getRate(boolean isInsuranceEnabled, boolean isSalaryClient);
    BigDecimal getInsuranceRate();
    BigDecimal getMonthlyPayment(BigDecimal totalAmount, BigDecimal rate, Integer term);
    BigDecimal getInsuranceAmount(BigDecimal amount);
    BigDecimal getTotalAmount(BigDecimal amount, boolean isInsuranceEnabled);
}
