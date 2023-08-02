package com.aryunin.conveyor.dto;

import com.aryunin.conveyor.dto.enums.Gender;
import com.aryunin.conveyor.dto.enums.MaterialStatus;
import com.aryunin.conveyor.util.DecimalSerializer;
import com.aryunin.conveyor.validation.DateInPast;
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
    @JsonSerialize(using = DecimalSerializer.class)
    @NotNull(message = "field must not be null")
    @Min(value = 10000, message = "too small amount")
    private BigDecimal amount;
    @NotNull(message = "field must not be null")
    @Min(value = 6, message = "too small term")
    private Integer term;
    @NotNull(message = "field must not be null")
    @Pattern(regexp = "[a-zA-Z]{2,30}", message = "invalid firstname format")
    private String firstName;
    @NotNull(message = "field must not be null")
    @Pattern(regexp = "[a-zA-Z]{2,30}", message = "invalid lastname format")
    private String lastName;
    @Pattern(regexp = "[a-zA-Z]{2,30}", message = "invalid middlename format")
    private String middleName;
    @NotNull(message = "field must not be null")
    private Gender gender;
    @JsonFormat(pattern="yyyy-MM-dd")
    @NotNull(message = "field must not be null")
    @DateInPast(message = "the date must be in the past")
    private LocalDate birthDate;
    @NotNull(message = "field must not be null")
    @Pattern(regexp = "\\d{4}")
    private String passportSeries;
    @NotNull(message = "field must not be null")
    @Pattern(regexp = "\\d{6}")
    private String passportNumber;
    @JsonFormat(pattern="yyyy-MM-dd")
    @NotNull(message = "field must not be null")
    @DateInPast(message = "the date must be in the past")
    private LocalDate passportIssueDate;
    @NotNull(message = "field must not be null")
    @Pattern(regexp = "\\d{3}-\\d{3}", message = "invalid passport issue branch format")
    private String passportIssueBranch;
    @NotNull(message = "field must not be null")
    private MaterialStatus materialStatus;
    @NotNull(message = "field must not be null")
    @Min(value = 0, message = "negative dependent amount")
    private Integer dependentAmount;
    @NotNull(message = "field must not be null")
    private EmploymentDTO employment;
    @NotNull(message = "field must not be null")
    @Pattern(regexp = "\\d{20}")
    private String account;
    @NotNull(message = "field must not be null")
    private Boolean isInsuranceEnabled;
    @NotNull(message = "field must not be null")
    private Boolean isSalaryClient;
}
