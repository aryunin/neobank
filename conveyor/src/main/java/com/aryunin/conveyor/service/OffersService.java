package com.aryunin.conveyor.service;

import com.aryunin.conveyor.dto.LoanApplicationRequestDTO;
import com.aryunin.conveyor.dto.LoanOfferDTO;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class OffersService {
    public List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO request) {
        return Collections.emptyList();
    }
}
