package com.aryunin.conveyor.service;

import com.aryunin.conveyor.dto.CreditDTO;
import com.aryunin.conveyor.dto.ScoringDataDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CalculationService {
    private final ScoringService scoringService;
    @Value("${credit.min-rate}")
    private BigDecimal minRate;

    public CreditDTO getCredit(ScoringDataDTO request) {
        return createCredit(request);
    }

    private CreditDTO createCredit(ScoringDataDTO data) {
        BigDecimal rate = calculateRate(data);
        BigDecimal totalAmount = scoringService.getTotalAmount(data.getAmount(), data.getIsInsuranceEnabled());
        BigDecimal monthlyPayment = scoringService.getMonthlyPayment(totalAmount, rate, data.getTerm());

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

    private BigDecimal calculateRate(ScoringDataDTO data) {
        BigDecimal rate = scoringService.getRate();
        rate = scoringService.modifyRate(rate, data.getIsInsuranceEnabled(), data.getIsSalaryClient());
        rate = scoringService.modifyRate(rate, data.getAmount(), data.getEmployment());
        rate = scoringService.modifyRate(rate, data.getMaterialStatus());
        rate = scoringService.modifyRate(rate, data.getDependentAmount());
        rate = scoringService.modifyRate(rate, data.getGender(), data.getBirthDate());

        return (rate.compareTo(minRate) < 0) ? minRate : rate;
    }
}
