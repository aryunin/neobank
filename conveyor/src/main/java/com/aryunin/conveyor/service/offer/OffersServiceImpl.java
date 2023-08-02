package com.aryunin.conveyor.service.offer;

import com.aryunin.conveyor.dto.LoanApplicationRequestDTO;
import com.aryunin.conveyor.dto.LoanOfferDTO;
import com.aryunin.conveyor.service.ScoringService;
import com.aryunin.conveyor.util.CreditUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OffersServiceImpl implements OffersService{
    private final ScoringService scoringService;
    private final CreditUtils creditUtils;

    @Override
    public List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO request) {
        log.info("creating offers");
        List<LoanOfferDTO> result = Arrays.asList(
                createOffer(false, false, request),
                createOffer(false, true, request),
                createOffer(true, false, request),
                createOffer(true, true, request)
        );
        log.info("offers created, sorting");
        result.sort(Comparator.comparing(LoanOfferDTO::getRate));
        log.info("offers sorted");
        return result;
    }

    private LoanOfferDTO createOffer(
            boolean isInsuranceEnabled,
            boolean isSalaryClient,
            LoanApplicationRequestDTO request) {
        log.info("creating offer with "
                + (isInsuranceEnabled ? "insurance enabled, " : "insurance disabled, ")
                + (isSalaryClient ? "salary client" : "no salary client")
        );

        BigDecimal rate = scoringService.getRate(isInsuranceEnabled, isSalaryClient);
        log.info("rate: " + rate);
        BigDecimal totalAmount = creditUtils.getTotalAmount(request.getAmount(), isInsuranceEnabled);
        log.info("total amount: " + totalAmount);
        BigDecimal monthlyPayment = creditUtils.getMonthlyPayment(totalAmount, rate, request.getTerm());
        log.info("monthly payment" + monthlyPayment);
        long appId = 0L;
        log.info("application id" + appId);

        return LoanOfferDTO.builder()
                .applicationId(appId)
                .requestedAmount(request.getAmount())
                .totalAmount(totalAmount)
                .term(request.getTerm())
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryEnabled(isSalaryClient)
                .build();
    }
}
