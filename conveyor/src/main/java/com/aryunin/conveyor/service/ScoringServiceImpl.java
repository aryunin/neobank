package com.aryunin.conveyor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

//TODO логирование
@Service
public class ScoringServiceImpl implements ScoringService {
    @Value("${credit.base-rate}")
    private BigDecimal baseRate;
    @Value("${credit.insurance-rate}")
    private BigDecimal insuranceRate;
    private final int scale = 16; // минимальная точность промежуточных вычислений

    @Override
    public BigDecimal getRate(boolean isInsuranceEnabled, boolean isSalaryClient) {
        var result = baseRate;
        if(isInsuranceEnabled) result = result.subtract(new BigDecimal("3.0"));
        if(isSalaryClient) result = result.subtract(new BigDecimal("1.0"));
        return result;
    }

    @Override
    public BigDecimal getTotalAmount(BigDecimal amount, boolean isInsuranceEnabled) {
        amount = amount.setScale(scale, RoundingMode.HALF_UP);
        var insuranceAmount = getInsuranceAmount(amount, insuranceRate);
        return (isInsuranceEnabled) ? amount.add(insuranceAmount) : amount;
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
    public BigDecimal getInsuranceAmount(BigDecimal baseAmount, BigDecimal insuranceRate) {
        return baseAmount
                .multiply(insuranceRate)
                .divide(new BigDecimal("100"), scale, RoundingMode.HALF_UP);
    }
}
