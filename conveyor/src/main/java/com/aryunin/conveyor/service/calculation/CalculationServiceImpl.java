package com.aryunin.conveyor.service.calculation;

import com.aryunin.conveyor.dto.CreditDTO;
import com.aryunin.conveyor.dto.ScoringDataDTO;
import com.aryunin.conveyor.service.ScoringService;
import com.aryunin.conveyor.util.CreditUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalculationServiceImpl implements CalculationService{
    private final ScoringService scoringService;
    private final CreditUtils creditUtils;

    @Override
    public CreditDTO getCredit(ScoringDataDTO data) {
        log.info("creating credit");
        var credit = createCredit(data);
        log.info("credit created");
        return credit;
    }

    private CreditDTO createCredit(ScoringDataDTO data) {
        log.info("scoring, calculating rate");
        var rate = scoringService.getRate(data);
        log.info("scoring successful, rate: " + rate);

        var totalAmount = creditUtils.getTotalAmount(data.getAmount(), data.getIsInsuranceEnabled());
        log.info("total amount: " + totalAmount);
        var monthlyPayment = creditUtils.getMonthlyPayment(totalAmount, rate, data.getTerm());
        log.info("monthly payment: " + monthlyPayment);
        var paymentSchedule = creditUtils.getPaymentSchedule(
                LocalDate.now(),
                totalAmount,
                rate,
                data.getTerm()
        );
        log.info("payment schedule: created with size " + paymentSchedule.size());
        var psk = creditUtils.getPSK(totalAmount, monthlyPayment, data.getTerm());
        log.info("psk: " + psk);

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
