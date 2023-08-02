package com.aryunin.conveyor.service.calculation;

import com.aryunin.conveyor.dto.PaymentScheduleElement;
import com.aryunin.conveyor.dto.ScoringDataDTO;
import com.aryunin.conveyor.service.ScoringService;
import com.aryunin.conveyor.util.CreditUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalculationServiceImplTest {
    @Mock
    private ScoringService scoringService;
    @Mock
    private CreditUtils creditUtils;
    @InjectMocks
    private CalculationServiceImpl calculationService;

    @Test
    void getCredit_success() {
        ScoringDataDTO data = ScoringDataDTO.builder()
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .build();

        var rate = new BigDecimal("7.50");
        var totalAmount = new BigDecimal("106000.00");
        var monthlyPayment = new BigDecimal("9196.29");
        var paymentSchedule = List.of(PaymentScheduleElement.builder().date(LocalDate.now()).build());
        var psk = new BigDecimal("4.11");

        when(scoringService.getRate(any())).thenReturn(rate);
        when(creditUtils.getTotalAmount(any(), anyBoolean())).thenReturn(totalAmount);
        when(creditUtils.getMonthlyPayment(any(), any(), any())).thenReturn(monthlyPayment);
        when(creditUtils.getPaymentSchedule(any(), any(), any(), any())).thenReturn(paymentSchedule);
        when(creditUtils.getPSK(any(), any(), any())).thenReturn(psk);

        var creditDTO = calculationService.getCredit(data);

        verify(scoringService, times(1)).getRate(any());
        verify(creditUtils, times(1)).getTotalAmount(any(), anyBoolean());
        verify(creditUtils, times(1)).getMonthlyPayment(any(), any(), any());
        verify(creditUtils, times(1)).getPaymentSchedule(any(), any(), any(), any());
        verify(creditUtils, times(1)).getPSK(any(), any(), any());

        assertEquals(rate, creditDTO.getRate());
        assertEquals(totalAmount, creditDTO.getAmount());
        assertEquals(monthlyPayment, creditDTO.getMonthlyPayment());
        assertEquals(rate, creditDTO.getRate());
        assertEquals(psk, creditDTO.getPsk());
        assertEquals(paymentSchedule, creditDTO.getPaymentSchedule());
    }
}