package com.aryunin.conveyor.service;

import com.aryunin.conveyor.dto.EmploymentDTO;
import com.aryunin.conveyor.dto.ScoringDataDTO;
import com.aryunin.conveyor.dto.enums.EmploymentStatus;
import com.aryunin.conveyor.dto.enums.Gender;
import com.aryunin.conveyor.dto.enums.MaterialStatus;
import com.aryunin.conveyor.dto.enums.Position;
import com.aryunin.conveyor.exception.ScoringFailedException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ScoringServiceImplTest {
    private final ScoringServiceImpl scoringService =
            new ScoringServiceImpl(new BigDecimal("8"), new BigDecimal("3.0"));

    @Test
    void getRate_preScoring_success() {
        var rate = scoringService.getRate(false, false)
                .setScale(0, RoundingMode.HALF_UP);
        var rateSal = scoringService.getRate(false, true)
                .setScale(0, RoundingMode.HALF_UP);
        var rateIns = scoringService.getRate(true, false)
                .setScale(0, RoundingMode.HALF_UP);
        var rateInsSal = scoringService.getRate(true, true)
                .setScale(0, RoundingMode.HALF_UP);

        assertEquals(new BigDecimal("8"), rate);
        assertEquals(new BigDecimal("7"), rateSal);
        assertEquals(new BigDecimal("5"), rateIns);
        assertEquals(new BigDecimal("4"), rateInsSal);
    }

    @Test
    void getRate_scoring_success() {
        EmploymentDTO employment = EmploymentDTO.builder()
                .employmentStatus(EmploymentStatus.EMPLOYED)
                .workExperienceCurrent(3)
                .workExperienceTotal(12)
                .salary(new BigDecimal("20000"))
                .position(Position.WORKER)
                .build();

        ScoringDataDTO data = ScoringDataDTO.builder()
                .amount(new BigDecimal("100000"))
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .employment(employment)
                .materialStatus(MaterialStatus.MARRIED) // rate - 3
                .dependentAmount(1)
                .gender(Gender.FEMALE)
                .birthDate(LocalDate.now().minusYears(33))
                .build();

        var rate = scoringService.getRate(data).setScale(0, RoundingMode.HALF_UP);
        assertEquals(new BigDecimal("5"), rate);

        data.getEmployment().setEmploymentStatus(EmploymentStatus.SELF_EMPLOYED); // rate + 1
        rate = scoringService.getRate(data).setScale(0, RoundingMode.HALF_UP);
        assertEquals(new BigDecimal("6"), rate);

        data.getEmployment().setEmploymentStatus(EmploymentStatus.BUSINESSMAN); // rate + 2
        rate = scoringService.getRate(data).setScale(0, RoundingMode.HALF_UP);
        assertEquals(new BigDecimal("8"), rate);

        data.getEmployment().setPosition(Position.MANAGER); // rate - 2
        rate = scoringService.getRate(data).setScale(0, RoundingMode.HALF_UP);
        assertEquals(new BigDecimal("6"), rate);

        data.getEmployment().setPosition(Position.TOP_MANAGER); // rate - 2
        rate = scoringService.getRate(data).setScale(0, RoundingMode.HALF_UP);
        assertEquals(new BigDecimal("4"), rate);

        data.setMaterialStatus(MaterialStatus.DIVORCED); // rate + 4
        rate = scoringService.getRate(data).setScale(0, RoundingMode.HALF_UP);
        assertEquals(new BigDecimal("8"), rate);

        data.setGender(Gender.MALE); // rate - 3
        rate = scoringService.getRate(data).setScale(0, RoundingMode.HALF_UP);
        assertEquals(new BigDecimal("5"), rate);

        data.setBirthDate(LocalDate.now().minusYears(25)); // rate + 3
        rate = scoringService.getRate(data).setScale(0, RoundingMode.HALF_UP);
        assertEquals(new BigDecimal("8"), rate);

        data.setBirthDate(LocalDate.now().minusYears(57));
        rate = scoringService.getRate(data).setScale(0, RoundingMode.HALF_UP);
        assertEquals(new BigDecimal("8"), rate);

        data.setGender(Gender.FEMALE); // rate - 3
        rate = scoringService.getRate(data).setScale(0, RoundingMode.HALF_UP);
        assertEquals(new BigDecimal("5"), rate);

        data.setGender(Gender.NON_BINARY); // rate + 6
        rate = scoringService.getRate(data).setScale(0, RoundingMode.HALF_UP);
        assertEquals(new BigDecimal("11"), rate);

        data.setDependentAmount(2); // rate + 1
        rate = scoringService.getRate(data).setScale(0, RoundingMode.HALF_UP);
        assertEquals(new BigDecimal("12"), rate);

        data.setDependentAmount(1); // rate - 1 => 11
        data.setGender(Gender.FEMALE); // rate - 6 => 5
        data.getEmployment().setEmploymentStatus(EmploymentStatus.EMPLOYED); // rate - 3 => 2, but min is 3
        rate = scoringService.getRate(data).setScale(0, RoundingMode.HALF_UP);
        assertEquals(new BigDecimal("3"), rate);
    }

    @Test
    void getRate_scoring_throws() {
        EmploymentDTO employment = EmploymentDTO.builder()
                .employmentStatus(EmploymentStatus.EMPLOYED)
                .workExperienceCurrent(3)
                .workExperienceTotal(12)
                .salary(new BigDecimal("20000"))
                .position(Position.WORKER)
                .build();

        ScoringDataDTO data = ScoringDataDTO.builder()
                .amount(new BigDecimal("100000"))
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .employment(employment)
                .materialStatus(MaterialStatus.MARRIED) // rate - 3
                .dependentAmount(1)
                .gender(Gender.FEMALE)
                .birthDate(LocalDate.now().minusYears(33))
                .build();

        assertDoesNotThrow(() -> scoringService.getRate(data));

        data.getEmployment().setEmploymentStatus(EmploymentStatus.UNEMPLOYED);
        assertThrows(ScoringFailedException.class, () -> scoringService.getRate(data));

        data.getEmployment().setEmploymentStatus(EmploymentStatus.EMPLOYED);
        data.getEmployment().setSalary(new BigDecimal("4999"));
        assertThrows(ScoringFailedException.class, () -> scoringService.getRate(data));

        data.getEmployment().setSalary(new BigDecimal("20000"));
        data.setBirthDate(LocalDate.now().minusYears(19));
        assertThrows(ScoringFailedException.class, () -> scoringService.getRate(data));

        data.setBirthDate(LocalDate.now().minusYears(61));
        assertThrows(ScoringFailedException.class, () -> scoringService.getRate(data));

        data.setBirthDate(LocalDate.now().minusYears(25));
        data.getEmployment().setWorkExperienceCurrent(2);
        assertThrows(ScoringFailedException.class, () -> scoringService.getRate(data));

        data.getEmployment().setWorkExperienceCurrent(3);
        data.getEmployment().setWorkExperienceTotal(11);
        assertThrows(ScoringFailedException.class, () -> scoringService.getRate(data));

        data.getEmployment().setWorkExperienceTotal(12);
        assertDoesNotThrow(() -> scoringService.getRate(data));
    }
}