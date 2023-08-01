package com.aryunin.conveyor.service;

import com.aryunin.conveyor.dto.CreditDTO;
import com.aryunin.conveyor.dto.ScoringDataDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CalculationServiceImpl implements CalculationService{
    private final CreditService creditService;
    private final ScoringService scoringService;

    @Override
    public CreditDTO getCredit(ScoringDataDTO data) {
        BigDecimal rate = creditService.getRate(data.getIsInsuranceEnabled(), data.getIsSalaryClient());
        rate = scoringService.scoreRate(rate, data);

        BigDecimal totalAmount = creditService.getTotalAmount(data.getAmount(), data.getIsInsuranceEnabled());
        BigDecimal monthlyPayment = creditService.getMonthlyPayment(totalAmount, rate, data.getTerm());

        return CreditDTO.builder()
                .amount(totalAmount)
                .term(data.getTerm())
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .psk(new BigDecimal(1)) // TODO psk
                .isInsuranceEnabled(data.getIsInsuranceEnabled())
                .isSalaryClient(data.getIsSalaryClient())
                .paymentSchedule(Collections.emptyList()) // TODO paymentSchedule
                .build();
    }
}
