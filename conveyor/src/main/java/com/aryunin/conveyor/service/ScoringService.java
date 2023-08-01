package com.aryunin.conveyor.service;

import com.aryunin.conveyor.dto.ScoringDataDTO;

import java.math.BigDecimal;

public interface ScoringService {

    BigDecimal scoreRate(BigDecimal rate, ScoringDataDTO data);
}
