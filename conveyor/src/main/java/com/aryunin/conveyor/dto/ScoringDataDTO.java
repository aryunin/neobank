package com.aryunin.conveyor.dto;

import com.aryunin.conveyor.entity.enums.Gender;
import com.aryunin.conveyor.entity.enums.MaterialStatus;
import com.aryunin.conveyor.util.MoneySerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ScoringDataDTO {
    @JsonSerialize(using = MoneySerializer.class)
    @NotNull
    @Min(value = 10000, message = "too small amount")
    private BigDecimal amount;
    @NotNull
    @Min(value = 6, message = "too small term")
    private Integer term;
    @NotNull
    @Pattern(regexp = "[a-zA-Z]{2,30}", message = "invalid firstname format")
    private String firstName;
    @NotNull
    @Pattern(regexp = "[a-zA-Z]{2,30}", message = "invalid lastname format")
    private String lastName;
    @Pattern(regexp = "[a-zA-Z]{2,30}", message = "invalid middlename format")
    private String middleName;
    @NotNull
    private Gender gender;
    @JsonFormat(pattern="yyyy-MM-dd")
    @NotNull
    private LocalDate birthDate;
    @NotNull
    @Pattern(regexp = "\\d{4}")
    private String passportSeries;
    @NotNull
    @Pattern(regexp = "\\d{6}")
    private String passportNumber;
    @JsonFormat(pattern="yyyy-MM-dd")
    @NotNull
    private LocalDate passportIssueDate;
    @NotNull
    @Pattern(regexp = "\\d{3}-\\d{3}", message = "invalid passport issue branch format")
    private String passportIssueBranch;
    @NotNull
    private MaterialStatus materialStatus;
    @NotNull
    @Min(value = 0, message = "negative dependent amount")
    private Integer dependentAmount; // fixme что это?
    @NotNull
    private EmploymentDTO employment;
    @NotNull
    @Pattern(regexp = "\\d{20}")
    private String account;
    @NotNull
    private Boolean isInsuranceEnabled;
    @NotNull
    private Boolean isSalaryClient;
}
