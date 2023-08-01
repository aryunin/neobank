package com.aryunin.conveyor.service.calculation;

import com.aryunin.conveyor.dto.ScoringDataDTO;

import java.math.BigDecimal;

public interface ScoringService {

    BigDecimal scoreRate(BigDecimal rate, ScoringDataDTO data);
}
