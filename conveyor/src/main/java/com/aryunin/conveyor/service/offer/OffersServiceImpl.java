package com.aryunin.conveyor.service.offer;

import com.aryunin.conveyor.dto.LoanApplicationRequestDTO;
import com.aryunin.conveyor.dto.LoanOfferDTO;
import com.aryunin.conveyor.service.ScoringService;
import com.aryunin.conveyor.util.CreditUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OffersServiceImpl implements OffersService{
    private final ScoringService scoringService;
    private final CreditUtils creditUtils;

    @Override
    public List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO request) {
        List<LoanOfferDTO> result = Arrays.asList(
                createOffer(false, false, request),
                createOffer(false, true, request),
                createOffer(true, false, request),
                createOffer(true, true, request)
        );
        result.sort(Comparator.comparing(LoanOfferDTO::getRate));
        return result;
    }

    private LoanOfferDTO createOffer(
            boolean isInsuranceEnabled,
            boolean isSalaryClient,
            LoanApplicationRequestDTO request) {
        BigDecimal rate = scoringService.getRate(isInsuranceEnabled, isSalaryClient);

        BigDecimal totalAmount = creditUtils.getTotalAmount(request.getAmount(), isInsuranceEnabled);
        BigDecimal monthlyPayment = creditUtils.getMonthlyPayment(totalAmount, rate, request.getTerm());

        return LoanOfferDTO.builder()
                .applicationId(0L)
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
