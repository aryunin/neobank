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

    @Override
    public BigDecimal getRate() {
        return baseRate;
    }

    @Override
    public BigDecimal getRate(boolean isInsuranceEnabled, boolean isSalaryClient) {
        var result = baseRate;
        if(isInsuranceEnabled) result = result.subtract(new BigDecimal("3.0"));
        if(isSalaryClient) result = result.subtract(new BigDecimal("1.0"));
        return result;
    }

    @Override
    public BigDecimal getInsuranceRate() {
        return insuranceRate;
    }

    @Override
    public BigDecimal getMonthlyPayment(BigDecimal totalAmount, BigDecimal rate, Integer term) {
        var rpm = rate.divide(new BigDecimal("1200"), rate.scale(), RoundingMode.HALF_UP);
        var x = totalAmount.multiply(rpm);
        var y = new BigDecimal(1).add(rpm).pow(term);
        y = new BigDecimal(1).divide(y, rate.scale(), RoundingMode.HALF_UP);
        y = new BigDecimal(1).subtract(y);
        return x.divide(y, rate.scale(), RoundingMode.HALF_UP).setScale(rate.scale(), RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getInsuranceAmount(BigDecimal amount) {
        return amount
                .multiply(insuranceRate)
                .divide(new BigDecimal("100"), amount.scale(), RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getTotalAmount(BigDecimal amount, boolean isInsuranceEnabled) {
        return (isInsuranceEnabled) ? amount.add(getInsuranceAmount(amount)) : amount;
    }
}
