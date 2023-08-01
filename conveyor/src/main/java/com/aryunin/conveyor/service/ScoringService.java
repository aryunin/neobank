package com.aryunin.conveyor.service;

import com.aryunin.conveyor.dto.EmploymentDTO;
import com.aryunin.conveyor.dto.enums.Gender;
import com.aryunin.conveyor.dto.enums.MaterialStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ScoringService {
    BigDecimal getRate();
    BigDecimal getInsuranceRate();
    BigDecimal getMonthlyPayment(BigDecimal totalAmount, BigDecimal rate, Integer term);
    BigDecimal getInsuranceAmount(BigDecimal amount);
    BigDecimal getTotalAmount(BigDecimal amount, boolean isInsuranceEnabled);
    BigDecimal modifyRate(BigDecimal rate, boolean isInsuranceEnabled, boolean isSalaryClient);
    BigDecimal modifyRate(BigDecimal rate, BigDecimal amount, EmploymentDTO employment);
    BigDecimal modifyRate(BigDecimal rate, MaterialStatus materialStatus);
    BigDecimal modifyRate(BigDecimal rate, Integer dependentCount);
    BigDecimal modifyRate(BigDecimal rate, Gender gender, LocalDate birthDate);
}
