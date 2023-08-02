package com.aryunin.conveyor.service;

import com.aryunin.conveyor.dto.ScoringDataDTO;

import java.math.BigDecimal;

public interface ScoringService {
    BigDecimal getRate(ScoringDataDTO data);
    BigDecimal getRate(boolean isInsuranceEnabled, boolean isSalaryClient);
}
