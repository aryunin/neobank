package com.aryunin.conveyor.service;

import com.aryunin.conveyor.dto.LoanApplicationRequestDTO;
import com.aryunin.conveyor.dto.LoanOfferDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OffersService {
    private final ScoringService scoringService;

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
        // TODO
        return null;
    }
}
