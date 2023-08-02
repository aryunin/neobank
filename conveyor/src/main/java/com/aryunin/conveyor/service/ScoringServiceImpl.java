package com.aryunin.conveyor.service;

import com.aryunin.conveyor.dto.EmploymentDTO;
import com.aryunin.conveyor.dto.ScoringDataDTO;
import com.aryunin.conveyor.dto.enums.EmploymentStatus;
import com.aryunin.conveyor.dto.enums.Gender;
import com.aryunin.conveyor.dto.enums.MaterialStatus;
import com.aryunin.conveyor.dto.enums.Position;
import com.aryunin.conveyor.exception.ScoringFailedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

//TODO логирование
@Service
public class ScoringServiceImpl implements ScoringService {
    private final BigDecimal baseRate;
    private final BigDecimal minRate;

    public ScoringServiceImpl(
            @Value("${credit.base-rate}") BigDecimal baseRate,
            @Value("${credit.min-rate}") BigDecimal minRate
    ) {
        this.baseRate = baseRate;
        this.minRate = minRate;
    }

    @Override
    public BigDecimal getRate(ScoringDataDTO data) {
        BigDecimal rate = getRate(data.getIsInsuranceEnabled(), data.getIsSalaryClient());
        rate = checkEmployment(rate, data.getAmount(), data.getEmployment());
        rate = checkMaterialStatus(rate, data.getMaterialStatus());
        rate = checkDependentAmount(rate, data.getDependentAmount());
        rate = checkGenderAndAge(rate, data.getGender(), data.getBirthDate());
        return (rate.compareTo(minRate) < 0) ? minRate : rate;
    }

    @Override
    public BigDecimal getRate(boolean isInsuranceEnabled, boolean isSalaryClient) {
        var rate = new BigDecimal(baseRate.toString());
        rate = checkInsurance(rate, isInsuranceEnabled);
        rate = checkSalaryClient(rate, isSalaryClient);
        return rate;
    }

    private static BigDecimal checkEmployment(BigDecimal rate, BigDecimal amount, EmploymentDTO employment) {
        rate = checkEmploymentStatus(rate, employment.getEmploymentStatus());
        rate = checkWorkingExperience(rate, employment.getWorkExperienceCurrent(), employment.getWorkExperienceTotal());
        rate = checkSalary(rate, employment.getSalary(), amount);
        rate = checkPosition(rate, employment.getPosition());
        return rate;
    }

    private static BigDecimal checkInsurance(BigDecimal rate, boolean isInsuranceEnabled) {
        return (isInsuranceEnabled) ? rate.subtract(new BigDecimal(3)) : rate;
    }

    private static BigDecimal checkSalaryClient(BigDecimal rate, boolean isSalaryClient) {
        return (isSalaryClient) ? rate.subtract(new BigDecimal(1)) : rate;
    }

    private static BigDecimal checkMaterialStatus(BigDecimal rate, MaterialStatus materialStatus) {
        return switch (materialStatus) {
            case MARRIED -> rate.subtract(new BigDecimal(3));
            case DIVORCED -> rate.add(new BigDecimal(1));
        };
    }

    private static BigDecimal checkDependentAmount(BigDecimal rate, Integer dependentCount) {
        return (dependentCount > 1) ? rate.add(new BigDecimal(1)) : rate;
    }

    private static BigDecimal checkGenderAndAge(BigDecimal rate, Gender gender, LocalDate birthDate) {
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        rate = checkAge(rate, age);
        rate = checkGender(rate, gender, age);
        return rate;
    }

    private static BigDecimal checkAge(BigDecimal rate, int age) {
        if (age < 20 || age > 60)
            throw new ScoringFailedException("Inappropriate age.");
        return rate;
    }

    private static BigDecimal checkGender(BigDecimal rate, Gender gender, int age) {
        return switch (gender) {
            case MALE -> (age >= 30 && age <= 55) ? rate.subtract(new BigDecimal(3)) : rate;
            case FEMALE -> (age >= 35 && age <= 60) ? rate.subtract(new BigDecimal(3)) : rate;
            case NON_BINARY -> rate.add(new BigDecimal(3));
        };
    }

    private static BigDecimal checkEmploymentStatus(BigDecimal rate, EmploymentStatus employmentStatus) {
        return switch (employmentStatus) {
            case UNEMPLOYED -> throw new RuntimeException("Inappropriate employment status.");
            case EMPLOYED -> rate;
            case SELF_EMPLOYED -> rate.add(new BigDecimal(1));
            case BUSINESSMAN -> rate.add(new BigDecimal(3));
        };
    }

    private static BigDecimal checkWorkingExperience(BigDecimal rate, Integer current, Integer total) {
        if(current < 3)
            throw new RuntimeException("Too little current work experience.");

        if(total < 12)
            throw new RuntimeException("Too little total work experience.");

        return rate;
    }

    private static BigDecimal checkSalary(BigDecimal rate, BigDecimal salary, BigDecimal amount) {
        if(amount.compareTo(salary.multiply(new BigDecimal(20))) > 0)
            throw new RuntimeException("Too small salary.");

        return rate;
    }

    private static BigDecimal checkPosition(BigDecimal rate, Position position) {
        return switch (position) {
            case WORKER -> rate;
            case MANAGER -> rate.subtract(new BigDecimal(2));
            case TOP_MANAGER -> rate.subtract(new BigDecimal(4));
        };
    }
}
