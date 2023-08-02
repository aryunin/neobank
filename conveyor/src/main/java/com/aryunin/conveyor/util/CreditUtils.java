package com.aryunin.conveyor.util;

import com.aryunin.conveyor.dto.PaymentScheduleElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class CreditUtils {
    private final BigDecimal insuranceRate;
    private final int decimalScale;

    public CreditUtils(
            @Value("${credit.insurance-rate}") BigDecimal insuranceRate,
            @Value("${decimal-scaling.calculation}") int scale
    ) {
        this.insuranceRate = insuranceRate;
        this.decimalScale = scale;
    }

    public BigDecimal getMonthlyPayment(BigDecimal amount, BigDecimal rate, Integer term) {
        var rpm = getNormalRate(rate);
        var x = amount.multiply(rpm);
        var y = new BigDecimal(1).add(rpm).pow(term);
        y = new BigDecimal(1).divide(y, decimalScale, RoundingMode.HALF_UP);
        y = new BigDecimal(1).subtract(y);
        return x.divide(y, decimalScale, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotalAmount(BigDecimal amount, boolean isInsuranceEnabled) {
        return (isInsuranceEnabled) ? amount.add(getInsuranceAmount(amount)) : amount;
    }

    public BigDecimal getPSK(BigDecimal amount, BigDecimal monthlyPayment, Integer term) {
        var fullPayment = monthlyPayment.multiply(new BigDecimal(term));
        var diff = fullPayment.subtract(amount);
        return diff.divide(amount, decimalScale, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
    }

    public List<PaymentScheduleElement> getPaymentSchedule(
            LocalDate startDate,
            BigDecimal amount,
            BigDecimal rate,
            Integer term) {
        var result = new ArrayList<PaymentScheduleElement>();
        var remainingDebt = amount;
        var currentDate = startDate;
        var monthlyPayment = getMonthlyPayment(amount, rate, term);
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
        return percentRate.divide(new BigDecimal("1200"), decimalScale, RoundingMode.HALF_UP);
    }

    private BigDecimal getInsuranceAmount(BigDecimal amount) {
        return amount
                .multiply(insuranceRate)
                .divide(new BigDecimal("100"), decimalScale, RoundingMode.HALF_UP);
    }
}
