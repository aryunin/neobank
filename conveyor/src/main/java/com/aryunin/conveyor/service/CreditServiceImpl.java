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
    private final BigDecimal baseRate;
    private final BigDecimal insuranceRate;
    private final int scale;

    public CreditServiceImpl(
            @Value("${credit.base-rate}") BigDecimal baseRate,
            @Value("${credit.insurance-rate}") BigDecimal insuranceRate,
            @Value("${decimal-scaling.calculation}") int scale) {
        this.baseRate = baseRate;
        this.insuranceRate = insuranceRate;
        this.scale = scale;
    }

    @Override
    public BigDecimal getRate(boolean isInsuranceEnabled, boolean isSalaryClient) {
        BigDecimal result = new BigDecimal(baseRate.toString());
        if(isInsuranceEnabled) result =  result.subtract(new BigDecimal("3.0"));
        if(isSalaryClient) result = result.subtract(new BigDecimal("1.0"));
        return result;
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
    public BigDecimal getPSK(BigDecimal amount, BigDecimal monthlyPayment, Integer term) {
        var fullPayment = monthlyPayment.multiply(new BigDecimal(term));
        var diff = fullPayment.subtract(amount);
        return diff.divide(amount, scale, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
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
