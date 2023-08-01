package com.aryunin.conveyor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CreditServiceImpl implements CreditService {
    @Value("${credit.base-rate}")
    private BigDecimal baseRate;
    @Value("${credit.insurance-rate}")
    private BigDecimal insuranceRate;
    @Value("${decimal-scaling.calculation}")
    private int scale;

    @Override
    public BigDecimal getRate(boolean isInsuranceEnabled, boolean isSalaryClient) {
        if(isInsuranceEnabled) baseRate = baseRate.subtract(new BigDecimal("3.0"));
        if(isSalaryClient) baseRate = baseRate.subtract(new BigDecimal("1.0"));
        return baseRate;
    }

    @Override
    public BigDecimal getMonthlyPayment(BigDecimal totalAmount, BigDecimal rate, Integer term) {
        var rpm = rate.divide(new BigDecimal("1200"), scale, RoundingMode.HALF_UP);
        var x = totalAmount.multiply(rpm);
        var y = new BigDecimal(1).add(rpm).pow(term);
        y = new BigDecimal(1).divide(y, scale, RoundingMode.HALF_UP);
        y = new BigDecimal(1).subtract(y);
        return x.divide(y, scale, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getInsuranceAmount(BigDecimal amount) {
        return amount
                .multiply(insuranceRate)
                .divide(new BigDecimal("100"), scale, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getTotalAmount(BigDecimal amount, boolean isInsuranceEnabled) {
        return (isInsuranceEnabled) ? amount.add(getInsuranceAmount(amount)) : amount;
    }
}
