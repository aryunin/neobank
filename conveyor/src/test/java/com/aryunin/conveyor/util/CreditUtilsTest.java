package com.aryunin.conveyor.util;

import com.aryunin.conveyor.dto.PaymentScheduleElement;
import com.aryunin.conveyor.exception.MathException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreditUtilsTest {
    private final CreditUtils creditUtils = new CreditUtils(new BigDecimal(3), 16);

    @Test
    void getMonthlyPayment_success() {
        var amount = new BigDecimal("100000");
        var rate = new BigDecimal("10");
        var term = 16;

        var actual = creditUtils.getMonthlyPayment(amount, rate, term).setScale(2, RoundingMode.HALF_UP);

        assertEquals(new BigDecimal("6701.89"), actual);
    }

    @Test
    void getMonthlyPayment_throws() {
        var amount = new BigDecimal("100000");

        assertThrows(MathException.class,
                () -> creditUtils.getMonthlyPayment(amount, new BigDecimal("0"), 16)
        );
        assertThrows(MathException.class,
                () -> creditUtils.getMonthlyPayment(amount, new BigDecimal("-1"), 16)
        );
        assertThrows(MathException.class,
                () -> creditUtils.getMonthlyPayment(amount, new BigDecimal("-1"), 0)
        );
    }

    @Test
    void getTotalAmount_success() {
        var amount = new BigDecimal("100000");
        var totalAmount = creditUtils.getTotalAmount(amount, false)
                .setScale(0, RoundingMode.HALF_UP);

        assertEquals(amount, totalAmount);

        totalAmount = creditUtils.getTotalAmount(amount, true)
                .setScale(0, RoundingMode.HALF_UP);
        var amountIns = amount.add(amount.multiply(new BigDecimal("0.03")))
                .setScale(0, RoundingMode.HALF_UP);

        assertEquals(amountIns, totalAmount);
    }

    @Test
    void getPSK_success() {
        var amount = new BigDecimal("100000");
        var monthlyPayment = new BigDecimal("6701.89");
        var term = 16;

        var actual = creditUtils.getPSK(amount, monthlyPayment, term)
                        .setScale(2, RoundingMode.HALF_UP);

        assertEquals(new BigDecimal("5.42"), actual);
    }

    @Test
    void getPSK_throws() {
        var monthlyPayment = new BigDecimal("1");

        assertThrows(MathException.class,
                () -> creditUtils.getPSK(new BigDecimal("0"), monthlyPayment, 1)
        );

        assertThrows(MathException.class,
                () -> creditUtils.getPSK(new BigDecimal("1"), monthlyPayment, 0)
        );
    }

    @Test
    void getPaymentSchedule_success() {
        var startDate = LocalDate.now();
        var amount = new BigDecimal("100000");
        var rate = new BigDecimal("10");
        var term = 4;

        List<PaymentScheduleElement> schedule = creditUtils.getPaymentSchedule(
                startDate,
                amount,
                rate,
                term
        );

        assertEquals(term, schedule.size());

        assertEquals(startDate.plusMonths(2), schedule.get(2).getDate());
        assertEquals(3, schedule.get(2).getNumber());
        assertEquals(new BigDecimal("25522.99"),
                schedule.get(2).getTotalPayment().setScale(2, RoundingMode.HALF_UP));
        assertEquals(new BigDecimal("420.12"),
                schedule.get(2).getInterestPayment().setScale(2, RoundingMode.HALF_UP));
        assertEquals(new BigDecimal("25102.87"),
                schedule.get(2).getDebtPayment().setScale(2, RoundingMode.HALF_UP));
        assertEquals(new BigDecimal("25312.06"),
                schedule.get(2).getRemainingDebt().setScale(2, RoundingMode.HALF_UP));
    }
}