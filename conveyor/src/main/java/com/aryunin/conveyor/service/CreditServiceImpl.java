package com.aryunin.conveyor.service;

import com.aryunin.conveyor.dto.PaymentScheduleElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        var rpm = getNormalRate(rate);
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

    @Override
    public List<PaymentScheduleElement> getPaymentSchedule(
            LocalDate startDate,
            BigDecimal totalAmount,
            BigDecimal rate,
            Integer term) {
        var result = new ArrayList<PaymentScheduleElement>();
        var remainingDebt = totalAmount;
        var currentDate = startDate;
        var monthlyPayment = getMonthlyPayment(totalAmount, rate, term);
        var normalRate = getNormalRate(rate);

        for(var i = 0; i < term; i++) {
            var interestPayment = remainingDebt.multiply(normalRate);
            var debtPayment = monthlyPayment.subtract(interestPayment);
            remainingDebt = remainingDebt.subtract(debtPayment);

            result.add(
                    PaymentScheduleElement.builder()
                    .number(i + 1)
                    .date(currentDate)
                    .totalPayment(monthlyPayment)
                    .debtPayment(debtPayment)
                    .interestPayment(interestPayment)
                    .remainingDebt(remainingDebt)
                    .build()
            );

            currentDate = currentDate.plusMonths(1L);
        }

        return result;
    }

    private BigDecimal getNormalRate(BigDecimal percentRate) {
        return percentRate.divide(new BigDecimal("1200"), scale, RoundingMode.HALF_UP);
    }
}
