package com.aryunin.conveyor.service;

import com.aryunin.conveyor.dto.EmploymentDTO;
import com.aryunin.conveyor.dto.enums.EmploymentStatus;
import com.aryunin.conveyor.dto.enums.Gender;
import com.aryunin.conveyor.dto.enums.MaterialStatus;
import com.aryunin.conveyor.dto.enums.Position;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;

//TODO логирование
@Service
public class ScoringServiceImpl implements ScoringService {
    @Value("${credit.base-rate}")
    private BigDecimal baseRate;
    @Value("${credit.insurance-rate}")
    private BigDecimal insuranceRate;
    @Value("${decimal-scaling.calculation}")
    private int scale;

    @Override
    public BigDecimal getRate() {
        return baseRate;
    }

    @Override
    public BigDecimal getInsuranceRate() {
        return insuranceRate;
    }

    @Override
    public BigDecimal getMonthlyPayment(BigDecimal totalAmount, BigDecimal rate, Integer term) {
        var rpm = rate.divide(new BigDecimal("1200"), scale, RoundingMode.HALF_UP);
        var x = totalAmount.multiply(rpm);
        var y = new BigDecimal(1).add(rpm).pow(term);
        y = new BigDecimal(1).divide(y, scale, RoundingMode.HALF_UP);
        y = new BigDecimal(1).subtract(y);
        return x.divide(y, scale, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getInsuranceAmount(BigDecimal amount) {
        return amount
                .multiply(insuranceRate)
                .divide(new BigDecimal("100"), scale, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getTotalAmount(BigDecimal amount, boolean isInsuranceEnabled) {
        return (isInsuranceEnabled) ? amount.add(getInsuranceAmount(amount)) : amount;
    }

    @Override
    public BigDecimal modifyRate(BigDecimal rate, boolean isInsuranceEnabled, boolean isSalaryClient) {
        if(isInsuranceEnabled) rate = rate.subtract(new BigDecimal("3.0"));
        if(isSalaryClient) rate = rate.subtract(new BigDecimal("1.0"));
        return rate;
    }

    @Override
    public BigDecimal modifyRate(BigDecimal rate, BigDecimal amount, EmploymentDTO employment) {
        rate = checkEmploymentStatus(rate, employment.getEmploymentStatus());
        rate = checkWorkingExperience(rate, employment.getWorkExperienceCurrent(), employment.getWorkExperienceTotal());
        rate = checkSalary(rate, employment.getSalary(), amount);
        rate = checkPosition(rate, employment.getPosition());
        return rate;
    }

    @Override
    public BigDecimal modifyRate(BigDecimal rate, MaterialStatus materialStatus) {
        return switch (materialStatus) {
            case MARRIED -> rate.subtract(new BigDecimal(3));
            case DIVORCED -> rate.add(new BigDecimal(1));
        };
    }

    @Override
    public BigDecimal modifyRate(BigDecimal rate, Integer dependentCount) {
        return (dependentCount > 1) ? rate.add(new BigDecimal(1)) : rate;
    }

    @Override
    public BigDecimal modifyRate(BigDecimal rate, Gender gender, LocalDate birthDate) {
        int age = getAge(birthDate);
        rate = checkAge(rate, age);
        rate = checkGender(rate, gender, age);
        return rate;
    }

    private int getAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    private BigDecimal checkAge(BigDecimal rate, int age) {
        if (age < 20 || age > 60)
            throw new RuntimeException(""); // TODO exc
        return rate;
    }

    private BigDecimal checkGender(BigDecimal rate, Gender gender, int age) {
        return switch (gender) {
            case MALE -> (age >= 30 && age <= 55) ? rate.subtract(new BigDecimal(3)) : rate;
            case FEMALE -> (age >= 35 && age <= 60) ? rate.subtract(new BigDecimal(3)) : rate;
            case NON_BINARY -> rate.add(new BigDecimal(3));
        };
    }

    private BigDecimal checkEmploymentStatus(BigDecimal rate, EmploymentStatus employmentStatus) {
        return switch (employmentStatus) {
            case UNEMPLOYED -> throw new RuntimeException(""); // TODO exc
            case EMPLOYED -> rate;
            case SELF_EMPLOYED -> rate.add(new BigDecimal(1));
            case BUSINESSMAN -> rate.add(new BigDecimal(3));
        };
    }

    private BigDecimal checkWorkingExperience(BigDecimal rate, Integer current, Integer total) {
        if(current < 3)
            throw new RuntimeException(""); // TODO exc

        if(total < 12)
            throw new RuntimeException(""); // TODO exc

        return rate;
    }

    private BigDecimal checkSalary(BigDecimal rate, BigDecimal salary, BigDecimal amount) {
        if(amount.compareTo(salary.multiply(new BigDecimal(20))) > 0)
            throw new RuntimeException(""); // TODO exc

        return rate;
    }

    private BigDecimal checkPosition(BigDecimal rate, Position position) {
        return switch (position) {
            case WORKER -> rate;
            case MANAGER -> rate.subtract(new BigDecimal(2));
            case TOP_MANAGER -> rate.subtract(new BigDecimal(4));
        };
    }
}
