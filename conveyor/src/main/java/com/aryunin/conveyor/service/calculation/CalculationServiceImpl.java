package com.aryunin.conveyor.service.calculation;

import com.aryunin.conveyor.dto.CreditDTO;
import com.aryunin.conveyor.dto.ScoringDataDTO;
import com.aryunin.conveyor.service.ScoringService;
import com.aryunin.conveyor.util.CreditUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CalculationServiceImpl implements CalculationService{
    private final ScoringService scoringService;
    private final CreditUtils creditUtils;

    @Override
    public CreditDTO getCredit(ScoringDataDTO data) {
        return createCredit(data);
    }

    private CreditDTO createCredit(ScoringDataDTO data) {
        var rate = scoringService.getRate(data);

        var totalAmount = creditUtils.getTotalAmount(data.getAmount(), data.getIsInsuranceEnabled());
        var monthlyPayment = creditUtils.getMonthlyPayment(totalAmount, rate, data.getTerm());
        var paymentSchedule = creditUtils.getPaymentSchedule(
                LocalDate.now(),
                totalAmount,
                rate,
                data.getTerm()
        );
        var psk = creditUtils.getPSK(totalAmount, monthlyPayment, data.getTerm());

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
