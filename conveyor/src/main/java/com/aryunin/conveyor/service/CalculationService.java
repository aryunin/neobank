package com.aryunin.conveyor.service;

import com.aryunin.conveyor.dto.CreditDTO;
import com.aryunin.conveyor.dto.ScoringDataDTO;

public interface CalculationService {
    CreditDTO getCredit(ScoringDataDTO data);
}
