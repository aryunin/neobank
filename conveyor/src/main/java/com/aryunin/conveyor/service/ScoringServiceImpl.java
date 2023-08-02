package com.aryunin.conveyor.service;

import com.aryunin.conveyor.dto.EmploymentDTO;
import com.aryunin.conveyor.dto.ScoringDataDTO;
import com.aryunin.conveyor.dto.enums.EmploymentStatus;
import com.aryunin.conveyor.dto.enums.Gender;
import com.aryunin.conveyor.dto.enums.MaterialStatus;
import com.aryunin.conveyor.dto.enums.Position;
import com.aryunin.conveyor.exception.ScoringFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Service
@Slf4j
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
        log.info("calculating rate from ScoringDataDTO");
        BigDecimal rate = getRate(data.getIsInsuranceEnabled(), data.getIsSalaryClient());
        log.info("rate: " + rate);
        rate = checkEmployment(rate, data.getAmount(), data.getEmployment());
        log.info("rate: " + rate);
        rate = checkMaterialStatus(rate, data.getMaterialStatus());
        log.info("rate: " + rate);
        rate = checkDependentAmount(rate, data.getDependentAmount());
        log.info("rate: " + rate);
        rate = checkGenderAndAge(rate, data.getGender(), data.getBirthDate());
        log.info("resulting rate: " + rate);

        var comparison = rate.compareTo(minRate);
        if(comparison < 0) log.info("negative rate, using minimal rate: " + minRate);
        return (comparison < 0) ? minRate : rate;
    }

    @Override
    public BigDecimal getRate(boolean isInsuranceEnabled, boolean isSalaryClient) {
        log.info("calculating rate from insurance & salary client");
        var rate = new BigDecimal(baseRate.toString());
        rate = checkInsurance(rate, isInsuranceEnabled);
        log.info("rate: " + rate);
        rate = checkSalaryClient(rate, isSalaryClient);
        return rate;
    }

    private static BigDecimal checkEmployment(BigDecimal rate, BigDecimal amount, EmploymentDTO employment) {
        rate = checkEmploymentStatus(rate, employment.getEmploymentStatus());
        log.info("rate: " + rate);
        rate = checkWorkingExperience(rate, employment.getWorkExperienceCurrent(), employment.getWorkExperienceTotal());
        log.info("rate: " + rate);
        rate = checkSalary(rate, employment.getSalary(), amount);
        log.info("rate: " + rate);
        rate = checkPosition(rate, employment.getPosition());
        return rate;
    }

    private static BigDecimal checkInsurance(BigDecimal rate, boolean isInsuranceEnabled) {
        log.info("checking insurance");
        return (isInsuranceEnabled) ? rate.subtract(new BigDecimal(3)) : rate;
    }

    private static BigDecimal checkSalaryClient(BigDecimal rate, boolean isSalaryClient) {
        log.info("checking salary client");
        return (isSalaryClient) ? rate.subtract(new BigDecimal(1)) : rate;
    }

    private static BigDecimal checkMaterialStatus(BigDecimal rate, MaterialStatus materialStatus) {
        log.info("checking material status");
        return switch (materialStatus) {
            case MARRIED -> rate.subtract(new BigDecimal(3));
            case DIVORCED -> rate.add(new BigDecimal(1));
        };
    }

    private static BigDecimal checkDependentAmount(BigDecimal rate, Integer dependentCount) {
        log.info("checking dependent amount");
        return (dependentCount > 1) ? rate.add(new BigDecimal(1)) : rate;
    }

    private static BigDecimal checkGenderAndAge(BigDecimal rate, Gender gender, LocalDate birthDate) {
        log.info("checking gender & age");
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        log.info("age: " + age);
        rate = checkAge(rate, age);
        log.info("rate: " + rate);
        rate = checkGender(rate, gender, age);
        return rate;
    }

    private static BigDecimal checkAge(BigDecimal rate, int age) {
        log.info("checking age");
        if (age < 20 || age > 60)
            throw new ScoringFailedException("Inappropriate age.");
        return rate;
    }

    private static BigDecimal checkGender(BigDecimal rate, Gender gender, int age) {
        log.info("checking gender");
        return switch (gender) {
            case MALE -> (age >= 30 && age <= 55) ? rate.subtract(new BigDecimal(3)) : rate;
            case FEMALE -> (age >= 35 && age <= 60) ? rate.subtract(new BigDecimal(3)) : rate;
            case NON_BINARY -> rate.add(new BigDecimal(3));
        };
    }

    private static BigDecimal checkEmploymentStatus(BigDecimal rate, EmploymentStatus employmentStatus) {
        log.info("checking employment status");
        return switch (employmentStatus) {
            case UNEMPLOYED -> throw new ScoringFailedException("Inappropriate employment status.");
            case EMPLOYED -> rate;
            case SELF_EMPLOYED -> rate.add(new BigDecimal(1));
            case BUSINESSMAN -> rate.add(new BigDecimal(3));
        };
    }

    private static BigDecimal checkWorkingExperience(BigDecimal rate, Integer current, Integer total) {
        log.info("checking working experience");
        if(current < 3)
            throw new ScoringFailedException("Too little current work experience.");

        if(total < 12)
            throw new ScoringFailedException("Too little total work experience.");

        return rate;
    }

    private static BigDecimal checkSalary(BigDecimal rate, BigDecimal salary, BigDecimal amount) {
        log.info("checking salary");
        if(amount.compareTo(salary.multiply(new BigDecimal(20))) > 0)
            throw new ScoringFailedException("Too small salary.");

        return rate;
    }

    private static BigDecimal checkPosition(BigDecimal rate, Position position) {
        log.info("checking position");
        return switch (position) {
            case WORKER -> rate;
            case MANAGER -> rate.subtract(new BigDecimal(2));
            case TOP_MANAGER -> rate.subtract(new BigDecimal(4));
        };
    }
}
