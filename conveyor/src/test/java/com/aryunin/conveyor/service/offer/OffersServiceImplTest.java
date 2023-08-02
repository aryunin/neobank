package com.aryunin.conveyor.service.offer;

import com.aryunin.conveyor.dto.LoanApplicationRequestDTO;
import com.aryunin.conveyor.service.ScoringService;
import com.aryunin.conveyor.util.CreditUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OffersServiceImplTest {
    @Mock
    private ScoringService scoringService;
    @Mock
    private CreditUtils creditUtils;
    @InjectMocks
    private OffersServiceImpl offersService;

    @Test
    void getOffers_success() {
        var request = LoanApplicationRequestDTO.builder().build();

        var rate = new BigDecimal("10");
        var rateSal = new BigDecimal("9");
        var rateIns = new BigDecimal("7");
        var rateInsSal = new BigDecimal("6");
        var amount = new BigDecimal("100000");
        var amountIns = new BigDecimal("106000");
        var monthlyPayment = new BigDecimal("9000");

        when(scoringService.getRate(anyBoolean(), anyBoolean())).thenReturn(rate, rateSal, rateIns, rateInsSal);
        when(creditUtils.getTotalAmount(any(), anyBoolean())).thenReturn(amount, amount, amountIns, amountIns);
        when(creditUtils.getMonthlyPayment(any(), any(), any())).thenReturn(monthlyPayment);

        var offers = offersService.getOffers(request);

        verify(scoringService, times(4)).getRate(anyBoolean(), anyBoolean());
        verify(creditUtils, times(4)).getTotalAmount(any(), anyBoolean());
        verify(creditUtils, times(4)).getMonthlyPayment(any(), any(), any());

        assertEquals(4, offers.size());

        assertEquals(rateInsSal, offers.get(0).getRate());
        assertEquals(amountIns, offers.get(0).getTotalAmount());
        assertEquals(rateIns, offers.get(1).getRate());
        assertEquals(amountIns, offers.get(1).getTotalAmount());
        assertEquals(rateSal, offers.get(2).getRate());
        assertEquals(amount, offers.get(2).getTotalAmount());
        assertEquals(rate, offers.get(3).getRate());
        assertEquals(amount, offers.get(3).getTotalAmount());
    }
}