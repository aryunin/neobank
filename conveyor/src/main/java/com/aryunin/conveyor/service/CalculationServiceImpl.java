package com.aryunin.conveyor.service;

import com.aryunin.conveyor.dto.CreditDTO;
import com.aryunin.conveyor.dto.ScoringDataDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CalculationServiceImpl implements CalculationService{
    private final CreditService creditService;
    private final ScoringService scoringService;

    @Override
    public CreditDTO getCredit(ScoringDataDTO data) {
        var rate = creditService.getRate(data.getIsInsuranceEnabled(), data.getIsSalaryClient());
        rate = scoringService.scoreRate(rate, data);

        var totalAmount = creditService.getTotalAmount(data.getAmount(), data.getIsInsuranceEnabled());
        var monthlyPayment = creditService.getMonthlyPayment(totalAmount, rate, data.getTerm());
        var paymentSchedule = creditService.getPaymentSchedule(
                LocalDate.now(),
                totalAmount,
                rate,
                data.getTerm()
        );
        var psk = creditService.getPSK(totalAmount, monthlyPayment, data.getTerm());

        return CreditDTO.builder()
                .amount(totalAmount)
                .term(data.getTerm())
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .psk(psk)
                .isInsuranceEnabled(data.getIsInsuranceEnabled())
                .isSalaryClient(data.getIsSalaryClient())
                .paymentSchedule(paymentSchedule)
                .build();
    }
}
