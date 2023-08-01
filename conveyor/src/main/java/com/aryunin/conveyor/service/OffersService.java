package com.aryunin.conveyor.service;

import com.aryunin.conveyor.dto.LoanApplicationRequestDTO;
import com.aryunin.conveyor.dto.LoanOfferDTO;

import java.util.List;

public interface OffersService {
    List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO request);
}
